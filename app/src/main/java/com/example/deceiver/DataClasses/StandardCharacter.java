package com.example.deceiver.DataClasses;

import com.example.deceiver.Enums.StandardRole;
import com.example.deceiver.Enums.StandardTeam;

public class StandardCharacter {
    private StandardRole role;
    private StandardTeam team;
    private boolean isAlive;
    private boolean isProtected;
    private boolean hasSword;
    private boolean isExposed;
    private boolean isHeavilyWounded;
    private boolean isSilenced;
    private boolean isHexed;
    private boolean isVivified;
    private boolean canVivify;
    private int woundCounter;

    public StandardCharacter(StandardRole role) {
        this.role = role;
        if(role==StandardRole.Deceiver||role==StandardRole.Traitor)
            this.team=StandardTeam.Deceivers;
        else
            this.team=StandardTeam.Village;
        this.isAlive=true;
        this.isProtected=false;
        this.hasSword=false;
        this.isExposed=false;
        this.isHeavilyWounded=false;
        this.isSilenced=false;
        this.isHexed=false;
        this.isVivified=false;
        this.canVivify=true;
        this.woundCounter=0;
    }

    public StandardCharacter() {
        this.role = StandardRole.Farmer;
        this.team=StandardTeam.Village;
        this.isAlive=true;
        this.isProtected=false;
        this.hasSword=false;
        this.isExposed=false;
        this.isHeavilyWounded=false;
        this.isSilenced=false;
        this.isHexed=false;
        this.isVivified=false;
        this.canVivify=true;
        this.woundCounter=0;
    }

    public StandardRole getRole() {
        return role;
    }

    public void setRole(StandardRole role) {
        this.role = role;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public boolean isHasSword() {
        return hasSword;
    }

    public void setHasSword(boolean hasSword) {
        this.hasSword = hasSword;
    }

    public boolean isExposed() {
        return isExposed;
    }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
    }

    public boolean isHeavilyWounded() {
        return isHeavilyWounded;
    }

    public void setHeavilyWounded(boolean heavilyWounded) {
        isHeavilyWounded = heavilyWounded;
    }

    public boolean isSilenced() {
        return isSilenced;
    }

    public void setSilenced(boolean silenced) {
        isSilenced = silenced;
    }

    public boolean isHexed() {
        return isHexed;
    }

    public void setHexed(boolean hexed) {
        isHexed = hexed;
    }

    public int getWoundCounter() {
        return woundCounter;
    }

    public void setWoundCounter(int woundCounter) {
        this.woundCounter = woundCounter;
    }

    public boolean isVivified() {  return isVivified;  }

    public void setVivified(boolean vivified) {
        isVivified = vivified;
    }

    public StandardTeam getTeam() {
        return team;
    }

    public void setTeam(StandardTeam team) {
        this.team = team;
    }

    public boolean isCanVivify() {
        return canVivify;
    }

    public void setCanVivify(boolean canVivify) {
        this.canVivify = canVivify;
    }
}
