package com.example.projecttwodundivedantam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

public class PurchaseMembershipActivity extends ComponentActivity {

    private CartManager cart;
    private Button btnViewCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_membership);

        cart        = CartManager.getInstance();
        btnViewCart = findViewById(R.id.btnViewCartMembership);

        Button btnStriking  = findViewById(R.id.btnBuyStriking);
        Button btnGrappling = findViewById(R.id.btnBuyGrappling);
        Button btnUltimate  = findViewById(R.id.btnBuyUltimate);
        Button btnReturn    = findViewById(R.id.btnReturnFromMembershipPurchase);

        btnStriking.setOnClickListener(v  -> addToCart("Striking Membership", 125));
        btnGrappling.setOnClickListener(v -> addToCart("Grappling Membership", 125));
        btnUltimate.setOnClickListener(v  -> addToCart("Ultimate Membership", 150));

        btnViewCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        btnReturn.setOnClickListener(v -> finish());

        updateCartButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartButton();
    }

    private void addToCart(String name, double price) {
        cart.addItem(name, price);
        updateCartButton();
        Toast.makeText(this, name + " added to cart!", Toast.LENGTH_SHORT).show();
    }

    private void updateCartButton() {
        btnViewCart.setText("View Cart (" + cart.getItemCount() + ")");
    }
}