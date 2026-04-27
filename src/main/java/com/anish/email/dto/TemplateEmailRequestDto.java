package com.anish.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

@Schema(description = "Request dto for sending template email notifications")
public record TemplateEmailRequestDto(

        @Schema(description = "List of emails to send the notifications",
        example = "[\"user1@gmail.com\", \"user2@gmail.com\"]")
        @NotEmpty
        List<@NotBlank @Email String> emails,

        @Schema(description = "Subject of the email", example = "New post has been published")
        @NotBlank
        String subject,

        @Schema(
                description = "Dynamic key-value pairs used to populate the email template",
                type = "object"
        )
        @NotEmpty
        Map<@NotBlank String, @NotNull Object> templateContent
) { }
