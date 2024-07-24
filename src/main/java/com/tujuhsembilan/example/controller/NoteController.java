package com.tujuhsembilan.example.controller;

import java.security.Principal;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.example.controller.dto.NoteDto;
import com.tujuhsembilan.example.model.Note;
import com.tujuhsembilan.example.repository.NoteRepo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

  private final NoteRepo repo;

  private final ModelMapper mdlMap;

  private boolean isAdmin(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(role -> role.equals("ADMIN") || role.equals("SYSTEM"));
  }

  @GetMapping
  public ResponseEntity<?> getNotes(Principal principal, Authentication authentication) {
    if (isAdmin(authentication)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins are not allowed to access.");
    }
    String username = principal.getName();
    return ResponseEntity
        .ok(repo.findByUsername(username)
            .stream()
            .map(o -> mdlMap.map(o, NoteDto.class))
            .collect(Collectors.toSet()));
  }

  @PostMapping
  public ResponseEntity<?> saveNote(@RequestBody NoteDto body, Principal principal, Authentication authentication) {
    if (isAdmin(authentication)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins are not allowed to access.");
    }
    var newNote = mdlMap.map(body, Note.class);
    newNote.setUsername(principal.getName());
    newNote = repo.save(newNote);
    return ResponseEntity.status(HttpStatus.CREATED).body(newNote);
  }

}
