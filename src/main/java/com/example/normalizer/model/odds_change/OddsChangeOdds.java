package com.example.normalizer.model.odds_change;


public class OddsChangeOdds {
    private double homeWin;
    private double draw;
    private double awayWin;

    public OddsChangeOdds() {}

    public OddsChangeOdds(double homeWin, double draw, double awayWin) {
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

    public String toString() {
        return "OddsChangeOdds{" +
                "homeWin=" + homeWin +
                ", draw=" + draw +
                ", awayWin=" + awayWin +
                '}';
    }
}
