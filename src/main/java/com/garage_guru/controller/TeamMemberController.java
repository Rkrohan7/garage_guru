package com.garage_guru.controller;

import com.garage_guru.dto.request.TeamMemberRequest;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.dto.response.TeamMemberResponse;
import com.garage_guru.service.TeamMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-members")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamMemberResponse> createTeamMember(@Valid @RequestBody TeamMemberRequest request) {
        TeamMemberResponse response = teamMemberService.createTeamMember(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamMemberResponse> getTeamMemberById(@PathVariable Long id) {
        TeamMemberResponse response = teamMemberService.getTeamMemberById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TeamMemberResponse>> getAllTeamMembers() {
        List<TeamMemberResponse> response = teamMemberService.getAllTeamMembers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembersByGarageId(@PathVariable Long garageId) {
        List<TeamMemberResponse> response = teamMemberService.getTeamMembersByGarageId(garageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamMemberResponse> updateTeamMember(@PathVariable Long id, @Valid @RequestBody TeamMemberRequest request) {
        TeamMemberResponse response = teamMemberService.updateTeamMember(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteTeamMember(@PathVariable Long id) {
        teamMemberService.deleteTeamMember(id);
        return ResponseEntity.ok(MessageResponse.success("Team member deleted successfully"));
    }
}
