package com.barclays.tradereporter.service;

import com.barclays.tradereporter.model.Regulator;
import com.barclays.tradereporter.model.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TradeConsumerTest {
    Random random;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    Map<Regulator, ConcurrentLinkedQueue<Trade>> unmodifiableRegulatorQueueMap;

    @BeforeEach
    public void setUp() {
        Map<Regulator, ConcurrentLinkedQueue<Trade>> regulatorQueueMap = new HashMap<>();
        regulatorQueueMap.put(Regulator.HongKong,  new ConcurrentLinkedQueue<Trade>());
        regulatorQueueMap.put(Regulator.Japan, new ConcurrentLinkedQueue<Trade>());
        regulatorQueueMap.put(Regulator.Australia, new ConcurrentLinkedQueue<Trade>());
        unmodifiableRegulatorQueueMap = Collections.unmodifiableMap(regulatorQueueMap);

        System.setOut(new PrintStream(outputStreamCaptor));

        random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt()).thenReturn(1);
    }

    @Test
    public void shouldPrintTradeToConsole(){
        TradeProducer equityOptionTradingSystem = new TradeProducer(unmodifiableRegulatorQueueMap, "EquityOption", "Equity:Option:PriceReturnBasicPerformance:SingleName");
        equityOptionTradingSystem.setRandomSeed(random);
        equityOptionTradingSystem.run();

        TradeConsumer hongKongReporter = new TradeConsumer(unmodifiableRegulatorQueueMap.get(Regulator.HongKong), Regulator.HongKong);
        hongKongReporter.run();

        assertThat(outputStreamCaptor.toString().trim()).containsPattern("HongKong reportable trades:\r\n" +
                "-----------------------\r\n" +
                "tradeId, productType, reprotableTo, reportTtime\r\n" +
                "EquityOption-0, Equity:Option:PriceReturnBasicPerformance:SingleName, HongKong, .*\r\n" +
                "=======================");
    }

}