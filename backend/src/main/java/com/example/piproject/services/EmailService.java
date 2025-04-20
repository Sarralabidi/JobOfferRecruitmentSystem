package com.example.piproject.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;


    public void sendSimpleMail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("your-email@example.com"); // can be fake for Mailtrap
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendInterviewInviteWithICS(String toEmail, List<ZonedDateTime> slots) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        //helper.setFrom("wellu@contact.com");
        //helper.setFrom("MS_LGMBJO@test-68zxl277pye4j905.mlsender.net"); was this workin one
        helper.setFrom("Sarra <MS_LGMBJO@test-68zxl277pye4j905.mlsender.net>");

        helper.setTo(toEmail);
        helper.setSubject("🎯 Interview Slot Confirmation - WELLU");

        String emailBody = """
    Hello,

    🎯 We are pleased to confirm your interview schedule!

    🗓️ Please find attached your calendar invitation, where you'll see the proposed slots.

    👉 Kindly add the invite to your Google Calendar, Outlook, or Apple Calendar for easy reminders.

    If you have any questions, feel free to reply to this email!

    Best regards,
    WellU Recruitment Team
    """;

        helper.setText(emailBody);

        // Generate the ICS content
        String icsContent = generateICS(slots);

        InputStreamSource attachment = new ByteArrayResource(icsContent.getBytes(StandardCharsets.UTF_8));
        helper.addAttachment("interview-invite.ics", attachment);

        mailSender.send(message);
    }


    private String generateICS(List<ZonedDateTime> slots) {
        StringBuilder icsBuilder = new StringBuilder();
        icsBuilder.append("BEGIN:VCALENDAR\n")
                .append("VERSION:2.0\n")
                .append("PRODID:-//Your Company//Interview Scheduler//EN\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

        for (ZonedDateTime slot : slots) {
            icsBuilder.append("BEGIN:VEVENT\n")
                    .append("UID:").append(slot.toEpochSecond()).append("@yourapp.com\n")
                    .append("DTSTAMP:").append(ZonedDateTime.now(ZoneId.of("UTC")).format(formatter)).append("\n")
                    .append("DTSTART:").append(slot.withZoneSameInstant(ZoneId.of("UTC")).format(formatter)).append("\n")
                    .append("DTEND:").append(slot.plusMinutes(30).withZoneSameInstant(ZoneId.of("UTC")).format(formatter)).append("\n")
                    .append("SUMMARY:Interview with Our Company\n")
                    .append("DESCRIPTION:Please join on time. Best of luck!\n")
                    .append("LOCATION:Online\n")
                    .append("END:VEVENT\n");
        }

        icsBuilder.append("END:VCALENDAR");

        return icsBuilder.toString();
    }
}
