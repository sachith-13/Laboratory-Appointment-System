package com.djamware.springsecuritymongo.services;

import com.djamware.springsecuritymongo.domain.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }


    public void sendBookingConfirmationEmail(String userEmail, String bookingDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Booking Confirmation");
        message.setText("Dear User,\n\nYour booking has been successfully added.\nDetails:\n" + bookingDetails);

        try {
            emailSender.send(message);
            logger.info("Email sent successfully to: {}", userEmail);
        } catch (MailException ex) {
            logger.error("Error occurred while sending email to {}: {}", userEmail, ex.getMessage());
            throw new RuntimeException("Could not send email. Please try again later.", ex);
        }
    }


    public void sendBookingApprovalNotification(String userEmail, String bookingDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Booking Approved");
        message.setText("Dear User,\n\nYour booking has been approved. \nDetails: \n" + bookingDetails);

        try {
            emailSender.send(message);
            logger.info("Email sent successfully to: {}", userEmail);
        } catch (MailException ex) {
            logger.error("Error occurred while sending email to {}: {}", userEmail, ex.getMessage());
            throw new RuntimeException("Could not send email. Please try again later.", ex);
        }
    }


    public void sendBookingCancelNotification(String userEmail, String bookingDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Booking Approved");
        message.setText("Dear User,\n\nYour booking has been Cancelled. \nDetails: \n" + bookingDetails);

        try {
            emailSender.send(message);
            logger.info("Email sent successfully to: {}", userEmail);
        } catch (MailException ex) {
            logger.error("Error occurred while sending email to {}: {}", userEmail, ex.getMessage());
            throw new RuntimeException("Could not send email. Please try again later.", ex);
        }
    }


    public void sendPaymentNotification(String userEmail, String bookingDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Payment Approved");
        message.setText("Dear User,\n\nYour Payment has been successfully. \nDetails: \n" + bookingDetails);

        try {
            emailSender.send(message);
            logger.info("Email sent successfully to: {}", userEmail);
        } catch (MailException ex) {
            logger.error("Error occurred while sending email to {}: {}", userEmail, ex.getMessage());
            throw new RuntimeException("Could not send email. Please try again later.", ex);
        }
    }
}
