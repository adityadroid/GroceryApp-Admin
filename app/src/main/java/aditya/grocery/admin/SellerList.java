package aditya.grocery.admin;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SellerList extends AppCompatActivity {

    ArrayList<String> sellers = new ArrayList<>();
    ArrayList<String> sidList = new ArrayList<>();
    ArrayList<String> listViewItems = new ArrayList<>();
    ListView sellerList;
    ArrayAdapter<String> adapter;
    final String FETCH_SELLERS_URL = "http://thegroceryapp.esy.es/api.php/sellers";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_list);

        sellerList = (ListView) findViewById(R.id.seller_list_view);


       adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,listViewItems );


        sellerList.setAdapter(adapter);



        sellerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(),EditSellerActivity.class);
                i.putExtra("sid",sidList.get(position));
                startActivity(i);

            }

        });
        new fetchSellers().execute();
    }

    public class fetchSellers extends AsyncTask
    {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("transform","1"));

            jsonObject=jsonParser.makeHttpRequest(FETCH_SELLERS_URL,"GET",params,null);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(jsonObject!=null)
            {
                Log.d("RESPONSE:",jsonObject.toString());
                try {
                    JSONArray sellerArray = jsonObject.getJSONArray("sellers");
                    for(int i =0;i<sellerArray.length();i++)
                    {
                        sidList.add(sellerArray.getJSONObject(i).getString("sid"));
                        sellers.add(sellerArray.getJSONObject(i).getString("sname"));
                        listViewItems.add(sidList.get(i)+"."+sellers.get(i));
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                Snackbar.make(sellerList,"Try Again!",Snackbar.LENGTH_SHORT).show();
            }
            super.onPostExecute(o);
        }
    }

}
