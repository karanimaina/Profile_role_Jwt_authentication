package com.jwt.jwtreaciveimpl.records;

import java.util.List;

public record RolesInProfile_data (
     List<Long> roleIds,
     Long profileId,
     Boolean isActive,
     String remarks){
}
