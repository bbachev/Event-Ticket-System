package eventticketsystem.preference.controller;

import eventticketsystem.preference.dto.PreferenceCategory;
import eventticketsystem.preference.dto.PreferenceRequest;
import eventticketsystem.preference.dto.UserDetailsRequest;
import eventticketsystem.preference.exception.UserNotExistsException;
import eventticketsystem.preference.service.PreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class PreferenceController {
    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @GetMapping("/preferences")
    public ResponseEntity<?> searchByCategory(@RequestParam PreferenceCategory category){
        this.preferenceService.searchByPreference(category);
        return null;
    }

    @GetMapping("/preferences/{userId}")
    public ResponseEntity<?> getUserPreferences(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok().body(this.preferenceService.getUserPreferences(userId));
        } catch (UserNotExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/preferences/{userId}")
    public ResponseEntity<?> addUserPreferences(@PathVariable UUID userId, @RequestBody PreferenceRequest request) {
        try {
            this.preferenceService.addUserPreferences(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserNotExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/preferences/user-details/{userId}")
    public ResponseEntity<?> addUserPreferences(@PathVariable UUID userId, @RequestBody UserDetailsRequest request) {
        try {
            this.preferenceService.addUserDetails(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserNotExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
