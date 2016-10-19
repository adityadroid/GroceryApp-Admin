package aditya.grocery.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrdersDetailsActivity extends AppCompatActivity {

    RecyclerView orderDetailsRecyclerView;
    List<Order> orderList = new ArrayList<>();
    static final String GETORDER_ID_URL = "http://thegroceryapp.esy.es/api.php/orders";
    static  final String GET_ORDER_DETAILS_URL = "http://thegroceryapp.esy.es/api.php/orderdetails";
   // int cid;
    OrderDetailAdapter adapter;
    int totalAmount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_details);
        orderDetailsRecyclerView=(RecyclerView)findViewById(R.id.order_details_recycler);
         adapter = new OrderDetailAdapter(orderList,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        orderDetailsRecyclerView.setLayoutManager(layoutManager);
        orderDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        orderDetailsRecyclerView.setAdapter(adapter);
        //final SharedPreferences settings = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
       // cid= settings.getInt("cid",0);
        new getOrderIDs().execute();

    }

    public  class getOrderIDs extends AsyncTask
    {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            findViewById(R.id.progress_bar_order_details).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("transform","1"));
           // params.add(new BasicNameValuePair("filter","cid,eq,"+cid));

            jsonObject=jsonParser.makeHttpRequest(GETORDER_ID_URL,"GET",params,null);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            findViewById(R.id.progress_bar_order_details).setVisibility(View.GONE);
            if(jsonObject!=null)
            {

                Log.d("RESPONSE::",jsonObject.toString());
                JSONArray orders = null;
                try {
                    orders = jsonObject.getJSONArray("orders");
                    for(int i =0;i<orders.length();i++)
                    {
                        String orderID = orders.getJSONObject(i).getString("orderid");
                        String status = orders.getJSONObject(i).getString("status");

                        new getOrderDetails(orderID,status).execute();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                Log.d("RESPONSE","NULL JSON ");
            }
            super.onPostExecute(o);
        }
    }

    public  class getOrderDetails extends AsyncTask
    {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        String orderID;
        String status;

        @Override
        protected void onPreExecute() {
            findViewById(R.id.progress_bar_order_details).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        public  getOrderDetails(String orderID, String status)
        {
            this.orderID=orderID;
            this.status=status;

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("transform","1"));
            params.add(new BasicNameValuePair("filter","orderid,eq,"+orderID));
            jsonObject=jsonParser.makeHttpRequest(GET_ORDER_DETAILS_URL,"GET",params,null);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            findViewById(R.id.progress_bar_order_details).setVisibility(View.GONE);
            if(jsonObject!=null)
            {

                Log.d("RESPONSE::",jsonObject.toString());
                try {
                    JSONArray orderdetails = jsonObject.getJSONArray("orderdetails");
                    List<String> pidList = new ArrayList<>();
                    List<String> qtyList = new ArrayList<>();

                    for(int i=0;i<orderdetails.length();i++)
                    {

                        String pid = orderdetails.getJSONObject(i).getString("pid");
                        String qty = orderdetails.getJSONObject(i).getString("qty");
                        pidList.add(pid);
                        qtyList.add(qty);
                    }
                    new getProductDetails(orderID,status,pidList,qtyList).execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                Log.d("RESPONSE","NULL JSON ");
            }
            super.onPostExecute(o);
        }
    }



    public class getProductDetails extends AsyncTask
    {
        List<String> pidList;
        List<String> qtyList;
        JSONObject jsonObject;
        String orderID,status;

        @Override
        protected void onPreExecute() {
            findViewById(R.id.progress_bar_order_details).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        public getProductDetails(String orderID, String status, List<String> pid, List<String> qty)
        {
            this.orderID=orderID;
            this.status=status;
            this.pidList=pid;
            this.qtyList=qty;

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("transform","1"));
            for(int i =0;i<pidList.size();i++)
            {
                Log.d("PID:",pidList.get(i));
                params.add(new BasicNameValuePair("filter[]","pid,eq,"+pidList.get(i)));

            }
            params.add(new BasicNameValuePair("satisfy","any"));

            jsonObject=jsonParser.makeHttpRequest("http://thegroceryapp.esy.es/api.php/products","GET",params,null);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            findViewById(R.id.progress_bar_order_details).setVisibility(View.GONE);

            if(jsonObject!=null) {
                    Log.d("RESPONSE:",jsonObject.toString());
                try {
                    JSONArray productDetails = jsonObject.getJSONArray("products");
                        int totalAmount=0;
                    String name="";
                    String price ="";
                    String qty="";
                    for(int i =0;i<productDetails.length();i++)
                    {
                      String curname = productDetails.getJSONObject(i).getString("name");
                        name = name+"\n"+curname;
                        String curprice = productDetails.getJSONObject(i).getString("price");
                        price=price+"\n"+curprice;
                        qty = qty+"\n"+qtyList.get(i);
                        totalAmount = totalAmount+(Integer.parseInt(curprice.trim())*Integer.parseInt(qtyList.get(i)));
                    }
                    price=price.replace("\\\n", System.getProperty("line.separator"));
                    name = name.replace("\\\n", System.getProperty("line.separator"));
                    qty = qty.replace("\\\n", System.getProperty("line.separator"));

                    // Product product = new  Product( name,description, price, img, stock, category, pid,qty);
                // orderList.add(product);
                //totalAmountTextView.setText(totalAmount+"");
                Order order = new Order(orderID,name , price,qty , totalAmount, status);
                orderList.add(order);
                adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
            else
            {
                Log.d("RESPONSE:","NULL JSON");
            }
            super.onPostExecute(o);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

}
