package com.accenture.accountmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.accountmanagement.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
