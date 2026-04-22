package com.anish.email.dto;

//public record AttachmentDto(
//        String fileName,
//        byte[] content
//) { }

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttachmentDto(

        @NotBlank(message = "File name cannot be empty")
        String fileName,

        @NotNull(message = "File content cannot be null")
        @Size(min = 1, message = "File cannot be empty")
        byte[] content
) {}