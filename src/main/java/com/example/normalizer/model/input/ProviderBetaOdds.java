package com.example.normalizer.model.input;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProviderBetaOdds {
    @JsonAlias("home")
    private double homeWin;
    @JsonAlias("draw")
    private double draw;
    @JsonAlias("away")
    private double awayWin;

    public ProviderBetaOdds() {}

    public ProviderBetaOdds(double homeWin, double draw, double awayWin) {
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
        return "ProviderBetaOdds{" +
                "homeWin=" + homeWin +
                ", draw=" + draw +
                ", awayWin=" + awayWin +
                '}';
    }

}
