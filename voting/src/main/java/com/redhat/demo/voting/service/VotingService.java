package com.redhat.demo.voting.service;

import com.redhat.demo.voting.messaging.Sender;
import com.redhat.demo.voting.rest.Result;
import com.redhat.demo.voting.rest.Vote;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.*;

@ApplicationScoped
public class VotingService {

    private static Map<Integer, String> options = new HashMap<Integer, String>() {
        {
            put(0, "Kafka on OpenShift: AMQ Streams");
            put(1, "Reactive applications with Kafka");
            put(2, "AMQ Streams http/mqtt bridge");
            put(3, "Streams API: use cases");
            put(4, "Kafka connectors: use cases");
            put(5, "Kafka and debezium: use cases");
            put(6, "Kafka and other Red Hat mw products");
            put(7, "Kafka monitoring and Admin");
        }
    };

    @Inject
    EntityManager entityManager;

    @Inject
    Sender sender;


    public List<Result> getResults() {
        List<Object[]> results =
                entityManager.createQuery("SELECT v.pollId, v.option, COUNT(v) AS total FROM Vote v GROUP BY v.pollId,v.option").getResultList();
        List<Result> returnList = new ArrayList<>();
        for (Object[] result : results) {
            Long pollId = ((Number) result[0]).longValue();
            int option = ((Number) result[1]).intValue();
            int count = ((Number) result[2]).intValue();
            Result temp = new Result();
            temp.setOption(option);
            temp.setPollId(pollId);
            temp.setTotal(count);
            temp.setDescription(options.get(option));
            returnList.add(temp);
        }
        return returnList;
    }

    public Vote addVote(Vote vote) {
        vote.setPollId(123l);
        vote.setId(UUID.randomUUID().toString());
        vote.setDescription(options.get(vote.getOption()));
        sender.add(vote);
        return vote;
    }
}
