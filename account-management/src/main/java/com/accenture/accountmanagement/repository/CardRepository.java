package com.accenture.accountmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accenture.accountmanagement.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByCardNumber(String cardNumber);

    boolean existsByCardNumberAndIdNot(String cardNumber, Long id);

    @Query("SELECT c FROM Card c JOIN FETCH c.account WHERE c.account.profile.id = :profileId")
    List<Card> findByProfileId(@Param("profileId") Long profileId);

    @Query("SELECT c FROM Card c JOIN FETCH c.account WHERE c.account.id = :accountId")
    List<Card> findByAccountId(@Param("accountId") Long accountId);
}
