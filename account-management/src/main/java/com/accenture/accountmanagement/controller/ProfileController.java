package com.accenture.accountmanagement.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.accountmanagement.dto.AccountRequest;
import com.accenture.accountmanagement.dto.AccountResponse;
import com.accenture.accountmanagement.dto.CardResponse;
import com.accenture.accountmanagement.dto.ProfileRequest;
import com.accenture.accountmanagement.dto.ProfileResponse;
import com.accenture.accountmanagement.model.Profile;
import com.accenture.accountmanagement.service.ProfileService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping()
    public List<ProfileResponse> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public Profile getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse saved = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ProfileResponse updateProfile(@PathVariable Long id, @Valid @RequestBody ProfileRequest request) {
        return profileService.updateProfile(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/accounts")
    public List<AccountResponse> getAccountsByProfileId(@PathVariable Long id) {
        return profileService.getAccountsByProfileId(id);
    }

    @GetMapping("/{id}/cards")
    public List<CardResponse> getCardsByProfileId(@PathVariable Long id) {
        return profileService.getCardsByProfileId(id);
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable Long id,
            @Valid @RequestBody AccountRequest account) {
        AccountResponse created = profileService.createAccountForProfile(id, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
