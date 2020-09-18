package org.feixu.geer.model;

/**
 * 器官
 */
public class Organ {
    private String name;
    private int riskLevel;

    public String getName() {
        return name;
    }

    public Organ setName(String name) {
        this.name = name;
        return this;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public Organ setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
        return this;
    }

}
