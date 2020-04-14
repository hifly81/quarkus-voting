package com.redhat.demo.voting.messaging;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.demo.voting.model.VoteEntity;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class Consumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @Inject
    EntityManager entityManager;


    @Transactional
    @Incoming("voting")
    public CompletionStage<?> onMessage(KafkaRecord<String, String> message) {

        /**
         * https://stackoverflow.com/questions/58534957
         */
        ManagedExecutor executor = ManagedExecutor.builder()
                .maxAsync(5)
                .propagated(ThreadContext.CDI,
                        ThreadContext.TRANSACTION)
                .build();
        ThreadContext threadContext = ThreadContext.builder()
                .propagated(ThreadContext.CDI,
                        ThreadContext.TRANSACTION)
                .build();

        return executor.runAsync(threadContext.contextualRunnable(() -> {
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
            } catch(Exception e) {

            } finally {
                message.ack();
            }
        }));
    }

}
