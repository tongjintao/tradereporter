package com.barclays.tradereporter.service;

import com.barclays.tradereporter.model.Regulator;
import com.barclays.tradereporter.model.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

class TradeProducerTest {
    Random random;
    Map<Regulator, ConcurrentLinkedQueue<Trade>> unmodifiableRegulatorQueueMap;

    @BeforeEach
    public void setUp() {
        Map<Regulator, ConcurrentLinkedQueue<Trade>> regulatorQueueMap = new HashMap<>();
        regulatorQueueMap.put(Regulator.HongKong,  new ConcurrentLinkedQueue<Trade>());
        regulatorQueueMap.put(Regulator.Japan, new ConcurrentLinkedQueue<Trade>());
        regulatorQueueMap.put(Regulator.Australia, new ConcurrentLinkedQueue<Trade>());
        unmodifiableRegulatorQueueMap = Collections.unmodifiableMap(regulatorQueueMap);

        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt()).thenReturn(1);
    }

    @Test
    public void shouldAddTradeToQueue(){
        TradeProducer equityOptionTradingSystem = new TradeProducer(unmodifiableRegulatorQueueMap, "EquityOption", "Equity:Option:PriceReturnBasicPerformance:SingleName");
        equityOptionTradingSystem.setRandomSeed(random);
        equityOptionTradingSystem.run();

        Trade trade = unmodifiableRegulatorQueueMap.get(Regulator.HongKong).peek();
        assertEquals("EquityOption-0", trade.getTradeId());
        assertEquals("Equity:Option:PriceReturnBasicPerformance:SingleName", trade.getProduct().getProductType());
        assertEquals(Regulator.HongKong, trade.getReportableTo());
    }

}