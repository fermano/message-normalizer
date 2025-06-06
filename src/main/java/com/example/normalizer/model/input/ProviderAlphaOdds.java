package com.example.normalizer.model.input;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProviderAlphaOdds {
    @JsonAlias("1")
    private double homeWin;
    @JsonAlias("X")
    private double draw;
    @JsonAlias("2")
    private double awayWin;

    public ProviderAlphaOdds() {}

    public ProviderAlphaOdds(double homeWin, double draw, double awayWin) {
        this.homeWin = homeWin;
        this.draw = draw;
        this.awayWin = awayWin;
    }

    public double getHomeWin() {
        return homeWin;
    }

    public void setHomeWin(double homeWin) {
        this.homeWin = homeWin;
    }

    public double getDraw() {
        return draw;
    }

    public void setDraw(double draw) {
        this.draw = draw;
    }

    public double getAwayWin() {
        return awayWin;
    }

    public void setAwayWin(double awayWin) {
        this.awayWin = awayWin;
    }

    @Override
    public String toString() {
        return "ProviderAlphaOdds{" +
                "homeWin=" + homeWin +
                ", draw=" + draw +
                ", awayWin=" + awayWin +
                '}';
    }
}
