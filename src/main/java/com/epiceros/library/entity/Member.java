package com.epiceros.library.entity;

import java.time.LocalDate;

public class Member {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate joinedDate;
    private Long activeStatusId;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public Long getActiveStatusId() {
        return activeStatusId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    public void setActiveStatusId(Long activeStatusId) {
        this.activeStatusId = activeStatusId;
    }
}