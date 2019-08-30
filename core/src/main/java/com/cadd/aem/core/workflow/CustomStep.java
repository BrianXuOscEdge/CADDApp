package com.cadd.aem.core.workflow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.jcr.Session;
import javax.jcr.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;
import javax.jcr.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import com.day.cq.dam.api.Asset;
import java.util.Collections;

import org.apache.jackrabbit.commons.JcrUtils;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import javax.jcr.Session;
import javax.jcr.Node;
import org.osgi.framework.Constants;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

//AssetManager
import com.day.cq.dam.api.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream ;
import java.io.OutputStream ;
import java.io.ByteArrayInputStream ;
import java.io.FileOutputStream ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//MessageServiceGateway API
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory ;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;
import com.day.cq.wcm.api.Page;


import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.workflow.model.WorkflowNode;
import com.adobe.granite.workflow.exec.WorkflowData;




//This custom workflow step will use the AEM Replication API to replicate content from Author to Publish
@Component(service=WorkflowProcess.class, property = {"process.label=My Email Custom Step"})

public class CustomStep implements WorkflowProcess
{


    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ResourceResolverFactory resolverFactory;

    //Inject a MessageGatewayService
    @Reference
    private MessageGatewayService messageGatewayService;

    private Session session;

    public void execute(WorkItem item, WorkflowSession wfsession,MetaDataMap args) throws WorkflowException {

        try
        {
            log.info("Here in execute method");    //ensure that the execute method is invoked

            //Declare a MessageGateway service
            MessageGateway<Email> messageGateway;

            //Set up the Email message
            Email email = new SimpleEmail();

            //Set the mail values
            String emailToRecipients = "brian.xu@oscedge.com";
            String emailCcRecipients = "brianxucst@gmail.com";

            email.addTo(emailToRecipients);
        //    email.addCc(emailCcRecipients);
            email.setSubject("AEM Custom Step");
            email.setFrom("scottm@adobe.com");
            email.setMsg("This message is to inform you that the AEM content has been deleted");

            //Inject a MessageGateway Service and send the message
            messageGateway = messageGatewayService.getGateway(Email.class);

            // Check the logs to see that messageGateway is not null
            messageGateway.send((Email) email);
            log.info("email sent.");
        }

        catch (Exception e)
        {
            e.printStackTrace()  ;
        }
    }

}
