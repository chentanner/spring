package com.sss.app.core.entity.model;

import java.util.ArrayList;
import java.util.List;

public class ExpiryPolicy {

    private List<ExpireCascadeRule> cascadeRules = new ArrayList<>();
    private boolean deleteOnExpiry = false;

    public ExpiryPolicy() {
    }

    public ExpiryPolicy(boolean deleteOnExpiry) {
        this.deleteOnExpiry = deleteOnExpiry;
    }

    public ExpiryPolicy(boolean deleteOnExpiry, ExpireCascadeRule rule) {
        this.deleteOnExpiry = deleteOnExpiry;
        cascadeRules.add(rule);
    }

    public ExpiryPolicy(boolean deleteOnExpiry, List<ExpireCascadeRule> rules) {
        this.deleteOnExpiry = deleteOnExpiry;
        cascadeRules.addAll(rules);
    }

    public boolean isDeleteOnExpiry() {
        return deleteOnExpiry;
    }

    public void setDeleteOnExpiry(boolean deleteOnExpiry) {
        this.deleteOnExpiry = deleteOnExpiry;
    }

    public void addCascadeRule(ExpireCascadeRule cascadeRule) {
        cascadeRules.add(cascadeRule);
    }

    public List<ExpireCascadeRule> getCascadeRules() {
        return cascadeRules;
    }

    public void setCascadeRules(List<ExpireCascadeRule> cascadeRules) {
        this.cascadeRules = cascadeRules;
    }

    public void cascadeExpire(AbstractEntity abstractEntity) {
        for (int i = 0; i < cascadeRules.size(); i++) {
            ExpireCascadeRule cascadeRule = cascadeRules.get(i);
            cascadeRule.cascadeExpire(abstractEntity);
        }
    }

}