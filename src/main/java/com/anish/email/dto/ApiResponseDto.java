package com.anish.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API response wrapper")
public record ApiResponseDto(

        @Schema(description = "HTTP status code of the response", example = "202")
        int statusCode,

        @Schema(description = "HTTP status message", example = "Accepted")
        String statusMessage,

        @Schema(description = "Custom message describing the result", example = "Notification queued successfully")
        String message,

        @Schema(description = "Payload of the response, can be any type", example = "true")
        Object data
) { }