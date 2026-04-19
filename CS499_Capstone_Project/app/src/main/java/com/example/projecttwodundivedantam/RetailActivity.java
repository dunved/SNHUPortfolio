package com.example.projecttwodundivedantam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.ComponentActivity;

public class RetailActivity extends ComponentActivity {

    private CartManager cart;
    private Button btnViewCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail);

        cart = CartManager.getInstance();
        String username = getIntent().getStringExtra("username");

        btnViewCart = findViewById(R.id.btnViewCart);
        Button btnMembership = findViewById(R.id.btnPurchaseMembership);
        Button btnGear       = findViewById(R.id.btnPurchaseGear);
        Button btnDrinks     = findViewById(R.id.btnPurchaseDrinks);
        Button btnReturn     = findViewById(R.id.btnReturnFromRetail);

        btnViewCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        btnMembership.setOnClickListener(v -> {
            Intent intent = new Intent(this, PurchaseMembershipActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnGear.setOnClickListener(v -> {
            Intent intent = new Intent(this, PurchaseGearActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnDrinks.setOnClickListener(v -> {
            Intent intent = new Intent(this, PurchaseRefreshmentsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnReturn.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnViewCart.setText("View Cart (" + cart.getItemCount() + " items)");
    }
}