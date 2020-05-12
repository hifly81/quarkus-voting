package com.redhat.demo.voting;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.redhat.demo.voting.model.Poll;
import com.redhat.demo.voting.model.PollOption;
import com.redhat.demo.voting.service.CacheService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AppLifecycleBean {

    @Inject
    CacheService cacheService;

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes StartupEvent ev) {

        //FIXME hard coded poll

        Poll poll = new Poll();
        poll.setId(111l);
        poll.setDescription("Kafka poll: bet kafka features?");
        List<PollOption> optionList = new ArrayList<>();

        PollOption pollOption = new PollOption();
        pollOption.setId(0);
        pollOption.setDescription("Kafka on OpenShift: AMQ Streams");

        PollOption pollOption2 = new PollOption();
        pollOption2.setId(1);
        pollOption2.setDescription("Reactive applications with Kafka");

        optionList.add(pollOption);
        optionList.add(pollOption2);
        poll.setOptions(optionList);

        cacheService.putInCache(poll);

        LOGGER.info("The application is starting...");
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

}