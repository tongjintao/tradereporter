package com.barclays.tradereporter.service;

import com.barclays.tradereporter.model.Regulator;
import com.barclays.tradereporter.model.Trade;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TradeConsumer implements Runnable {
    private ConcurrentLinkedQueue<Trade> queue;
    private Regulator regulator;

    public TradeConsumer(ConcurrentLinkedQueue<Trade> queue, Regulator regulator) {
        this.queue = queue;
        this.regulator = regulator;
    }

    @Override
    public void run() {
        StringBuilder toBePrint = new StringBuilder();
        toBePrint.append(regulator + " reportable trades:");
        toBePrint.append(System.getProperty("line.separator"));
        toBePrint.append("-----------------------");
        toBePrint.append(System.getProperty("line.separator"));
        toBePrint.append("tradeId, productType, reprotableTo, reportTtime");
        toBePrint.append(System.getProperty("line.separator"));
        for(Trade trade: queue){
            toBePrint.append(trade);
            toBePrint.append(System.getProperty("line.separator"));
        }
        toBePrint.append("=======================");
        toBePrint.append(System.getProperty("line.separator"));

        System.out.print(toBePrint);
    }
}
