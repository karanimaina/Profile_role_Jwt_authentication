package com.jwt.jwtreaciveimpl.model.service;

import com.jwt.jwtreaciveimpl.records.*;
import reactor.core.publisher.Mono;

public interface RoleProfileService {
    Mono<UniversalResponse> addProfile(ProfileData profilesWrapper);

    Mono<UniversalResponse>  addRole(RoleData rolesWrapper);

    Mono<UniversalResponse>  addRolesInProfile(RolesInProfile_data rolesInProfileWrapper);


    Mono<UniversalResponse>  getProfiles(Integer page, Integer size);

    Mono<UniversalResponse>  getRolesInProfiles(Long profileId);

    Mono<UniversalResponse>  getRoles(Integer page, Integer size);

    Mono<UniversalResponse>  activateDeactivateProfile(Long profileId, boolean activated);

    Mono<UniversalResponse>  editProfile(ProfileData profilesWrapper);

    Mono<UniversalResponse>  activateDeactivateRole(Long roleId, boolean activated);

    Mono<UniversalResponse>  editRole(RoleData rolesWrapper);

    Mono<UniversalResponse>  removeRoleInProfile(RolesInProfile_data rolesInProfileWrapper);

    Mono<UniversalResponse>  addProfileUser(ProfileUserWrapper profileUserWrapper);

    Mono<UniversalResponse>  getUserRoles(String currentUser);

    Mono<UniversalResponse>  removeUserProfile(ProfileUserWrapper profileUserWrapper);


    Mono<UniversalResponse>  usersByProfile(Long profileId);

    Mono<UniversalResponse> userProfiles(String currentUser);

    Mono<UniversalResponse>  addUsersToProfile(ProfileUserWrapper profileUserWrapper);
}

