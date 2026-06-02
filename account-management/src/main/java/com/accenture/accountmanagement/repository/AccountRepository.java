package com.accenture.accountmanagement.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accenture.accountmanagement.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByProfileId(Long profileId);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.id = :id AND a.balance + :amount >= 0")
    int updateBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);
}
