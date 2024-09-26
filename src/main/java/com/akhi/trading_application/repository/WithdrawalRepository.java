package com.akhi.trading_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akhi.trading_application.modal.Withdrawal;

public interface WithdrawalRepository extends JpaRepository<Withdrawal,Long>{

    List<Withdrawal> findByUserId(Long userId);

}
