package com.accenture.accountmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.accenture.accountmanagement.dto.AccountRequest;
import com.accenture.accountmanagement.dto.AccountResponse;
import com.accenture.accountmanagement.dto.ProfileRequest;
import com.accenture.accountmanagement.dto.ProfileResponse;
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

    public List<ProfileResponse> getAllProfiles() {
        logger.info("Fetching all profiles.");
        return profileRepository.findAll().stream()
                .map(ProfileResponse::fromEntity)
                .toList();
    }

    public ProfileResponse getProfileById(Long id) {
        return ProfileResponse.fromEntity(findEntityById(id));
    }

    private Profile findEntityById(Long id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            logger.warn("Profile id not found: {}", id);
            return new RuntimeException("Profile id not found: " + id);
        });
    }

    public ProfileResponse createProfile(ProfileRequest request) {
        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setMiddleName(request.getMiddleName());
        profile.setLastName(request.getLastName());
        profile.setEmail(request.getEmail());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setDateOfBirth(request.getDateOfBirth());
        Profile saved = profileRepository.save(profile);
        return ProfileResponse.fromEntity(saved);
    }

    public ProfileResponse updateProfile(Long id, ProfileRequest request) {
        Profile existing = findEntityById(id);
        logger.info("Updating Profile: {}", id);
        existing.setFirstName(request.getFirstName());
        existing.setMiddleName(request.getMiddleName());
        existing.setLastName(request.getLastName());
        existing.setDateOfBirth(request.getDateOfBirth());
        existing.setEmail(request.getEmail());
        existing.setPhoneNumber(request.getPhoneNumber());
        Profile saved = profileRepository.save(existing);
        logger.info("Updated Profile: {}", id);
        return ProfileResponse.fromEntity(saved);
    }

    public void deleteProfile(Long id) {
        logger.info("Deleting profile: {}", id);
        profileRepository.deleteById(id);
    }

    public List<AccountResponse> getAccountsByProfileId(Long profileId) {
        findEntityById(profileId);
        return accountService.getAccountsByProfileId(profileId);
    }

    public AccountResponse createAccountForProfile(Long profileId, AccountRequest request) {
        Profile profile = findEntityById(profileId);
        Account account = new Account();
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setAccountStatus(request.getAccountStatus());
        account.setCurrency(request.getCurrency());
        account.setProfile(profile);
        return accountService.createAccount(account);
    }
}
