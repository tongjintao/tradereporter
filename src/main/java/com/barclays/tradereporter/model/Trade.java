package com.barclays.tradereporter.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Trade {
    private String tradeId;
    private Product product;
    private Regulator reportableTo;
    private LocalDateTime reportTime;

    public Trade(String tradeId, Product product, Regulator reportableTo) {
        this.tradeId = tradeId;
        this.product = product;
        this.reportableTo = reportableTo;
        this.reportTime = LocalDateTime.now();
    }

    public String getTradeId() {
        return tradeId;
    }

    public Product getProduct() {
        return product;
    }

    public Regulator getReportableTo() {
        return reportableTo;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(tradeId, trade.tradeId) &&
                Objects.equals(product, trade.product) &&
                reportableTo == trade.reportableTo &&
                Objects.equals(reportTime, trade.reportTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, product, reportableTo, reportTime);
    }

    @Override
    public String toString() {
        return  tradeId + ", " + product.getProductType() + ", " + reportableTo + ", " + reportTime;
    }
}
