package com.ptitB22CN539.QuizRemake.Utils;

import com.nimbusds.jose.util.StandardCharset;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailUtils {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String template, Map<String, Object> properties) {
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, StandardCharset.UTF_8.name());
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            Context context = new Context();
            context.setVariables(properties);
            String htmlFileName = this.templateEngine.process(template, context);
            mimeMessageHelper.setText(htmlFileName, true);
            this.mailSender.send(mimeMessage);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
