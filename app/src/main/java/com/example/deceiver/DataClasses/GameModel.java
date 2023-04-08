package com.example.deceiver.DataClasses;

public class GameModel {
    private String date,result;
    private long bodyCount,dawns,nights,days;

    public GameModel() {
    }

    public GameModel(String date, String result, long bodyCount, long dawns, long nights, long days) {
        this.date = date;
        this.result = result;
        this.bodyCount = bodyCount;
        this.dawns = dawns;
        this.nights = nights;
        this.days = days;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getBodyCount() {
        return bodyCount;
    }

    public void setBodyCount(long bodyCount) {
        this.bodyCount = bodyCount;
    }

    public long getDawns() {
        return dawns;
    }

    public void setDawns(long dawns) {
        this.dawns = dawns;
    }

    public long getNights() {
        return nights;
    }

    public void setNights(long nights) {
        this.nights = nights;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }
}
