package com.jwt.jwtreaciveimpl.service;

import com.jwt.jwtreaciveimpl.model.User;
import com.jwt.jwtreaciveimpl.model.rbac.Profile;
import com.jwt.jwtreaciveimpl.model.rbac.Roles;
import com.jwt.jwtreaciveimpl.model.rbac.RolesInProfiles;
import com.jwt.jwtreaciveimpl.model.rbac.UsersProfile;
import com.jwt.jwtreaciveimpl.records.*;
import com.jwt.jwtreaciveimpl.repoitory.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoleProfileServiceImpl implements RoleProfileService {
    private final RolesRepository rolesRepository;
    private final ProfilesRepository profilesRepository;
    private final UsersProfilesRepository usersProfilesRepository;
    private final RolesInProfileRepository rolesInProfileRepository;
    private final UserRepository userRepository;
    @Override
    public UniversalResponse addProfile(Profile_data profile) {
        List<Profile> profileNameExists = profilesRepository.findByNameIgnoreCase(profile.name());
        if (profileNameExists.size()>0)
           return  UniversalResponse.builder().status(400).message("Profile with the given name already exist").build();
        Profile profiles = Profile.builder()
                .isActive(true)
                .name(profile.name())
                .remarks(profile.remarks())
                .build();
        profilesRepository.save(profiles);
        return  UniversalResponse.builder()
                .status(200)
                .message("profile added successfully")
                .data(profile.name()).build();
    }

    @Override
    public UniversalResponse addRole(Role_data roles) {
       List<Roles> rolesList = rolesRepository.findByNameIgnoreCase(roles.name());
        if (rolesList.size()>0)
            return  UniversalResponse.builder().status(400).message("Role name exists").build();

        Roles role = Roles.builder()
                .isSystemRole(false)
                .remarks(roles.remarks())
                .isActive(false)
                .name(roles.name())
                .build();
        rolesRepository.save(role);
        return  UniversalResponse.builder().status(200)
                .message("Role added successfully")
                .data(role.getName())
                .build();
    }

    @Override
    public UniversalResponse addRolesInProfile(RolesInProfile_data rolesInProfileWrapper) {
        Optional<Profile> optionalProfiles = profilesRepository.findById(rolesInProfileWrapper.profileId());
        if (!optionalProfiles.isPresent()) {
            return  UniversalResponse.builder().status(400).message("Profile with that id not found.").build();
        }
        List<Long> longList = rolesInProfileWrapper.roleIds();
        List<Long> availableRoleIds = new ArrayList<>();
        List<Long> nonAvailableRoleIds = new ArrayList<>();
        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
        longList.forEach(roleId -> {
            Optional<Roles> optionalRoles = rolesRepository.findById(roleId);
            if (optionalRoles.isPresent()) {
                availableRoleIds.add(roleId);
            } else {
                stringJoiner.add(String.valueOf(roleId));
                nonAvailableRoleIds.add(roleId);
            }
        });
        availableRoleIds.forEach(roleId -> {
            Optional<RolesInProfiles> roleIdAndProfileId = rolesInProfileRepository.findAllByRoleIdAndProfileId(roleId, rolesInProfileWrapper.profileId());

            RolesInProfiles rolesInProfiles;
            if (roleIdAndProfileId.isPresent()) {
                rolesInProfiles = roleIdAndProfileId.get();
            } else {
                rolesInProfiles = new RolesInProfiles();
                rolesInProfiles.setProfileId(rolesInProfileWrapper.profileId());
                rolesInProfiles.setRoleId(roleId);
            }
            rolesInProfiles.setIsActive(true);
            rolesInProfileRepository.save(rolesInProfiles);
        });
        if (!nonAvailableRoleIds.isEmpty()) {
            return  UniversalResponse.builder().status(404).message("Following roles ids " + stringJoiner + " not added because they are not found.").build();
        } else {
            return  UniversalResponse.builder().status(200).message("Role added to profile").build();
        }
    }

    @Override
    public UniversalResponse getProfiles(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Profile> profilesPage = profilesRepository.findAllByIsActiveTrue(pageable);

        return new UniversalResponse(200, "Profiles retrieved successfully", profilesPage);
    }

    @Override
    public UniversalResponse getRolesInProfiles(Long profileId) {
        Optional<Profile> optionalProfiles = profilesRepository.findById(profileId);
        if (!optionalProfiles.isPresent()) {
            return  UniversalResponse.builder().status(404).message("No profile with such id").build();
        }
        List<RolesInProfiles> profiles = rolesInProfileRepository.findAllByProdfileId(profileId);
        List<RolesInProfiles> rolesInProfilesList = profiles;

        List<Roles> rolesInProfiles = new ArrayList<>();
        for (RolesInProfiles inProfiles : rolesInProfilesList) {
            Optional<Roles> optionalRoles = rolesRepository.findById(inProfiles.getRoleId());
            optionalRoles.ifPresent(rolesInProfiles::add);
        }
        List<Roles> rolesList = rolesRepository.findAllByIsActiveTrue();
        List<Roles> rolesAbsentInProfiles = rolesList.stream().filter(roles1 -> !rolesInProfiles.contains(roles1)).collect(Collectors.toList());
        HashMap<String, Object> profileRolesHashMap = new HashMap<>();
        profileRolesHashMap.put("present", rolesInProfiles);
        profileRolesHashMap.put("absent", rolesAbsentInProfiles);
        return  UniversalResponse.builder().status(200).message("Profile roles retrieved successfully").data(profileRolesHashMap).build();
    }

    @Override
    public UniversalResponse getRoles(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Roles> rolesPage = rolesRepository.findAllByIsActiveTrue(pageable);
        return new UniversalResponse(200, "Roles retrieved successfully", rolesPage);
    }



    @Override
    public UniversalResponse activateDeactivateProfile(Long profileId, boolean activated) {
        Optional<Profile> profilesOptional = profilesRepository.findById(profileId);
        if (!profilesOptional.isPresent())
            return  UniversalResponse.builder().status(404).message("No such profile").build();

        Profile profiles = profilesOptional.get();
        profiles.setIsActive(activated);
        profiles.setUpdatedOn(new Date());
        profilesRepository.save(profiles);
        return  UniversalResponse.builder().status(200).message("Profile edited successfully").build();
    }

    @Override
    public UniversalResponse editProfile(Profile_data profilesWrapper) {
        Optional<Profile> profilesOptional = profilesRepository.findById(profilesWrapper.id());
        if (!profilesOptional.isPresent())
           return  UniversalResponse.builder().status(404).message("No such profile").build();


        Profile profiles = profilesOptional.get();
        profiles.setName(profilesWrapper.name());
        profiles.setRemarks(profilesWrapper.remarks());
        profiles.setIsActive(profilesWrapper.isActive());
        profiles.setUpdatedOn(new Date());
        profilesRepository.save(profiles);
        return  UniversalResponse.builder().status(200).message("Profile edited successfully").build();
    }

    @Override
    public UniversalResponse activateDeactivateRole(Long roleId, boolean activated) {
        Optional<Roles> optionalRoles = rolesRepository.findById(roleId);

        if (!optionalRoles.isPresent()) {
            return  UniversalResponse.builder().status(404).message("no role with that id found.").build();
        }

        Roles roles = optionalRoles.get();
        if (roles.getIsSystemRole())
          return  UniversalResponse.builder().status(404).message("cannot edit a system role").build();
        roles.setIsActive(activated);
        rolesRepository.save(roles);
        return  UniversalResponse.builder().status(200).message("role activated successfully").build();
    }

    @Override
    public UniversalResponse editRole(Role_data rolesWrapper) {
        Optional<Roles> optionalRoles = rolesRepository.findById(rolesWrapper.roleId());

        if (!optionalRoles.isPresent()) {
            return  UniversalResponse.builder().status(404).message("no role with that id found.").build();
        }

        Roles roles = optionalRoles.get();
        if (roles.getIsSystemRole())
            return  UniversalResponse.builder().status(400).message("no role with that id found.").build();

        roles.setRemarks(rolesWrapper.remarks());
        roles.setName(rolesWrapper.name());
        roles.setIsActive(rolesWrapper.isActive());
        rolesRepository.save(roles);
        return  UniversalResponse.builder().status(200).message("role edited successfully").build();
    }

    @Override
    public UniversalResponse removeRoleInProfile(RolesInProfile_data rolesInProfileWrapper) {
        List<Long> roleIds = rolesInProfileWrapper.roleIds();
        List<RolesInProfiles> rolesInProfilesList = new ArrayList<>();
        List<Long> nonAvailableIds = new ArrayList<>();
        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
        roleIds.forEach(roleId -> {
            Optional<RolesInProfiles> idAndProfileId = rolesInProfileRepository.findAllByRoleIdAndProfileId(roleId, rolesInProfileWrapper.profileId());

            if (!idAndProfileId.isPresent()) {
                nonAvailableIds.add(roleId);
                stringJoiner.add(String.valueOf(roleId));
            } else {
                rolesInProfilesList.add(idAndProfileId.get());
            }
        });


        rolesInProfilesList.forEach(rolesInProfiles -> {
            rolesInProfiles.setIsActive(false);
            rolesInProfileRepository.save(rolesInProfiles);
        });
        if (!nonAvailableIds.isEmpty()) {
            return  UniversalResponse.builder().status(404).message("some role ids not found "+  stringJoiner).build();

        } else {
            return  UniversalResponse.builder().status(200).message("role removed from profile "+  stringJoiner).build();
        }
    }

    @Override
    public UniversalResponse addProfileUser(ProfileUserWrapper profileUserWrapper) {
        Optional<UsersProfile> andProfileId = usersProfilesRepository.findByUserEmailAndProfileId(profileUserWrapper.userEmail(), profileUserWrapper.profileId());
        if (andProfileId.isPresent()) {
            UsersProfile usersProfile = andProfileId.get();
            usersProfile.setIsActive(true);
            usersProfilesRepository.save(usersProfile);
            return  UniversalResponse.builder().status(200).message("profile added to user").build();
        }
        Optional<Profile> profilesOptional = profilesRepository.findById(profileUserWrapper.profileId());

        if (!profilesOptional.isPresent())
            return  UniversalResponse.builder().status(404).message("no profile with that id found").build();

        UsersProfile usersProfile = new UsersProfile();
        usersProfile.setProfileId(profileUserWrapper.profileId());
        usersProfile.setUserEmail(profileUserWrapper.userEmail());
        usersProfile.setIsActive(true);
        usersProfilesRepository.save(usersProfile);
        return  UniversalResponse.builder().status(200).message("profile added to user").build();
    }

    @Override
    public UniversalResponse getUserRoles(String currentUser) {
        Optional<List<UsersProfile>> userProfileOptional = usersProfilesRepository.findByUserEmailAndIsActiveTrue(currentUser);
        List<Roles> rolesList = new ArrayList<>();

        if (!userProfileOptional.isPresent())
            return null;

        List<UsersProfile> usersProfiles = userProfileOptional.get();
        for (UsersProfile usersProfile : usersProfiles) {
            Optional<Profile> byId = profilesRepository.findById(usersProfile.getProfileId());
            if (!byId.isPresent())
                continue;
            if (!byId.get().getIsActive()) {
                continue;
            }
            List<RolesInProfiles> profiles = rolesInProfileRepository.findAllByProdfileId(usersProfile.getProfileId());
            profiles.forEach(rolesInProfiles -> {
                Optional<Roles> optionalRoles = rolesRepository.findById(rolesInProfiles.getRoleId());
                if (optionalRoles.isPresent()) {
                    if (!rolesList.contains(optionalRoles.get())) {
                        if (optionalRoles.get().getIsActive()) {
                            rolesList.add(optionalRoles.get());
                        }
                    }
                }
            });
        }

        return UniversalResponse.builder()
                .data(rolesList)
                .message("roles for the current user")
                .status(200)
                .build();
    }

    @Override
    public UniversalResponse removeUserProfile(ProfileUserWrapper profileUserWrapper) {
        Optional<UsersProfile> optionalUsersProfile = usersProfilesRepository.findByUserEmailAndProfileId(profileUserWrapper.userEmail(), profileUserWrapper.profileId());
        if (!optionalUsersProfile.isPresent()) {
            return  UniversalResponse.builder().status(200).message("user profile not found").build();
        }

        UsersProfile usersProfile = optionalUsersProfile.get();
        usersProfile.setIsActive(false);
        usersProfilesRepository.save(usersProfile);
        return  UniversalResponse.builder().status(200).message("user profile remove").build();
    }

    @Override
    public UniversalResponse usersByProfile(Long profileId) {
        Optional<List<UsersProfile>> allByProfileId = usersProfilesRepository.findAllByProfileIdAndIsActiveTrue(profileId);

        if (!allByProfileId.isPresent())
            return  UniversalResponse.builder().status(404).message("profile with that profile id not found").build();

        List<User> usersList = new ArrayList<>();
        List<UsersProfile> usersProfileList = allByProfileId.get();
        usersProfileList.forEach(usersProfile -> {
            List<User> allByEmail = userRepository.findAllByEmail(usersProfile.getUserEmail());
            if (allByEmail.size() > 0)
                usersList.add(allByEmail.get(0));
        });

        return new UniversalResponse(200, "users with that profile", usersList);
    }

    @Override
    public UniversalResponse userProfiles(String currentUser) {
        Optional<List<UsersProfile>> usersProfiles = usersProfilesRepository.findByUserEmailAndIsActiveTrue(currentUser);
        if (!usersProfiles.isPresent()) {
            return  UniversalResponse.builder().status(404).message("user not assigned any profile yet").build();
        }

        List<UsersProfile> usersProfileList = usersProfiles.get();
        List<Profile> profilesList = new ArrayList<>();

        usersProfileList.forEach(usersProfile -> {
            Optional<Profile> profiles = profilesRepository.findById(usersProfile.getProfileId());
            profiles.ifPresent(profilesList::add);
        });
        List<Profile> allProfiles = profilesRepository.findAll();
        List<Profile> absentProfiles = allProfiles.stream().filter(profiles -> !profilesList.contains(profiles)).collect(Collectors.toList());
        HashMap<String, Object> profilesHashMap = new HashMap<>();
        profilesHashMap.put("present", profilesList);
        profilesHashMap.put("absent", absentProfiles);

        return new UniversalResponse(200, "user profiles", profilesHashMap);
    }

    @Override
    public UniversalResponse addUsersToProfile(ProfileUserWrapper profileUserWrapper) {
        Optional<Profile> optionalProfiles = profilesRepository.findById(profileUserWrapper.profileId());
        if (!optionalProfiles.isPresent())
            return  UniversalResponse.builder().status(404).message("profile with that id not found").build();

        for (int i = 0; i < profileUserWrapper.emails().size(); i++) {
            String email = profileUserWrapper.emails().get(i);
            Optional<UsersProfile> emailAndProfileId = usersProfilesRepository.findByUserEmailAndProfileId(email, profileUserWrapper.profileId());
            if (!emailAndProfileId.isPresent()) {
                UsersProfile usersProfile = new UsersProfile();
                usersProfile.setUserEmail(email);
                usersProfile.setProfileId(profileUserWrapper.profileId());
                usersProfilesRepository.save(usersProfile);
            }
        }
        return  UniversalResponse.builder().status(200).message("users added to profile").build();
    }
}
