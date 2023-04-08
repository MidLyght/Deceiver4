package com.example.deceiver.DataClasses;

public class GameModel {
    private String Date,Result;
    private long BodyCount,Dawns,Nights,Days;

    public GameModel() {
    }

    public GameModel(String Date, String Result, long BodyCount, long Dawns, long Nights, long Days) {
        this.Date = Date;
        this.Result = Result;
        this.BodyCount = BodyCount;
        this.Dawns = Dawns;
        this.Nights = Nights;
        this.Days = Days;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public long getBodyCount() {
        return BodyCount;
    }

    public void setBodyCount(long bodyCount) {
        BodyCount = bodyCount;
    }

    public long getDawns() {
        return Dawns;
    }

    public void setDawns(long dawns) {
        Dawns = dawns;
    }

    public long getNights() {
        return Nights;
    }

    public void setNights(long nights) {
        Nights = nights;
    }

    public long getDays() {
        return Days;
    }

    public void setDays(long days) {
        Days = days;
    }
}
