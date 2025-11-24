package com.example.edugo.service;

import com.example.edugo.dto.DeviceRegistrationRequest;
import com.example.edugo.entity.Principales.Device;
import com.example.edugo.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer l'enregistrement et la gestion des appareils (OneSignal Player IDs)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;

    /**
     * Enregistre ou met à jour un appareil pour un utilisateur
     */
    @Transactional
    public Device registerDevice(DeviceRegistrationRequest request) {
        // Vérifier si le device existe déjà
        Optional<Device> existingDevice = deviceRepository.findByOneSignalPlayerId(request.getOneSignalPlayerId());

        if (existingDevice.isPresent()) {
            // Mettre à jour le device existant
            Device device = existingDevice.get();
            device.setUserId(request.getUserId());
            device.setUserRole(request.getUserRole());
            device.setPlatform(request.getPlatform());
            device.setDeviceModel(request.getDeviceModel());
            device.setAppVersion(request.getAppVersion());
            device.setIsActive(true);
            
            log.info("Device mis à jour: {} pour l'utilisateur {}", request.getOneSignalPlayerId(), request.getUserId());
            return deviceRepository.save(device);
        } else {
            // Créer un nouveau device
            Device device = new Device();
            device.setOneSignalPlayerId(request.getOneSignalPlayerId());
            device.setUserId(request.getUserId());
            device.setUserRole(request.getUserRole());
            device.setPlatform(request.getPlatform());
            device.setDeviceModel(request.getDeviceModel());
            device.setAppVersion(request.getAppVersion());
            device.setIsActive(true);
            
            log.info("Nouveau device enregistré: {} pour l'utilisateur {}", request.getOneSignalPlayerId(), request.getUserId());
            return deviceRepository.save(device);
        }
    }

    /**
     * Désactive un appareil (lors de la déconnexion ou désinstallation de l'app)
     */
    @Transactional
    public void deactivateDevice(String oneSignalPlayerId) {
        Optional<Device> device = deviceRepository.findByOneSignalPlayerId(oneSignalPlayerId);
        if (device.isPresent()) {
            device.get().setIsActive(false);
            deviceRepository.save(device.get());
            log.info("Device désactivé: {}", oneSignalPlayerId);
        }
    }

    /**
     * Récupère tous les appareils actifs d'un utilisateur
     */
    public List<Device> getUserDevices(Long userId) {
        return deviceRepository.findByUserIdAndActive(userId);
    }

    /**
     * Récupère tous les appareils actifs d'un rôle
     */
    public List<Device> getRoleDevices(String role) {
        return deviceRepository.findByRoleAndActive(role);
    }
}

