package com.barclays.tradereporter.service;

import com.barclays.tradereporter.model.Product;
import com.barclays.tradereporter.model.Regulator;
import com.barclays.tradereporter.model.Trade;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TradeProducer implements Runnable {
    private static final List<Regulator> REGULATOR_LIST = Collections.unmodifiableList(Arrays.asList(Regulator.values()));

    private Map<Regulator, ConcurrentLinkedQueue<Trade>> regulatorQueueMap;
    private Product product;
    private Integer tradeId = 0;
    private Random random;

    public TradeProducer(Map<Regulator, ConcurrentLinkedQueue<Trade>> regulatorQueueMap, String productId, String productType) {
        this.regulatorQueueMap = regulatorQueueMap;
        this.product = new Product(productId, productType);
        this.random = new Random();
    }

    public void setRandomSeed(Random random){
        this.random = random;
    }

    @Override
    public void run() {
        Regulator regulator = REGULATOR_LIST.get(random.nextInt(REGULATOR_LIST.size()));
        Trade newTrade = new Trade(product.getProductId() + "-" +tradeId.toString(), product, regulator);
        ConcurrentLinkedQueue queue = regulatorQueueMap.get(regulator);
        queue.add(newTrade);
        tradeId += 1;
    }
}
