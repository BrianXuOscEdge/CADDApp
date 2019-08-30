package com.cadd.aem.core.workflow;

import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ACS AEM Commons - E-mail Service
 * A Generic Email service that sends an email to a given list of recipients.
 */
@Component
@Service
public final class EmailService{

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Reference
    private static MessageGatewayService messageGatewayService;

    @Reference
    private static ResourceResolverFactory resourceResolverFactory;

    public static List<String> sendEmail(final String templatePath,
                                  final Map<String, String> emailParams,
                                  final String... recipients) {

        List<String> failureList = new ArrayList<String>();

        if (recipients == null || recipients.length <= 0) {
            throw new IllegalArgumentException("Invalid Recipients");
        }

        List<InternetAddress> addresses = new ArrayList<InternetAddress>(recipients.length);
        for (String recipient : recipients) {
            try {
                addresses.add(new InternetAddress(recipient));
            } catch (AddressException e) {
                log.warn("Invalid email address {} passed to sendEmail(). Skipping.", recipient);
            }
        }
        InternetAddress[] iAddressRecipients = addresses.toArray(new InternetAddress[addresses.size()]);
        List<InternetAddress> failureInternetAddresses = sendEmail(templatePath, emailParams, iAddressRecipients);

        for (InternetAddress address : failureInternetAddresses) {
            failureList.add(address.toString());
        }

        return failureList;
    }


    public static List<InternetAddress> sendEmail(final String templatePath, final Map<String, String> emailParams,
                                           final InternetAddress... recipients) {

        List<InternetAddress> failureList = new ArrayList<InternetAddress>();

        if (recipients == null || recipients.length <= 0) {
            throw new IllegalArgumentException("Invalid Recipients");
        }

        final MailTemplate mailTemplate = this.getMailTemplate(templatePath);
        final Class<? extends Email> mailType = this.getMailType(templatePath);
        final MessageGateway<Email> messageGateway = messageGatewayService.getGateway(mailType);

        for (final InternetAddress address : recipients) {
            try {
                // Get a new email per recipient to avoid duplicate attachments
                final Email email = getEmail(mailTemplate, mailType, emailParams);
                email.setTo(Collections.singleton(address));
                messageGateway.send(email);
            } catch (Exception e) {
                failureList.add(address);
                log.error("Error sending email to [ " + address + " ]", e);
            }
        }

        return failureList;
    }

    private Email getEmail(final MailTemplate mailTemplate,
                           final Class<? extends Email> mailType,
                           final Map<String, String> params) throws EmailException, MessagingException, IOException {

        final Email email = mailTemplate.getEmail(StrLookup.mapLookup(params), mailType);

        if (params.containsKey(EmailServiceConstants.SENDER_EMAIL_ADDRESS)
                && params.containsKey(EmailServiceConstants.SENDER_NAME)) {

            email.setFrom(
                    params.get(EmailServiceConstants.SENDER_EMAIL_ADDRESS),
                    params.get(EmailServiceConstants.SENDER_NAME));

        } else if (params.containsKey(EmailServiceConstants.SENDER_EMAIL_ADDRESS)) {
            email.setFrom(params.get(EmailServiceConstants.SENDER_EMAIL_ADDRESS));
        }

        return email;
    }

    private Class<? extends Email> getMailType(String templatePath) {
        return templatePath.endsWith(".html") ? HtmlEmail.class : SimpleEmail.class;
    }

    private MailTemplate getMailTemplate(String templatePath) throws IllegalArgumentException {
        MailTemplate mailTemplate = null;
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            mailTemplate = MailTemplate.create(templatePath, resourceResolver.adaptTo(Session.class));

            if (mailTemplate == null) {
                throw new IllegalArgumentException("Mail template path [ "
                        + templatePath + " ] could not resolve to a valid template");
            }
        } catch (LoginException e) {
            log.error("Unable to obtain an administrative resource resolver to get the Mail Template at [ "
                    + templatePath + " ]", e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }

        return mailTemplate;
    }
}