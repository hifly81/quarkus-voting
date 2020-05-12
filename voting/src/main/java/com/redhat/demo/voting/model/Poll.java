package com.redhat.demo.voting.model;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.List;
import java.util.Objects;

public class Poll {

    @ProtoField(number = 1)
    Long id;
    @ProtoField(number = 2)
    String description;
    @ProtoField(number = 3)
    List<PollOption> options;

    public Poll() {}

    @ProtoFactory
    public Poll(Long id, String description, List<PollOption> options) {
        this.id = id;
        this.description = description;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return id.equals(poll.id) &&
                description.equals(poll.description) &&
                options.equals(poll.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, options);
    }
}
