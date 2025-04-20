package com.example.piproject.services;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;
import com.mailersend.sdk.emails.Email;
import org.springframework.stereotype.Service;

@Service
public class MailerSendService {

    private final MailerSend mailerSend;

    public MailerSendService() {
        this.mailerSend = new MailerSend();
        this.mailerSend.setToken("mlsn.5933c011790134d3f4c640b700caa719bc5f7ae43adcef6c559227f3ac509195"); // 🔒 Replace with real token
    }

    public void sendInterviewConfirmation(String toEmail, String toName, String[] slots) {
        Email email = new Email();

        // 💌 Sender (must be verified in MailerSend) WORKS B** YAY
        email.setFrom("WellU", "noreply@test-68zxl277pye4j905.mlsender.net");

        // 📩 Recipient
        email.addRecipient(toName, toEmail);

        // 📝 Subject and Body
        email.setSubject("Interview Slot Confirmation");

        String slotList = String.join("<br>", slots);
        email.setHtml("<p>Hello " + toName + ",</p>" +
                "<p>Your interview slot is confirmed for:</p>" +
                "<strong>" + slotList + "</strong>" +
                "<p>Best of luck! ✨</p>");

        email.setPlain("Hello " + toName + ",\nYour interview slot is confirmed for:\n" + String.join(", ", slots) + "\n\nBest of luck!");

        try {
            MailerSendResponse response = mailerSend.emails().send(email);
            System.out.println("✅ Email sent! ID: " + response.messageId);
        } catch (MailerSendException e) {
            System.out.println("❌ MailerSend error: " + e.getMessage());
            System.out.println();
        }
    }
}
