package com.example.normalizer.model.input;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProviderBetaRequest {
    @JsonAlias("event_id")
    private String eventId;
    @JsonAlias("type")
    private String msgType;
    @JsonAlias("odds")
    private ProviderBetaOdds odds;
    @JsonAlias("result")
    private String result;

    public ProviderBetaRequest() {}

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

    public ProviderBetaOdds getOdds() {
        return odds;
    }

    public void setOdds(ProviderBetaOdds odds) {
        this.odds = odds;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ProviderBetaRequest{" +
                "eventId='" + eventId + '\'' +
                ", msgType='" + msgType + '\'' +
                ", odds=" + odds +
                ", result='" + result + '\'' +
                '}';
    }

}
