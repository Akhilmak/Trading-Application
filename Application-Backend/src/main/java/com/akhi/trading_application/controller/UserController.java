package com.akhi.trading_application.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akhi.trading_application.domain.VerificationType;
import com.akhi.trading_application.modal.ForgotPasswordToken;
import com.akhi.trading_application.modal.User;
import com.akhi.trading_application.modal.VerificationCode;
import com.akhi.trading_application.request.ForgotPasswordTokenRequest;
import com.akhi.trading_application.request.ResetPasswordRequest;
import com.akhi.trading_application.response.ApiResponse;
import com.akhi.trading_application.response.AuthResponse;
import com.akhi.trading_application.service.EmailService;
import com.akhi.trading_application.service.ForgotPasswordService;
import com.akhi.trading_application.service.UserService;
import com.akhi.trading_application.service.VerificationCodeService;
import com.akhi.trading_application.utils.OtpUtils;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    private String jwt;

    @GetMapping("/api/user/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt)throws Exception{
        User user=userService.findUserByJwt(jwt);
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    @PostMapping("/api/user/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVeficationOtp(@RequestHeader("Authorization") String jwt, @PathVariable VerificationType verificationType)throws Exception{
        User user=userService.findUserByJwt(jwt);

        VerificationCode verificationCode=verificationCodeService.getVerificationCodeByUser(user.getId());
        if(verificationCode==null){
            verificationCode=verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(),verificationCode.getOtp());
        }
        return new ResponseEntity<String>("Verification OTP SuccessFully Sent...!",HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable String otp,@RequestHeader("Authorization") String jwt)throws Exception{
        User user=userService.findUserByJwt(jwt);

        VerificationCode verificationCode=verificationCodeService.getVerificationCodeByUser(user.getId());
        String sendTo=verificationCode.getVerificationType().equals(VerificationType.EMAIL)?verificationCode.getEmail():verificationCode.getMobile();
        boolean isVerified=verificationCode.getOtp().equals(otp);
        if(isVerified){
            User updatedUser=userService.enable2FA(verificationCode.getVerificationType(), sendTo, user);

            verificationCodeService.deleteVerificationCodeById(verificationCode);

            return new ResponseEntity<User>(updatedUser,HttpStatus.OK);
        }
        throw new Exception("Wrong Otp"); 
    }


    //end point for sending Forgot password otp
    @PostMapping("/api/user/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp( @RequestBody ForgotPasswordTokenRequest req)throws Exception{
        
        User user=userService.findUserByEmail(req.getSendTo());
        String otp=OtpUtils.generateOtp();
        UUID uuid=UUID.randomUUID();
        String id=uuid.toString();


        ForgotPasswordToken token=forgotPasswordService.findByUser(user.getId());
        if(token==null){
            token=forgotPasswordService.creatToken(user, id, otp, req.getVerificationType(), req.getSendTo());
        }

        if(req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
        }

        AuthResponse res=new AuthResponse();
        res.setSession(token.getId());
        res.setMessage("Password reset OTP sent Sucessfully.....!");

        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    //endpoint for verifying forgot password otp 

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id,@RequestBody ResetPasswordRequest req,@RequestHeader("Authorization") String jwt)throws Exception{
        

        ForgotPasswordToken forgotPasswordToken=forgotPasswordService.findById(id);
        boolean isVerified=forgotPasswordToken.getOtp().equals(req.getOtp());
        
        if(isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(),req.getPassword());
            ApiResponse response=new ApiResponse();
            response.setMessage("Password Updated Successfully....!");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        throw new Exception("Wrong OTP...!");
    }
}
