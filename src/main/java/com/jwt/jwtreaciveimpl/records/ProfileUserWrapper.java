package com.jwt.jwtreaciveimpl.records;

import java.util.List;

public record ProfileUserWrapper(
         String userEmail,
         Long profileId,
        List<String>emails
) {
}
