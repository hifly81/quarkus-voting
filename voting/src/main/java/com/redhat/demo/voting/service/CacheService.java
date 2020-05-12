package com.redhat.demo.voting.service;

import com.redhat.demo.voting.cache.DefaultCache;
import com.redhat.demo.voting.model.Poll;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CacheService {

    @Inject
    @DefaultCache
    Cache<Long, Poll> cache;

    public Long putInCache(Poll poll) {
        cache.put(poll.getId(), poll);
        return poll.getId();
    }

    public Poll getEntry(Long id){
        Poll poll = cache.get(id);
        if (poll == null)
            return new Poll();
        return poll;
    }

}
