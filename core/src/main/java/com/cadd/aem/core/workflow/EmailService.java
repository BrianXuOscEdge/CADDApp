package com.cadd.aem.core.workflow;

import org.osgi.annotation.versioning.ProviderType;

import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;
import java.util.List;
import java.util.Map;

/**
 * A service interface for sending a generic template based Email Notification.
 *
 * The mailType is set to HTMLEmail by default if there are any attachments. Since we are using
 * the template to determine the mailType, the template name has to be *.html.
 *
 * Here is an example to send an email with attachments:
 *
 *      String attachment1 = "This text should be in the attache txt file."
 *      Map<String, DataSource> attachments = new HashMap<>();
 *      attachments.put("attachment1.txt", new ByteArrayDataSource(attachment1, "text/plain"));
 *      ...
 *      ...
 *      List<String> participantList = emailService.sendEmail(htmlEmailTemplatePath, emailParams, attachments, key);
 */
@ProviderType
public interface EmailService {

    /**
     * Construct an email based on a template and send it to one or more
     * recipients.
     *
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param recipients recipient email addresses
     *
     * @return failureList containing list recipient's InternetAddresses for which email sent failed
     */
    List<InternetAddress> sendEmail(String templatePath, Map<String, String> emailParams,
                                    InternetAddress... recipients);

    /**
     * Construct an email based on a template and send it to one or more
     * recipients.
     *
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param recipients recipient email addresses. Invalid email addresses are skipped.
     *
     * @return failureList containing list recipient's String addresses for which email sent failed
     */
    List<String> sendEmail(String templatePath, Map<String, String> emailParams, String... recipients);

    /**
     * Construct an email with attachments based on a template and send it to one or more
     * recipients.
     *
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param attachments attachments map with file name
     * @param recipients recipient email addresses
     * @return failureList containing list recipient's String addresses for which email sent failed
     */
    List<InternetAddress> sendEmail(String templatePath, Map<String, String> emailParams, Map<String, DataSource> attachments, InternetAddress... recipients);

    /**
     * Construct an email with attachments based on a template and send it to one or more
     * recipients.
     *
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param attachments attachments map with file name
     * @param recipients recipient email addresses
     * @return failureList containing list recipient's String addresses for which email sent failed
     */
    List<String> sendEmail(String templatePath, Map<String, String> emailParams, Map<String, DataSource> attachments, String... recipients);
}