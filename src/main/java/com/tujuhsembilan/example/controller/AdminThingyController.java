package com.tujuhsembilan.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/admin")
public class AdminThingyController {

  @PreAuthorize("hasAuthority('SYSTEM') or hasAuthority('ADMIN')")
  @PostMapping("/do-something")
  public ResponseEntity<?> doSomething() {
    return ResponseEntity.ok().build();
  }

}
