package com.example.normalizer.model.odds_change;

public class OddsChange {
    private String eventId;
    private OddsChangeOdds odds;

    public OddsChange() {}

    public OddsChange(String eventId, OddsChangeOdds odds) {
        this.eventId = eventId;
        this.odds = odds;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public OddsChangeOdds getOdds() {
        return odds;
    }

    public void setOdds(OddsChangeOdds odds) {
        this.odds = odds;
    }

    @Override
    public String toString() {
        return "OddsChange{" +
                "eventId='" + eventId + '\'' +
                ", odds=" + odds +
                '}';
    }
}
