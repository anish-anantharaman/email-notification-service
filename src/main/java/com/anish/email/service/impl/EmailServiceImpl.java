package com.anish.email.service.impl;

import com.anish.email.config.properties.EmailProperties;
import com.anish.email.dto.AttachmentDto;
import com.anish.email.dto.EmailRequestDto;
import com.anish.email.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final EmailProperties emailProperties;

    @Override
    @Async("asyncExecutor")
    public void sendEmail(EmailRequestDto emailRequestDto) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            for (String email : emailRequestDto.emails()) {
                MimeMessageHelper mimeMessageHelper =
                        new MimeMessageHelper(mimeMessage, Boolean.TRUE);
                mimeMessageHelper.setFrom(emailProperties.from(), emailProperties.alias());
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject(emailRequestDto.subject());
                mimeMessageHelper.setText(emailRequestDto.content());
                javaMailSender.send(mimeMessage);
            }
            log.info("Plain email sent successfully");
        } catch (Exception e) {
            log.error("Error sending plain email: {}", e.getMessage(), e);
        }
    }

    @Override
    @Async("asyncExecutor")
    public void sendEmailWithAttachments(
            EmailRequestDto emailRequestDto, List<AttachmentDto> attachments) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            for(String email : emailRequestDto.emails()) {
                MimeMessageHelper mimeMessageHelper =
                        new MimeMessageHelper(mimeMessage, Boolean.TRUE);
                mimeMessageHelper.setFrom(emailProperties.from(), emailProperties.alias());
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject(emailRequestDto.subject());
                mimeMessageHelper.setText(emailRequestDto.content());
                for (AttachmentDto file : attachments) {
                    String fileName = Optional.ofNullable(file.fileName())
                            .filter(s -> !s.isEmpty())
                            .orElse("Attachment");
                    mimeMessageHelper.addAttachment(
                            fileName,
                            new ByteArrayResource(file.content())
                    );
                }
                javaMailSender.send(mimeMessage);
            }
            log.info("Attachment email sent successfully");
        } catch(Exception e) {
            log.error("Error sending email with attachment: {}",
                    e.getMessage(), e);
        }
    }
}
