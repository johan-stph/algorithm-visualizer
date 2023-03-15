package edu.kit.algorithms.api.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BaseController {

    @GetMapping
    public ResponseEntity<String> alive() {
        return ResponseEntity.ok("I'm alive!");
    }

    @GetMapping("/test")
    public ResponseEntity<String> unprotectedRoute() {
        return ResponseEntity.ok("I'm alive!");
    }
}
