package com.example.projecttwodundivedantam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import java.util.List;

public class CartActivity extends ComponentActivity {

    private CartManager cart;
    private LinearLayout containerCartItems;
    private TextView textEmptyCart;
    private TextView textCartTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart               = CartManager.getInstance();
        containerCartItems = findViewById(R.id.containerCartItems);
        textEmptyCart      = findViewById(R.id.textEmptyCart);
        textCartTotal      = findViewById(R.id.textCartTotal);

        Button btnCheckout        = findViewById(R.id.btnCheckout);
        Button btnClearCart       = findViewById(R.id.btnClearCart);
        Button btnContinueShopping = findViewById(R.id.btnContinueShopping);

        btnCheckout.setOnClickListener(v -> {
            if (cart.getItems().isEmpty()) {
                Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Order placed! Total: $" + String.format("%.2f", cart.getTotal()),
                        Toast.LENGTH_LONG).show();
                cart.clearCart();
                loadCart();
            }
        });

        btnClearCart.setOnClickListener(v -> {
            cart.clearCart();
            loadCart();
            Toast.makeText(this, "Cart cleared.", Toast.LENGTH_SHORT).show();
        });

        btnContinueShopping.setOnClickListener(v -> finish());

        loadCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }

    private void loadCart() {
        containerCartItems.removeAllViews();
        List<CartItem> items = cart.getItems();

        if (items.isEmpty()) {
            textEmptyCart.setVisibility(View.VISIBLE);
            textCartTotal.setText("Total: $0.00");
            return;
        }

        textEmptyCart.setVisibility(View.GONE);

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            final int index = i;

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setBackgroundColor(0xFF1E1E1E);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rowParams.setMargins(0, 0, 0, 8);
            row.setLayoutParams(rowParams);
            row.setPadding(24, 20, 24, 20);

            TextView label = new TextView(this);
            label.setText(item.toString());
            label.setTextColor(0xFFFFFFFF);
            label.setTextSize(14);
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            label.setLayoutParams(labelParams);

            Button btnRemove = new Button(this);
            btnRemove.setText("Remove");
            btnRemove.setTextSize(11);
            btnRemove.setBackgroundColor(0xFF8B0000);
            btnRemove.setTextColor(0xFFFFFFFF);
            btnRemove.setOnClickListener(v -> {
                cart.removeItem(index);
                loadCart();
            });

            row.addView(label);
            row.addView(btnRemove);
            containerCartItems.addView(row);
        }

        textCartTotal.setText("Total: $" + String.format("%.2f", cart.getTotal()));
    }
}