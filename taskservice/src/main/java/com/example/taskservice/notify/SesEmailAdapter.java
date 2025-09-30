package com.example.taskservice.notify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;

@Service
@Profile("aws")
@RequiredArgsConstructor
public class SesEmailAdapter implements EmailPort {
    

        // Configure in application-aws.properties
    @Value("${app.mail.from}")
    private String from;

    // You can inject via config too; hardcode region if you prefer
    private final SesV2Client ses = SesV2Client.builder()
            .region(Region.EU_WEST_2) // London
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    @Override
    public void send(String to, String subject, String htmlBody) {
        SendEmailRequest req = SendEmailRequest.builder()
                .fromEmailAddress(from)
                .destination(d -> d.toAddresses(to))
                .content(c -> c.simple(s -> s
                        .subject(t -> t.data(subject))
                        .body(b -> b.html(h -> h.data(htmlBody)))))
                .build();
        ses.sendEmail(req);
    }
}
