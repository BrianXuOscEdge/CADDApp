package com.cadd.aem.core.workflow;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Defines special keys for the replacement variable map
 * passed to EmailService.sendEmail().
 */
@ProviderType
public final class EmailServiceConstants {


    private EmailServiceConstants() {

    }

    /**
     * Sender Email Address variable passed in the input parameter
     * map to the sendEmail() function.
     */
    public static final String SENDER_EMAIL_ADDRESS = "senderEmailAddress";

    /**
     * Sender Name variable passed in the input parameter
     * map to the sendEmail() function.
     */
    public static final String SENDER_NAME = "senderName";


    /**
     * Subject line variable used to specify the subject in the input parameter map.
     */
    public static final String SUBJECT = "subject";

    /**
     * Variable used to specify the bounce address. Also referred to as the envelope FROM address.
     */
    public static final String BOUNCE_ADDRESS = "bounceAddress";
}