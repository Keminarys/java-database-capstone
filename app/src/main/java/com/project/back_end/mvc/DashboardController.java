package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.project.back_end.service.Service;

@Controller
public class DashboardController {

    @Autowired
    private Service sharedService;

    @GetMapping("/adminDashboard/{token}")
    public ModelAndView adminDashboard(@PathVariable String token) {
        String validationResult = sharedService.validateToken(token, "admin");
        if (validationResult == null || validationResult.isEmpty()) {
            return new ModelAndView("admin/adminDashboard");
        } else {
            return new ModelAndView("redirect:/");
        }
    }

    @GetMapping("/doctorDashboard/{token}")
    public ModelAndView doctorDashboard(@PathVariable String token) {
        String validationResult = sharedService.validateToken(token, "doctor");
        if (validationResult == null || validationResult.isEmpty()) {
            return new ModelAndView("doctor/doctorDashboard");
        } else {
            return new ModelAndView("redirect:/");
        }
    }
}
