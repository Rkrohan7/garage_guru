package com.garage_guru.service;

import com.garage_guru.dto.request.TeamMemberRequest;
import com.garage_guru.dto.response.TeamMemberResponse;

import java.util.List;

public interface TeamMemberService {
    TeamMemberResponse createTeamMember(TeamMemberRequest request);
    TeamMemberResponse getTeamMemberById(Long id);
    List<TeamMemberResponse> getAllTeamMembers();
    List<TeamMemberResponse> getTeamMembersByGarageId(Long garageId);
    TeamMemberResponse updateTeamMember(Long id, TeamMemberRequest request);
    void deleteTeamMember(Long id);
}
