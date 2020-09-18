package org.feixu.geer.model;

import java.util.List;

/**
 * 抑癌基因
 */
public class TumorSuppressorGene {
    private String name;
    private List<String> effectOrgans;

    public String getName() {
        return name;
    }

    public TumorSuppressorGene setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getEffectOrgans() {
        return effectOrgans;
    }

    public TumorSuppressorGene setEffectOrgans(List<String> effectOrgans) {
        this.effectOrgans = effectOrgans;
        return this;
    }
}
