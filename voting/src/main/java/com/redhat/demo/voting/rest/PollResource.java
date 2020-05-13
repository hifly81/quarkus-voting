package com.redhat.demo.voting.rest;

import com.redhat.demo.voting.service.VotingService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/poll")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PollResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollResource.class);

    @Inject
    VotingService votingService;

    @ConfigProperty(name = "greeting.message", defaultValue="hello")
    String greetingMessage;

    @ConfigProperty(name = "greeting.name", defaultValue="voting app")
    String greetingName;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return greetingMessage + " " + greetingName;
    }

    @GET
    @Path("/results")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(250)
    @Fallback(fallbackMethod = "fallbackPolls")
    public List<Result> pollResults() {
        return votingService.getResults();
    }

    @GET
    @Path("/results/{pollId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(250)
    public List<Result> pollResultsById(@PathParam("pollId") Long pollId) {
        return votingService.getResultsByPollId(pollId);
    }

    @POST
    @Path("/vote")
    public Response add(Vote vote) {
        try {
            vote = votingService.addVote(vote);
            return Response.status(OK).entity(vote).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<Result> fallbackPolls() {
        LOGGER.info("Falling back to PollResource#fallbackPolls()");
        Result dummyResult = new Result();
        dummyResult.setPollId(-1l);
        dummyResult.setDescription("Dummy Poll");
        dummyResult.setTotal(0);
        dummyResult.setOption(0);
        return Collections.singletonList(dummyResult);
    }


}