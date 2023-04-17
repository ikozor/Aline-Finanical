package com.aline.core.aws.email;

import com.aline.core.aws.config.AWSEmailConfig;
import com.aline.core.config.AppConfig;
import com.aline.core.exception.badgateway.EmailNotSentException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.simpleemailv2.AmazonSimpleEmailServiceV2;
import com.amazonaws.services.simpleemailv2.model.Body;
import com.amazonaws.services.simpleemailv2.model.Content;
import com.amazonaws.services.simpleemailv2.model.Destination;
import com.amazonaws.services.simpleemailv2.model.EmailContent;
import com.amazonaws.services.simpleemailv2.model.Message;
import com.amazonaws.services.simpleemailv2.model.NotFoundException;
import com.amazonaws.services.simpleemailv2.model.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "Email Service")
@RequiredArgsConstructor
@ConditionalOnBean(AWSEmailConfig.class)
public class EmailService {

    private final AppConfig appConfig;
    private final AWSEmailConfig emailConfig;

    private final AmazonSimpleEmailServiceV2 client;
    private final AmazonS3 s3;

    private String fromEmail;

    @PostConstruct
    public void init() {
        fromEmail = appConfig.getEmail().getFrom();
    }

    /**
     * Send a simple test email.
     * @param subject The subject of the email.
     * @param body The plain text message.
     * @param to Email address(es) to send this email to.
     */
    public void sendEmail(String subject, String body, String... to) {
        Content textBody = new Content().withData(body);
        sendEmailHelper(subject, textBody, to);
    }

    /**
     * Send an email with a custom content body.
     * @param subject The subject of the email.
     * @param content The content of the email.
     * @param to The email address(es) to send this email to.
     */
    public void sendEmail(String subject, Content content, String... to) {
        sendEmailHelper(subject, content, to);
    }

    public void sendHtmlEmail(String subject, String templateName, String to, Map<String, String> templateVariables) {
        InputStream templateData = getEmailTemplateInputStream(templateName);
        BufferedReader br = new BufferedReader(new InputStreamReader(templateData));
        String htmlData = interpolateStringVariables(br, templateVariables);
        sendEmail(subject, htmlData, to);
    }

    private void sendEmailHelper(String subject, Content content, String... to) {

        Content emailSubject = new Content().withData(subject);

        Destination destination = new Destination().withToAddresses(to);

        Body emailBody = new Body().withHtml(content);

        Message message = new Message()
                .withSubject(emailSubject)
                .withBody(emailBody);

        EmailContent emailContent = new EmailContent()
                .withSimple(message);

        SendEmailRequest request = new SendEmailRequest()
                .withFromEmailAddress(fromEmail)
                .withDestination(destination)
                .withContent(emailContent);

        log.info("Attempting to send email...");

        try {
            client.sendEmail(request);
            log.info("Email successfully sent to: {}", Arrays.toString(to));
        } catch (SdkClientException | IllegalArgumentException e) {
            log.error("Email could not be sent. Reason: {}", e.getMessage());
            throw new EmailNotSentException();
        }
    }

    /**
     * Retrieves an email template from the AWS S3 Bucket
     * @param templateName The email template name.
     * @return The email template as a string.
     */
    public String getEmailTemplateData(String templateName) {

        String bucketName = emailConfig.getTemplateBucketName();
        try {
            return s3.getObjectAsString(bucketName, templateName);
        } catch (SdkClientException | IllegalArgumentException e) {
            log.error("Template '{}' was not found in bucket '{}'.", templateName, bucketName);
            throw new NotFoundException("Could not find email template to send.");
        }
    }

    /**
     * Retrieves the input stream of the email
     * template from S3.
     * @param templateName The template name.
     * @return An input stream of the template contents.
     */
    public InputStream getEmailTemplateInputStream(String templateName) {
        String bucketName = emailConfig.getTemplateBucketName();
        try {
            return s3.getObject(bucketName, templateName).getObjectContent();
        } catch (SdkClientException | IllegalArgumentException e) {
            log.error("Template '{}' was not found in bucket '{}'.", templateName, bucketName);
            throw new NotFoundException("Could not find email template to send.");
        }
    }

    /**
     * Replaces template variables <em>(formatted as so: ${variable})</em> with
     * the specified string value in a HashMap of string keys and string values.
     * @param template The template to replace variables in.
     * @param variables The HashMap of string keys and string values.
     * @return A template with all the specified variables replaced.
     */
    public String interpolateStringVariables(String template, Map<String, String> variables) {
        String[] atomicTemplate = {template};
        variables.forEach((variable, value) -> {
            atomicTemplate[0] = atomicTemplate[0].replaceAll("\\$\\{" + variable + "}", value);
        });
        return atomicTemplate[0];
    }

    /**
     * Interpolate string variables while reading through
     * the buffered reader data stream.
     * @param bufferedReader The buffered reader to read from.
     * @param variables The Hashmap of string variables/
     * @return A string now with replaced placeholders.
     */
    public String interpolateStringVariables(BufferedReader bufferedReader, Map<String, String> variables) {
        return bufferedReader.lines()
                .map(line -> interpolateVariablesInLine(line, variables))
                .collect(Collectors.joining());
    }

    /**
     * Interpolate variables in a single line.
     * @param line The line to replace placeholders with variables.
     * @param variables The variables to replace the placeholders with.
     * @return A string with replaced placeholders.
     */
    public String interpolateVariablesInLine(String line, Map<String, String> variables) {
        Pattern pattern = Pattern.compile("\\$\\{[A-Za-z0-9]*}");
        Matcher matcher = pattern.matcher(line);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String varPlaceholder = matcher.group();
            Pattern varNamePattern = Pattern.compile("(?<=\\$\\{)[A-Za-z0-9]*(?=})");
            Matcher varNameMatcher = varNamePattern.matcher(varPlaceholder);
            String varName = "";
            if (varNameMatcher.find()) {
                varName = varNameMatcher.group();
            }

            if (variables.containsKey(varName)) {
                String var = variables.get(varName);
                matcher.appendReplacement(sb, var);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
