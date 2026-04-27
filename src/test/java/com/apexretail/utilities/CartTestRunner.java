package com.apexretail.utilities;

import com.apexretail.domain.Cart;
import com.apexretail.domain.CartItem;

public class CartTestRunner {

    public static void main(String[] args) {
        Cart cart = new Cart();

        cart.addItem(1, 2);

        for (CartItem item : cart.getItems()) {
            System.out.println("ID: " + item.getProductID() +
                    ", Qty: " + item.getQuantity());
        }

        cart.addItem(1, 3);

        for (CartItem item : cart.getItems()) {
            System.out.println("ID: " + item.getProductID() +
                    ", Qty: " + item.getQuantity());
        }

        cart.addItem(2, 1);
        cart.updateQuantity(1, 10);
        // cart.updateQuantity(999, 2);
        cart.removeItem(2);
        cart.clear();
        // cart.removeItem(1);
        // cart.addItem(1, 0);
        // cart.addItem(-1, 1);
        // cart.updateQuantity(1, -10);

        System.out.println(cart.getItems().size());

        for (CartItem item : cart.getItems()) {
            System.out.println("ID: " + item.getProductID() +
                    ", Qty: " + item.getQuantity());
        }

    }

}
