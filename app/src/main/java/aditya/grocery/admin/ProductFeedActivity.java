package aditya.grocery.admin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProductFeedActivity extends AppCompatActivity{

    RecyclerView productRecycler;
    List<Product> productList = new ArrayList<>();
    Spinner sortBy,selectCatSpinner;
    String[] sortByList = {"Price Low to High","Price High to Low","Alphabatically"};
    static final String PRODUCTS_URL = "http://thegroceryapp.esy.es/getproducts.php";
    JSONParser jsonParser = new JSONParser();
    ProductAdapter adapter;
    String selectedCat;

    CharSequence searchQuery;
    String[] categories = { "bath and shower",
            "household supplies",
            "pasta and noodles",
            "easy meals and mixes",
            "dairy",
            "cooking essentials",
            "snack foods",
            "fragrance",
            "baby care",
            "personal and healthcare",
            "skin care",
            "chocolates",
            "beverages"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_feed);
        setTitle("Results");




        productRecycler =(RecyclerView)findViewById(R.id.productFeedRecycler);
        sortBy= (Spinner)findViewById(R.id.sortBySpinner);
        selectCatSpinner=(Spinner)findViewById(R.id.chooseCategorySpinner);

        ArrayAdapter<String> sortByAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,sortByList);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        sortBy.setAdapter(sortByAdapter);
        selectCatSpinner.setAdapter(categoryAdapter);

        adapter = new ProductAdapter(productList,getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        productRecycler.setLayoutManager(layoutManager);
        productRecycler.setItemAnimator(new DefaultItemAnimator());
        productRecycler.setAdapter(adapter);
        productRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        productRecycler.addOnItemTouchListener( new RecyclerItemClickListener(getApplicationContext(), productRecycler,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {

                Intent i = new Intent(getApplicationContext(),ProductDetailsActivity.class);
                i.putExtra("product",(Parcelable) productList.get(position));
                startActivity(i);
                // do whatever
            }

            @Override public void onLongItemClick(View view, int position) {
                // do whatever
            }

        }));


        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==0)
                {

                    Collections.sort(productList,Product.PriceLowToHigh);
                    adapter.notifyDataSetChanged();


                }
                else if(i==1)
                {
                    Collections.sort(productList,Product.PriceHighTOLow);
                    adapter.notifyDataSetChanged();

                }
                else if(i==2)
                {
                    Collections.sort(productList,Product.SortByName);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedCat= adapterView.getItemAtPosition(i).toString();
                new fetchProducts().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



//    public void prepareData()
//    {
//        Product product = new Product("onion", "this is a product", "200INR", "","");
//        productList.add(product);
//        productList.add(product);
//
//        productList.add(product);
//
//        productList.add(product);
//
//
//        adapter.notifyDataSetChanged();
//
//
//
//
//
//
//
//    }

    public class fetchProducts extends AsyncTask
    {
        JSONObject jsonObject;
        @Override
        protected void onPreExecute() {
            findViewById(R.id.progress_bar_product_feed).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("category",selectedCat));

            jsonObject= jsonParser.makeHttpRequest(PRODUCTS_URL,"POST",list,null);
            Log.d("JSON:::",jsonObject.toString());
            return jsonObject;
        }

        @Override
        protected void onPostExecute(Object o) {
            findViewById(R.id.progress_bar_product_feed).setVisibility(View.GONE);
            try {
                boolean success = jsonObject.getBoolean("success");

                if(success)
                {
                    productList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject row = jsonArray.getJSONObject(i);
                        String name = row.getString("name");
                        String pid = row.getString("pid");
                        String category = row.getString("category");
                        String price = row.getString("price");
                        String description = row.getString("description");
                        String stock = row.getString("quantity");
                        String img = row.getString("img");
                        String sid = row.getString("sid");

                        Product product = new Product(name,description,price,img,stock,category,pid,sid);
                        productList.add(product);
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Snackbar.make(productRecycler,"Some error occured!", Snackbar.LENGTH_SHORT).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(o);
        }
    }

    public class fetchSearchResults extends AsyncTask
    {
        JSONObject jsonObject;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("transform","1"));
            params.add(new BasicNameValuePair("filter","name,cs,"+searchQuery));
            jsonObject=jsonParser.makeHttpRequest("http://thegroceryapp.esy.es/api.php/products","GET",params,null);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(jsonObject!=null)
            {
                Log.d("RESPONSE::",jsonObject.toString());
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray("products");
                    for(int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject row = jsonArray.getJSONObject(i);
                        String name = row.getString("name");
                        String pid = row.getString("pid");
                        String category = row.getString("category");
                        String price = row.getString("price");
                        String description = row.getString("description");
                        String stock = row.getString("quantity");
                        String img = row.getString("img");
                        String sid = row.getString("sid");

                        Product product = new Product(name,description,price,img,stock,category,pid,sid);
                        productList.add(product);
                    }
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(o);
        }
    }
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(),AddProductActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        super.onBackPressed();
//    }



}
