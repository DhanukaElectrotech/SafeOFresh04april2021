package com.dhanuka.morningparcel.events;

/**
  */
public class SearchTextChangedEvent {
    private String text;

    public SearchTextChangedEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
