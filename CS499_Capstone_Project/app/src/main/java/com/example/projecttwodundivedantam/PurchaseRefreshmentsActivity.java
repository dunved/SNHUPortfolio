package com.example.projecttwodundivedantam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

public class PurchaseRefreshmentsActivity extends ComponentActivity {

    private CartManager cart;
    private Button btnViewCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_refreshments);

        cart        = CartManager.getInstance();
        btnViewCart = findViewById(R.id.btnViewCartDrinks);

        Button btnWater         = findViewById(R.id.btnBuyWater);
        Button btnElectrolyte12 = findViewById(R.id.btnBuyElectrolyte12);
        Button btnElectrolyte16 = findViewById(R.id.btnBuyElectrolyte16);
        Button btnEnergy        = findViewById(R.id.btnBuyEnergyDrink);
        Button btnReturn        = findViewById(R.id.btnReturnFromDrinks);

        btnWater.setOnClickListener(v         -> addToCart("Water Bottle", 1.00));
        btnElectrolyte12.setOnClickListener(v -> addToCart("Electrolyte Mix 12 oz", 3.00));
        btnElectrolyte16.setOnClickListener(v -> addToCart("Electrolyte Mix 16 oz", 3.50));
        btnEnergy.setOnClickListener(v        -> addToCart("Energy Drink", 3.00));

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