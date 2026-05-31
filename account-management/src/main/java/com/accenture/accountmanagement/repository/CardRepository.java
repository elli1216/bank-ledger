package com.accenture.accountmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.accountmanagement.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
