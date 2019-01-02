package com.mayihi.store.domain;

public class CartItem {

    private Product product;
    private int quantity;
    private double subTotal;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return this.product.getShop_price() * this.quantity;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
