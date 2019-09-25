package com.redhat.demo.voting.rest;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Result {

    private Long pollId;
    private Integer option;
    private Integer total;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
