package com.example.normalizer.model.input;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProviderAlphaRequest {
    @JsonAlias("event_id")
    private String eventId;
    @JsonAlias("msg_type")
    private String msgType;
    @JsonAlias("values")
    private ProviderAlphaOdds odds;
    @JsonAlias("outcome")
    private String outcome;

    public ProviderAlphaRequest() {}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public ProviderAlphaOdds getOdds() {
        return odds;
    }

    public void setOdds(ProviderAlphaOdds odds) {
        this.odds = odds;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        return "ProviderAlphaRequest{" +
                "eventId='" + eventId + '\'' +
                ", msgType='" + msgType + '\'' +
                ", odds='" + odds + '\'' +
                ", outcome='" + outcome + '\'' +
                '}';
    }
}
