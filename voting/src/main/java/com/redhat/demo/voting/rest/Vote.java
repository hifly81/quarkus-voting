package com.redhat.demo.voting.rest;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Vote {

    private String id;
    private Long pollId;
    private Integer option;
    private String description;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public Integer getOption() {
        return option;
    }

    public void setOption(Integer option) {
        this.option = option;
    }
}
