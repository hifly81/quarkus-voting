package com.redhat.demo.voting.messaging;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.demo.voting.model.VoteEntity;
import io.smallrye.reactive.messaging.annotations.Merge;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class Consumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @Inject
    EntityManager entityManager;

    @Incoming("voting")
    @Merge
    @Transactional
    public CompletionStage<Void> onMessage(KafkaRecord<String, String> message) throws IOException {
        try {
            JsonNode json = objectMapper.readTree(message.getPayload());
            Long pollId = json.get("pollId").asLong();
            Integer option = json.get("option").asInt();
            String voteId = json.get("id").asText();
            String description = json.get("description").asText();

            //Store the vote
            LOGGER.info("Received message from kafka with the message: " + json);

            VoteEntity entity = new VoteEntity();
            entity.setId(voteId);
            entity.setOption(option);
            entity.setDescription(description);
            entity.setPollId(pollId);

            entityManager.persist(entity);
            entityManager.flush();


        } catch (Throwable t) {
            t.printStackTrace();
        }
        return message.ack();
    }
}
