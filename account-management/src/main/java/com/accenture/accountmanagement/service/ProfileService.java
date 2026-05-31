package com.accenture.accountmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.accenture.accountmanagement.model.Account;
import com.accenture.accountmanagement.model.Profile;
import com.accenture.accountmanagement.repository.ProfileRepository;

@Service
public class ProfileService {
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final ProfileRepository profileRepository;
    private final AccountService accountService;

    public ProfileService(ProfileRepository profileRepository, AccountService accountService) {
        this.profileRepository = profileRepository;
        this.accountService = accountService;
    }

    public List<Profile> getAllProfiles() {
        logger.info("Fetching all profiles.");
        return profileRepository.findAll();
    }

    public Profile getProfileById(Long id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            logger.warn("Profile id not found: {}", id);
            return new RuntimeException("Profile id not found: " + id);
        });
    }

    public Profile createProfile(Profile profile) {
        Profile saved = profileRepository.save(profile);
        return saved;
    }

    public Account createAccountForProfile(Long profileId, Account account) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> {
            logger.warn("Profile id not found: {}", profileId);
            return new RuntimeException("Profile id not found: " + profileId);
        });
        account.setProfile(profile);
        return accountService.createAccount(account);
    }
}
