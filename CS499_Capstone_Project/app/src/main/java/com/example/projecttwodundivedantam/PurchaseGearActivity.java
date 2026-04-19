package com.example.projecttwodundivedantam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

public class PurchaseGearActivity extends ComponentActivity {

    private CartManager cart;
    private Button btnViewCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_gear);

        cart        = CartManager.getInstance();
        btnViewCart = findViewById(R.id.btnViewCartGear);

        Button btnRashguard       = findViewById(R.id.btnBuyRashguard);
        Button btnGrapplingShorts = findViewById(R.id.btnBuyGrapplingShorts);
        Button btnGi              = findViewById(R.id.btnBuyGi);
        Button btnMuayThaiShorts  = findViewById(R.id.btnBuyMuayThaiShorts);
        Button btnGloves          = findViewById(R.id.btnBuyGloves);
        Button btnShinguards      = findViewById(R.id.btnBuyShinguards);
        Button btnReturn          = findViewById(R.id.btnReturnFromGear);

        btnRashguard.setOnClickListener(v       -> addToCart("BJJ Rashguard", 50));
        btnGrapplingShorts.setOnClickListener(v -> addToCart("Grappling Shorts", 40));
        btnGi.setOnClickListener(v              -> addToCart("BJJ Gi + Belt", 100));
        btnMuayThaiShorts.setOnClickListener(v  -> addToCart("Muay Thai Shorts", 60));
        btnGloves.setOnClickListener(v          -> addToCart("Boxing Gloves", 80));
        btnShinguards.setOnClickListener(v      -> addToCart("Shinguards", 100));

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