package com.redhat.demo.voting;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes StartupEvent ev) {               
        LOGGER.info("The application is starting...");
    }

    void onStop(@Observes ShutdownEvent ev) {               
        LOGGER.info("The application is stopping...");
    }

}