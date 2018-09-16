package com.am.study.zuul.filter.route.weight;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "weight.color.filter")
public class WeightColorConfig {

    private boolean enabled;
    private int order = 1;

    private List<WeightRule> rules;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<WeightRule> getRules() {
        return rules;
    }

    public void setRules(List<WeightRule> rules) {
        this.rules = rules;
    }
}