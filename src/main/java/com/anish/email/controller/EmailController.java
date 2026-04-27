package com.anish.email.controller;

import com.anish.email.annotation.AcceptedResponse;
import com.anish.email.annotation.CommonErrorResponses;
import com.anish.email.dto.ApiResponseDto;
import com.anish.email.dto.AttachmentDto;
import com.anish.email.dto.EmailRequestDto;
import com.anish.email.dto.TemplateEmailRequestDto;
import com.anish.email.exception.AttachmentException;
import com.anish.email.service.EmailService;
import com.anish.email.service.validator.AttachmentValidator;
import com.anish.email.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/emails")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Email Notifications", description = "APIs for sending email notifications")
@Validated
public class EmailController {

    private final EmailService emailService;

    private final AttachmentValidator attachmentValidator;

    @CommonErrorResponses
    @AcceptedResponse
    @Operation(
            summary = "Send plain email",
            description = "Queues a plain email for asynchronous sending to specified recipients"
    )
    @PostMapping(path = "/plain", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendEmail(
            @Schema(description = "Email request with recipients, subject, and content")
            @Valid @RequestBody EmailRequestDto emailRequestDto) {

        emailService.sendEmail(emailRequestDto);
        ApiResponseDto response = new ApiResponseDto(HttpStatus.ACCEPTED.value(),
                HttpStatus.ACCEPTED.getReasonPhrase(),
                Constants.CommonConstants.EMAIL_RESPONSE_MESSAGE,
                Boolean.TRUE);
        return ResponseEntity.accepted().body(response);
    }


    @CommonErrorResponses
    @AcceptedResponse
    @Operation(
            summary = "Send plain email with attachments",
            description = "Queues a plain email with attachment(s) for asynchronous sending to specified recipients"
    )
    @PostMapping(path = "/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendEmailWithAttachments(
            @Parameter(
                    description = "Email request JSON",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmailRequestDto.class)
                    )
            )
            @Valid @RequestPart("emailRequest")
            EmailRequestDto emailRequestDto,

            @Parameter(
                    description = "Files to upload",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))
                    )
            )
            @RequestPart("attachments")
            @NotEmpty(message = "At least one attachment is required")
            List<MultipartFile> attachments) {

        List<AttachmentDto> attachmentDtos = attachments.stream()
                        .map(file -> {
                            try {
                                return new AttachmentDto(file.getOriginalFilename(),
                                        file.getBytes());
                            } catch (IOException e) {
                                log.error("Error in processing the attachment: {}", e.getMessage(), e);
                                throw new AttachmentException("Attachment processing error");
                            }
                        })
                .toList();

        attachmentValidator.validate(attachmentDtos);
        emailService.sendEmailWithAttachments(emailRequestDto, attachmentDtos);
        ApiResponseDto response = new ApiResponseDto(HttpStatus.ACCEPTED.value(),
                HttpStatus.ACCEPTED.getReasonPhrase(),
                Constants.CommonConstants.EMAIL_RESPONSE_MESSAGE,
                Boolean.TRUE);
        return ResponseEntity.accepted().body(response);
    }

    @CommonErrorResponses
    @AcceptedResponse
    @Operation(
            summary = "Send templated email",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                                {
                                                  "emails": ["user1@gmail.com"],
                                                  "subject": "Application Received",
                                                  "templateContent": {
                                                    "imageUrl": "https://www.fico.com/sites/default/files/styles/lg/public/2024-05/AdobeStock_629603644.jpeg.webp?itok=0HuY-TzI",
                                                    "name": "William",
                                                    "message": "We’ve received your U.S. income tax return submission. Our tax professionals are reviewing your information to ensure accuracy and compliance with IRS requirements. If any additional details are needed, we will contact you promptly. You’ll be notified once your return is ready for the next step.",
                                                    "senderName": "Gardner Rich & Co.",
                                                    "year": "2026"
                                                  }
                                                }
                                            """
                            )
                    )
            )
    )
    @PostMapping(path = "/template", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendTemplateEmail(
            @Valid @RequestBody TemplateEmailRequestDto templateEmailRequestDto) {

        emailService.sendTemplateEmail(templateEmailRequestDto);
        ApiResponseDto response = new ApiResponseDto(HttpStatus.ACCEPTED.value(),
                HttpStatus.ACCEPTED.getReasonPhrase(),
                Constants.CommonConstants.EMAIL_RESPONSE_MESSAGE,
                Boolean.TRUE);
        return ResponseEntity.accepted().body(response);
    }
}
