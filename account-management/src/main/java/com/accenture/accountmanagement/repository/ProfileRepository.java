package com.accenture.accountmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.accountmanagement.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Override
    @EntityGraph(attributePaths = "accounts")
    List<Profile> findAll();

    @Override
    @EntityGraph(attributePaths = "accounts")
    Optional<Profile> findById(Long id);
}
