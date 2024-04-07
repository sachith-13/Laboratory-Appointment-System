package com.djamware.springsecuritymongo.controllers;

import com.djamware.springsecuritymongo.domain.Lab;
import com.djamware.springsecuritymongo.services.BookingService;
import com.djamware.springsecuritymongo.services.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class LabController {

    private LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping("admin/labs")
    public String getAllLabs(Model model) {
        List<Lab> labs = labService.getAllLabs();
        model.addAttribute("labs", labs);
        return "/admin/labs"; // Return the name of your Thymeleaf template
    }

    @PostMapping("/addLab")
    public ResponseEntity<Object> addLab(@RequestBody Lab lab) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName(); // Retrieves the logged-in user's user ID
            lab.setUserId(userId);
            Lab savedLab = labService.addLab(lab, userId);
            return ResponseEntity.status(HttpStatus.OK).body("{\"successMessage\": \"Lab added successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"errorMessage\": \"Failed to add lab: " + e.getMessage() + "\"}");
        }
    }


    @PostMapping("/deleteLab")
    public String deleteLab(@RequestParam("labId") String labId, RedirectAttributes redirectAttributes) {
        try {
            labService.deleteLab(labId);
            redirectAttributes.addFlashAttribute("DeletesuccessMessage", "Lab deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete lab: " + e.getMessage());
        }
        return "redirect:/admin/labs"; // Redirect to the dashboard after lab deletion
    }
}


