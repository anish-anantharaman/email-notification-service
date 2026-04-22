package com.anish.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Request dto for sending email notifications")
public record EmailRequestDto(

        @Schema(description = "List of emails to send the notifications",
                example = "[\"user1@gmail.com\", \"user2@gmail.com\"]")
        @NotEmpty
        List<@NotBlank @Email String> emails,

        @Schema(description = "Subject of the email", example = "Server Down")
        @NotBlank
        String subject,

        @Schema(description = "Content of the email", example = "The production server is down since 2 PM")
        @NotBlank
        String content
) { }