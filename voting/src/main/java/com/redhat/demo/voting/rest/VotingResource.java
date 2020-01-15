package com.redhat.demo.voting.rest;

import com.redhat.demo.voting.service.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/voting")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VotingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotingResource.class);

    @Inject
    VotingService votingService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/results")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Result> results() {
        return votingService.getResults();
    }

    @POST
    public Response add(Vote vote) {
        vote = votingService.addVote(vote);
        return Response.ok(vote).build();
    }


}