package com.am.study.zuul;

import com.am.study.zuul.filter.route.weight.ServiceWeight;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Weight {

    TreeMap<Integer, ServiceWeight> colorPool;
    Random someRandGen = new Random();
    AtomicInteger totalWeight;

    public void startBalancing(ServiceWeight... servers) {

        this.colorPool = new TreeMap<Integer, ServiceWeight>();
        totalWeight = new AtomicInteger(0);
        for (ServiceWeight s : servers) {
            //  associate each server with the sum of the weights so far
            totalWeight.getAndAdd(s.getWeight());
            this.colorPool.put(totalWeight.get(), s);
        }
    }

    public ServiceWeight getNext() {
        Integer rnd = someRandGen.nextInt(this.totalWeight.get());
        return colorPool.ceilingEntry(rnd).getValue();
    }

    public static void main(String[] args) {
        Weight weight = new Weight();

        ServiceWeight blueColor = new ServiceWeight("blue", 0);
        ServiceWeight greenColor = new ServiceWeight("green", 100);

        Map<ServiceWeight, Integer> invokes = new ConcurrentHashMap<ServiceWeight, Integer>();
        invokes.put(blueColor, 0);
        invokes.put(greenColor, 0);

        weight.startBalancing(blueColor, greenColor);

        for (int i = 0; i < 100000; i++) {
            int rnd = weight.someRandGen.nextInt(weight.totalWeight.get()) + 1;

            increment(invokes, weight.colorPool.ceilingEntry(rnd).getValue());
            //System.out.println(weight.colorPool.ceilingEntry(rnd).getValue());
        }

        System.out.println("Blue invokes: " + invokes.get(blueColor).intValue());
        System.out.println("Green invokes: " + invokes.get(greenColor).intValue());
    }

    public static void increment(Map<ServiceWeight, Integer> invokes, ServiceWeight sorted) {

        int currentValue = 0;
        int updatedValue = currentValue;

        if (invokes.containsKey(sorted)) {
            currentValue = invokes.get(sorted);
            updatedValue = currentValue + 1;
        }

        invokes.put(sorted, updatedValue);
    }
}