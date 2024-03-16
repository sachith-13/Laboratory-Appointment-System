package com.djamware.springsecuritymongo.services;

import com.djamware.springsecuritymongo.domain.Lab;
import com.djamware.springsecuritymongo.repositories.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class LabService {
    @Autowired
    private LabRepository labRepository;

    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }

    public Optional<Lab> findLabById(String labId) {
        return labRepository.findById(labId);
    }


    public Lab addLab(Lab lab, String createdBy) {
        // Set the createdBy field of the lab
        lab.setUserId(createdBy);
        return labRepository.save(lab);
    }


    public void deleteLab(String labId) {
        labRepository.deleteById(labId);
    }
    // Additional methods implementation
}


