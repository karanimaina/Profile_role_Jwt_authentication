package com.jwt.jwtreaciveimpl.service;

import com.jwt.jwtreaciveimpl.records.*;

public interface RoleProfileService {
    UniversalResponse addProfile(Profile_data profilesWrapper);

    UniversalResponse addRole(Role_data rolesWrapper);

    UniversalResponse addRolesInProfile(RolesInProfile_data rolesInProfileWrapper);


    UniversalResponse getProfiles(Integer page, Integer size);

    UniversalResponse getRolesInProfiles(Long profileId);

    UniversalResponse getRoles(Integer page, Integer size);

    UniversalResponse activateDeactivateProfile(Long profileId, boolean activated);

    UniversalResponse editProfile(Profile_data profilesWrapper);

    UniversalResponse activateDeactivateRole(Long roleId, boolean activated);

    UniversalResponse editRole(Role_data rolesWrapper);

    UniversalResponse removeRoleInProfile(RolesInProfile_data rolesInProfileWrapper);

    UniversalResponse addProfileUser(ProfileUserWrapper profileUserWrapper);

    UniversalResponse getUserRoles(String currentUser);

    UniversalResponse removeUserProfile(ProfileUserWrapper profileUserWrapper);


    UniversalResponse usersByProfile(Long profileId);

    UniversalResponse userProfiles(String currentUser);

    UniversalResponse addUsersToProfile(ProfileUserWrapper profileUserWrapper);
}

