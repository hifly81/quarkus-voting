package com.redhat.demo.voting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "Vote")
@Table(name = "Vote")
public class VoteEntity implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(nullable = false)
    private Long pollId;
    @Column(nullable = false)
    private Integer option;
    @Column(nullable = false)
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
