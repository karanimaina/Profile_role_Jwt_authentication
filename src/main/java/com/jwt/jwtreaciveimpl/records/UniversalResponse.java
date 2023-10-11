package com.jwt.jwtreaciveimpl.records;

import lombok.Builder;

@Builder
public record UniversalResponse(int status, String message, Object data) {
}
