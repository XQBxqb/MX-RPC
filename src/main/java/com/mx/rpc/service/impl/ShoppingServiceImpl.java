package com.mx.rpc.service.impl;

import com.mx.rpc.service.ShoppingService;

import java.util.ArrayList;
import java.util.List;

public class ShoppingServiceImpl implements ShoppingService {
    private static List<String> products = new ArrayList<>();
    static {
        products.add("阿迪");
        products.add("耐克");
    }

    @Override
    public Integer price(String name) {
        return 10;
    }

    @Override
    public List<String> products() {
        return products;
    }
}
