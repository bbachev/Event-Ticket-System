package eventticketsystem.gateway.controller;

import eventticketsystem.gateway.adapter.PreferenceAdapter;
import eventticketsystem.gateway.dto.preference.PreferenceRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/preferences")
public class PreferenceController {
    private final PreferenceAdapter preferenceAdapter;

    public PreferenceController(PreferenceAdapter preferenceAdapter) {
        this.preferenceAdapter = preferenceAdapter;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> addPreferences(@PathVariable UUID id, @RequestBody PreferenceRequest request) {
       this.preferenceAdapter.addUserPreferences(id, request);
       return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPreferences(@PathVariable UUID id){
        return ResponseEntity.ok(this.preferenceAdapter.getUserPreference(id));
    }
}

