package com.anish.email.service.validator;

import com.anish.email.dto.AttachmentDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class AttachmentValidator {
    public void validate(@Valid List<AttachmentDto> attachments) {}
}