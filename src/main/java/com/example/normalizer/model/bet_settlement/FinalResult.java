package com.example.normalizer.model.bet_settlement;

public enum FinalResult {
    HOME("home"),
    DRAW("draw"),
    AWAY("away");

    private final String value;

    FinalResult(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
