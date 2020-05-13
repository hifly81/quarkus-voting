package com.redhat.demo.voting;

import com.redhat.demo.voting.model.Poll;
import com.redhat.demo.voting.service.CacheService;
import org.infinispan.Cache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

    @InjectMocks
    CacheService cacheService;
    @Mock
    Cache<Long, Poll> cache;

    @Test
    void test_put_entry_in_cache() {

        Poll entry = new Poll(1l, "Test Poll", null);
        Long id = cacheService.putInCache(entry);
        assertThat(id).isNotZero();
        assertThat(id).isEqualTo(1l);
    }

    @Test
    void test_get_entry_from_cache() {

        Poll entry = new Poll(1l, "Test Poll", null);
        when(cache.get(entry.getId())).thenReturn(entry);

        Poll result = cacheService.getEntry(entry.getId());
        assertThat(result.getId()).isEqualTo(1l);
    }


}
