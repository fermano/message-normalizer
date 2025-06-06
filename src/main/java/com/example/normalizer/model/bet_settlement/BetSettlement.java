package com.example.normalizer.model.bet_settlement;

public class BetSettlement {
    private String eventId;
    private String matchResult; // "home", "draw", or "away"

    public BetSettlement(String eventId, String matchResult) {
        this.eventId = eventId;
        this.matchResult = matchResult;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    @Override
    public String toString() {
        return "BetSettlement{" +
                "eventId='" + eventId + '\'' +
                ", matchResult='" + matchResult + '\'' +
                '}';
    }
}
