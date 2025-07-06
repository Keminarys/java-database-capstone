package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final SystemService systemService;

    @Autowired
    public AdminController(SystemService systemService) {
        this.systemService = systemService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody Admin admin) {
        ResponseEntity<?> response = systemService.validateAdmin(admin.getUsername(), admin.getPassword());

        Map<String, Object> result = new HashMap<>();
        if (response.getStatusCode().is2xxSuccessful()) {
            result.put("status", "success");
            result.put("token", response.getBody());
            return ResponseEntity.ok(result);
        } else {
            result.put("status", "error");
            result.put("message", response.getBody());
            return ResponseEntity.status(response.getStatusCode()).body(result);
        }
    }
}
