package com.am.study.zuul.filter.route.weight;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WeightRouteFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(WeightRouteFilter.class);

    @Autowired
    private WeightColorConfig colorConfig;

    private Map<WeightRule, TreeMap<Integer, ServiceWeight>> poolMap;
    private Map<WeightRule, Map<ServiceWeight, Integer>> poolMapForStatistics;

    @PostConstruct
    public void initBean() {
        try {
            this.poolMap = new ConcurrentHashMap<WeightRule, TreeMap<Integer, ServiceWeight>>();
            this.poolMapForStatistics = new ConcurrentHashMap<>();
        } catch (Exception e) {
            logger.error("Problems to initialize WeightRouteFilter!");
        }
    }

    private void startBalancing(WeightRule weightRule) {

        logger.debug("Starting balancing list for WeightRule {} ", weightRule);

        TreeMap colorPool = new TreeMap<Integer, ServiceWeight>();
        AtomicInteger totalWeight = new AtomicInteger(0);

        for (ServiceWeight s : weightRule.getWeights()) {
            logger.trace("adding weight color {} for weight rule {}", s, weightRule);
            totalWeight.getAndAdd(s.getWeight());
            colorPool.put(totalWeight.get(), s);
        }

        this.poolMap.remove(weightRule);
        this.poolMap.put(weightRule, colorPool);
    }

    private boolean existBalanceRule(WeightRule weightRule) {
        if (poolMap.containsKey(weightRule)) {
            return true;
        }

        return false;
    }

    public String getNextServiceId(WeightRule weightRule) {
        Random someRandGen = new Random();
        int rnd = someRandGen.nextInt(weightRule.totalWeight()) + 1;

        //if (!existBalanceRule(weightRule)) {
        startBalancing(weightRule);
        //}

        ServiceWeight sorted = poolMap.get(weightRule).ceilingEntry(rnd).getValue();
        incrementStatistics(weightRule, sorted);

        return sorted.getServiceId();
    }

    public void incrementStatistics(WeightRule weightRule, ServiceWeight sorted) {

        int currentValue = 0;
        int updatedValue = currentValue;

        Map<ServiceWeight, Integer> invokes = null;

        if (!poolMapForStatistics.containsKey(weightRule)) {
            invokes = new ConcurrentHashMap<ServiceWeight, Integer>();
            poolMapForStatistics.put(weightRule, invokes);
        }

        invokes = poolMapForStatistics.get(weightRule);
        if (invokes.containsKey(sorted)) {
            currentValue = invokes.get(sorted);
            updatedValue = currentValue + 1;
        }

        invokes.put(sorted, updatedValue);
    }

    @Override
    public Object run() throws ZuulException {
        long start = System.currentTimeMillis();
        long end = 0L;

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String contextURI = (String) ctx.get("requestURI");

        logger.info(String.format("Receiving request %s in %s", request.getMethod(), contextURI));

        try {
            for (WeightRule rule : colorConfig.getRules()) {
                if (rule.matchPath(request.getServletPath())) {
                    String randomServiceId = getNextServiceId(rule);
                    logger.debug("Request MATCH with a Rule. ServiceId: {}", randomServiceId);
                    ctx.set("serviceId", randomServiceId);

                    logger.info(String.format("Weight applied for %s in serviceId: %s", contextURI, randomServiceId));
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(String.format("Problems to apply weight filter for  %s", contextURI), e);
            ctx.setResponseStatusCode(HttpStatus.VARIANT_ALSO_NEGOTIATES.value());
            ctx.setSendZuulResponse(false);
        } finally {
            end = System.currentTimeMillis();
        }

        logger.info(String.format("Filter processing time %s em %s(ms)", contextURI, (end - start)));
        return null;
    }

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return colorConfig != null ? colorConfig.getOrder() : 0;
    }

    @Override
    public boolean shouldFilter() {
        return colorConfig != null ? colorConfig.isEnabled() : false;
    }
}