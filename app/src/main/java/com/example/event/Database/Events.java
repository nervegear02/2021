package com.example.event.Database;

public class Events {
    private String event_name, event_details, event_schedule, event_price;
    private boolean expandable;

    public Events() {
    }

    public Events(String event_name, String event_details, String event_schedule, String event_price, boolean expandable) {
        this.event_name = event_name;
        this.event_details = event_details;
        this.event_schedule = event_schedule;
        this.event_price = event_price;
        this.expandable = expandable;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_details() {
        return event_details;
    }

    public void setEvent_details(String event_details) {
        this.event_details = event_details;
    }

    public String getEvent_schedule() {
        return event_schedule;
    }

    public void setEvent_schedule(String event_schedule) {
        this.event_schedule = event_schedule;
    }

    public String getEvent_price() {
        return event_price;
    }

    public void setEvent_price(String event_price) {
        this.event_price = event_price;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    @Override
    public String toString() {
        return "Events{" +
                "event_name ='" + event_name + '\'' +
                ", event_details ='" + event_details + '\'' +
                ", event_price ='" + event_price + '\'' +
                ", event_schedule ='" + event_schedule + '\'' +
                '}';
    }
}
