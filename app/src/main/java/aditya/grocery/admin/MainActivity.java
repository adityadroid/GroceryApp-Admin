package aditya.grocery.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class MainActivity extends AppCompatActivity {

    BootstrapButton addNewProductButton, editProductButton, ordersButton,sellersButton,updateSellersButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        addNewProductButton=(BootstrapButton)findViewById(R.id.add_new_product_main);
        editProductButton=(BootstrapButton)findViewById(R.id.edit_product_main);
        ordersButton=(BootstrapButton)findViewById(R.id.orders_main);
        sellersButton=(BootstrapButton)findViewById(R.id.sellers_main);
        updateSellersButton=(BootstrapButton)findViewById(R.id.edit_sellers_main);
        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),AddProductActivity.class);
                startActivity(i);
            }
        });
        editProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ProductFeedActivity.class);
                startActivity(i);
            }
        });
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),OrdersDetailsActivity.class);
                startActivity(i);
            }
        });

        sellersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddSellerActivity.class);
                startActivity(i);

            }
        });
        updateSellersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SellerList.class);
                startActivity(i);

            }
        });

    }
}
