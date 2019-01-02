package com.mayihi.store.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    private Map<String, CartItem> itemMap;//Map<pid,CartItem>
    private double total;
    private Collection<CartItem> items;

    public void setItems(Collection<CartItem> items) {
        this.items = items;
    }

    public Collection<CartItem> getItems() {
        return itemMap.values();
    }

    public Cart() {
        itemMap = new HashMap<>();
    }

    /**
     * 添加商品到购物车
     *
     * @param cartItem 一种商品
     * @param num      商品数量
     */
    public void addItemToCart(CartItem cartItem, int num) {
        String pid = cartItem.getProduct().getPid();
        if (itemMap.containsKey(cartItem.getProduct().getPid())) {
            //之前购物车有这件商品，在商品数量基础上增加num
            cartItem.setQuantity(itemMap.get(cartItem.getProduct().getPid()).getQuantity() + num);
        }else{
            //之前购物车没有这件商品，增加num
            cartItem.setQuantity(num);
        }
        itemMap.put(pid, cartItem);
    }

    /**
     * 清空购物车
     */
    public void clearCart() {
        itemMap.clear();
    }

    /**
     * 删除购物车的商品
     * @param pid 商品pid
     */
    public void delItemFromCartByPid(String pid) {
        itemMap.remove(pid);
    }


    public Map<String, CartItem> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, CartItem> itemMap) {
        this.itemMap = itemMap;
    }

    public double getTotal() {
        total = 0;
        for (Map.Entry<String, CartItem> cartItemEntry : itemMap.entrySet()) {
            total += cartItemEntry.getValue().getSubTotal();
        }
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
