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

    public static String getRiskDesc(int riskLevel) {
        if (riskLevel <= 5) {
            return "阴性";
        } else if (riskLevel <= 10) {
            return "注意";
        } else if (riskLevel <= 15) {
            return "警告";
        } else {
            return "阳性";
        }
    }
}
