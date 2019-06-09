package com.cadd.aem.core.workflow;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Defines additional keys that are available for templates when using the
 * SendCADDEmailProcess
 */
@ProviderType
public final class SendTemplatedEmailConstants {

    private SendTemplatedEmailConstants() {

    }

    /**
     * absolute URL string to the payload on the author environment includes the
     * editor extension, i.e 'cf#' or 'editor.html' for pages 'damadmin#' or
     * 'assetdetails.html' for assets To be used in the template as:
     * <code>${authorLink}</code>
     */
    public static final String AUTHOR_LINK = "authorLink";

    /**
     * absolute URL link to the payload on publish. To be used in the template
     * as: <code>${publishLink}</code>
     */
    public static final String PUBLISH_LINK = "publishLink";

    /**
     * the payload path To be used in the template as: <code>${jcr:Path}</code>
     */
    public static final String JCR_PATH = "jcr:Path";

    /**
     * the title of the current workflow model To be used in the template as:
     * <code>${wfModelTitle}</code>
     */
    public static final String WF_MODEL_TITLE = "wfModelTitle";

    /**
     * the title of the current step in the workflow To be used in the template
     * as: <code>${wfStepTitle}</code>
     */
    public static final String WF_STEP_TITLE = "wfStepTitle";

    /**
     * name of the workflow initiator To be used in the template
     * as: <code>${wfInitiator}</code>
     */
    public static final String WF_INITIATOR = "wfInitiator";
}
