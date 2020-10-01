package org.feixu.geer.model;

import java.util.List;

/**
 * 癌症相关基因突变
 */
public class CancerRelatedGeneMutations {
    private String name;
    List<String> organs;
    private Boolean result;

    public String getName() {
        return name;
    }

    public CancerRelatedGeneMutations setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getOrgans() {
        return organs;
    }

    public CancerRelatedGeneMutations setOrgans(List<String> organs) {
        this.organs = organs;
        return this;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
