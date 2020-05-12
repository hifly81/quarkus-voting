package com.redhat.demo.voting.service;

import com.redhat.demo.voting.messaging.Sender;
import com.redhat.demo.voting.model.Poll;
import com.redhat.demo.voting.rest.Result;
import com.redhat.demo.voting.rest.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.*;


@ApplicationScoped
public class VotingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotingService.class);

    @Inject
    EntityManager entityManager;

    @Inject
    Sender sender;

    @Inject
    CacheService cacheService;


    @Traced(operationName = "VotingService.getResults")
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
            Poll poll = cacheService.getEntry(pollId);
            if(poll != null)
                temp.setDescription(poll.getDescription());
            returnList.add(temp);
        }
        return returnList;
    }

    @Traced(operationName = "VotingService.addVote")
    public Vote addVote(Vote vote) throws Exception {
        LOGGER.info("Adding vote: " + vote);
        Poll poll = cacheService.getEntry(vote.getPollId());
        if(poll == null)
            throw new Exception("Poll not exists!");
        vote.setId(UUID.randomUUID().toString());
        sender.add(vote);
        return vote;
    }
}
