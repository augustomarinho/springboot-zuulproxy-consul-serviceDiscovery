package com.am.study.zuul.filter.route.weight;

public class ServiceWeight {

    private String serviceId;
    private int weight;

    public ServiceWeight() {
    }

    public ServiceWeight(String serviceId, int weight) {
        this.serviceId = serviceId;
        this.weight = weight;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceWeight that = (ServiceWeight) o;

        if (weight != that.weight) return false;
        return serviceId.equals(that.serviceId);
    }

    @Override
    public int hashCode() {
        int result = serviceId.hashCode();
        result = 31 * result + weight;
        return result;
    }

    @Override
    public String toString() {
        return "ServiceWeight{" +
                "serviceId='" + serviceId + '\'' +
                ", weight=" + weight +
                '}';
    }
}