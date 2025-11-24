package com.example.edugo.controller;

import com.example.edugo.dto.DeviceRegistrationRequest;
import com.example.edugo.entity.Principales.Device;
import com.example.edugo.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Tag(name = "Devices", description = "Gestion des appareils et notifications push")
@SecurityRequirement(name = "bearerAuth")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/register")
    @Operation(summary = "Enregistrer un appareil pour les notifications push", 
               description = "Permet d'enregistrer ou mettre à jour un appareil (OneSignal Player ID) pour un utilisateur")
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    public ResponseEntity<Device> registerDevice(@RequestBody DeviceRegistrationRequest request) {
        Device device = deviceService.registerDevice(request);
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/{playerId}")
    @Operation(summary = "Désactiver un appareil", 
               description = "Désactive un appareil lorsque l'utilisateur se déconnecte ou désinstalle l'application")
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    public ResponseEntity<Void> deactivateDevice(
            @Parameter(description = "OneSignal Player ID") @PathVariable String playerId) {
        deviceService.deactivateDevice(playerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Récupérer les appareils d'un utilisateur", 
               description = "Récupère la liste de tous les appareils actifs d'un utilisateur")
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    public ResponseEntity<java.util.List<Device>> getUserDevices(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId) {
        return ResponseEntity.ok(deviceService.getUserDevices(userId));
    }
}

