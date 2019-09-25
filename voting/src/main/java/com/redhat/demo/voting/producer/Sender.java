package com.redhat.demo.voting.producer;

import com.redhat.demo.voting.rest.Vote;
import io.smallrye.reactive.messaging.kafka.KafkaMessage;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingQueue;

@ApplicationScoped

public class Sender {

    @Inject
    Jsonb jsonb;

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    private BlockingQueue<Vote> messages = new LinkedBlockingQueue<>();

    public void add(Vote message) {
        messages.add(message);

    }

    @Outgoing("voting")
    public CompletionStage<KafkaMessage<String, String>> send() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Vote vote = messages.take();
                LOGGER.info("Sending message to kafka with the message: " + vote);
                String json = jsonb.toJson(vote);
                return KafkaMessage.of("voting", vote.getId(), json);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });

    }

}


