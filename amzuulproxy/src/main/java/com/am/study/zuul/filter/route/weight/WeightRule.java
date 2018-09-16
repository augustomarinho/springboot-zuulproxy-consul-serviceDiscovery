package com.am.study.zuul.filter.route.weight;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeightRule {

    private String regexPath;
    private List<ServiceWeight> weights;

    public String getRegexPath() {
        return regexPath;
    }

    public void setRegexPath(String regexPath) {
        this.regexPath = regexPath;
    }

    public List<ServiceWeight> getWeights() {
        return weights;
    }

    public void setWeights(List<ServiceWeight> weights) {
        this.weights = weights;
    }

    public boolean matchPath(String path) {
        Pattern regexPathPattern = Pattern.compile(regexPath);
        Matcher matcher = regexPathPattern.matcher(path);
        return matcher.matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeightRule rule = (WeightRule) o;

        return regexPath.equals(rule.regexPath);
    }

    @Override
    public int hashCode() {
        return regexPath.hashCode();
    }

    public int totalWeight() {

        if (weights == null || weights.isEmpty()) {
            return 100;
        }

        return weights.stream().mapToInt(w -> w.getWeight()).sum();
    }
}