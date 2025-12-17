package com.garage_guru.service.impl;

import com.garage_guru.dto.request.TeamMemberRequest;
import com.garage_guru.dto.response.TeamMemberResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.entity.TeamMember;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.TeamMemberRepository;
import com.garage_guru.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final GarageRepository garageRepository;

    @Override
    @Transactional
    public TeamMemberResponse createTeamMember(TeamMemberRequest request) {
        // Java 21: Using record accessor methods
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        TeamMember teamMember = TeamMember.builder()
                .name(request.name())
                .position(request.position())
                .address(request.address())
                .pan(request.pan())
                .email(request.email())
                .contactNumber(request.contactNumber())
                .joiningDate(request.joiningDate())
                .garage(garage)
                .build();

        teamMember = teamMemberRepository.save(teamMember);
        return mapToResponse(teamMember);
    }

    @Override
    public TeamMemberResponse getTeamMemberById(Long id) {
        TeamMember teamMember = teamMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));
        return mapToResponse(teamMember);
    }

    @Override
    public List<TeamMemberResponse> getAllTeamMembers() {
        // Java 21: Using toList()
        return teamMemberRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TeamMemberResponse> getTeamMembersByGarageId(Long garageId) {
        // Java 21: Using toList()
        return teamMemberRepository.findByGarageId(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public TeamMemberResponse updateTeamMember(Long id, TeamMemberRequest request) {
        TeamMember teamMember = teamMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));

        // Java 21: Using record accessor methods
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        teamMember.setName(request.name());
        teamMember.setPosition(request.position());
        teamMember.setAddress(request.address());
        teamMember.setPan(request.pan());
        teamMember.setEmail(request.email());
        teamMember.setContactNumber(request.contactNumber());
        teamMember.setJoiningDate(request.joiningDate());
        teamMember.setGarage(garage);

        teamMember = teamMemberRepository.save(teamMember);
        return mapToResponse(teamMember);
    }

    @Override
    @Transactional
    public void deleteTeamMember(Long id) {
        if (!teamMemberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Team member not found with id: " + id);
        }
        teamMemberRepository.deleteById(id);
    }

    private TeamMemberResponse mapToResponse(TeamMember teamMember) {
        // Java 21: Using record constructor
        return new TeamMemberResponse(
                teamMember.getId(),
                teamMember.getName(),
                teamMember.getPosition(),
                teamMember.getAddress(),
                teamMember.getPan(),
                teamMember.getEmail(),
                teamMember.getContactNumber(),
                teamMember.getJoiningDate(),
                teamMember.getGarage().getId(),
                teamMember.getGarage().getName()
        );
    }
}
