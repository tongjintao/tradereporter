package com.barclays.tradereporter.model;

import java.util.Objects;

public class Product {
    private String productId;
    private String productType;

    public Product(String productId, String productType) {
        this.productId = productId;
        this.productType = productType;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductType() {
        return productType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId) &&
                Objects.equals(productType, product.productType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productType);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }
}
