package com.example.journeyease;

public class User {
    String From,To;
    long People;
    public User(){}

    public User(String from, String to, long people) {
        From = from;
        To = to;
        People = people;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public long getPeople() {
        return People;
    }

    public void setPeople(long people) {
        People = people;
    }
}
