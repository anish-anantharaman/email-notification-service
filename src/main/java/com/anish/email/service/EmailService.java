package com.anish.email.service;

import com.anish.email.dto.AttachmentDto;
import com.anish.email.dto.EmailRequestDto;
import com.anish.email.dto.TemplateEmailRequestDto;

import java.util.List;

public interface EmailService {

    void sendEmail(EmailRequestDto emailRequestDto);

    void sendEmailWithAttachments(EmailRequestDto emailRequestDto, List<AttachmentDto> attachments);

    void sendTemplateEmail(TemplateEmailRequestDto templateEmailRequestDto);
}
