package com.redhat.demo.voting;

import com.redhat.demo.voting.producer.Sender;
import com.redhat.demo.voting.rest.Result;
import com.redhat.demo.voting.rest.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/voting")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VotingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotingResource.class);

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
    Sender sender;

    @Inject
    EntityManager entityManager;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/results")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Result> results() {
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

    @POST
    public Response add(Vote vote) {
        //TODO
        vote.setPollId(123l);
        vote.setId(UUID.randomUUID().toString());
        vote.setDescription(options.get(vote.getOption()));
        sender.add(vote);
        return Response.ok(vote).build();
    }


}