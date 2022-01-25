package com.barclays.tradereporter;

import com.barclays.tradereporter.model.Regulator;
import com.barclays.tradereporter.model.Trade;
import com.barclays.tradereporter.service.TradeConsumer;
import com.barclays.tradereporter.service.TradeProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class TradeReporterApplication implements CommandLineRunner {

	@Override
	public void run(String... args) {
		Map<Regulator, ConcurrentLinkedQueue<Trade>> regulatorQueueMap = new HashMap<>();
		regulatorQueueMap.put(Regulator.HongKong,  new ConcurrentLinkedQueue<Trade>());
		regulatorQueueMap.put(Regulator.Japan, new ConcurrentLinkedQueue<Trade>());
		regulatorQueueMap.put(Regulator.Australia, new ConcurrentLinkedQueue<Trade>());
		Map<Regulator, ConcurrentLinkedQueue<Trade>> unmodifiableRegulatorQueueMap = Collections.unmodifiableMap(regulatorQueueMap);

		TradeProducer equityOptionTradingSystem = new TradeProducer(unmodifiableRegulatorQueueMap, "EquityOption", "Equity:Option:PriceReturnBasicPerformance:SingleName");
		TradeProducer creditDerivativesTradingSystem = new TradeProducer(unmodifiableRegulatorQueueMap, "CreditDerivatives", "Credit:Index:IOS");
		TradeProducer interestRatesDerivativesTradingSystem = new TradeProducer(unmodifiableRegulatorQueueMap, "InterestRatesDerivatives", "InterestRate:IRSwap:FixedFloat");

		TradeConsumer hongKongReporter = new TradeConsumer(unmodifiableRegulatorQueueMap.get(Regulator.HongKong), Regulator.HongKong);
		TradeConsumer japanReporter = new TradeConsumer(unmodifiableRegulatorQueueMap.get(Regulator.Japan), Regulator.Japan);
		TradeConsumer australiaReporter = new TradeConsumer(unmodifiableRegulatorQueueMap.get(Regulator.Australia), Regulator.Australia);

		ScheduledExecutorService producerScheduler = Executors.newScheduledThreadPool(3);
		ScheduledExecutorService consumerScheduler = Executors.newScheduledThreadPool(3);

		producerScheduler.scheduleAtFixedRate(equityOptionTradingSystem, 0, 3, TimeUnit.SECONDS);
		producerScheduler.scheduleAtFixedRate(creditDerivativesTradingSystem, 0, 3, TimeUnit.SECONDS);
		producerScheduler.scheduleAtFixedRate(interestRatesDerivativesTradingSystem, 0, 3, TimeUnit.SECONDS);

		consumerScheduler.scheduleAtFixedRate(hongKongReporter, 0, 10, TimeUnit.SECONDS);
		consumerScheduler.scheduleAtFixedRate(japanReporter, 0, 10, TimeUnit.SECONDS);
		consumerScheduler.scheduleAtFixedRate(australiaReporter, 0, 10, TimeUnit.SECONDS);
	}

	public static void main(String[] args) {
		SpringApplication.run(TradeReporterApplication.class, args);
	}

}
