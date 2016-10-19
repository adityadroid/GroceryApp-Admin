
    package aditya.grocery.admin;

    import android.os.AsyncTask;
    import android.support.design.widget.Snackbar;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.EditText;

    import com.beardedhen.androidbootstrap.BootstrapButton;

    import org.apache.http.NameValuePair;
    import org.apache.http.message.BasicNameValuePair;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.List;

    public class EditSellerActivity extends AppCompatActivity {

        public static final String INSERT_SELLER_URL = "http://thegroceryapp.esy.es/sellers/updatesellerdetails.php";
        public  static final String GET_SELLER_URL= "http://thegroceryapp.esy.es/api.php/sellers";
        EditText sellerName, sellerPhone, sellerMail, sellerAdd1, sellerAdd2;
        BootstrapButton addSeller;
        String name,phone,email,add1,add2,sid;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_seller);
            addSeller=(BootstrapButton)findViewById(R.id.edit_seller_button);
            sellerName=(EditText) findViewById(R.id.edit_seller_name);
            sellerPhone=(EditText)findViewById(R.id.edit_seller_mobile);
            sellerMail=(EditText)findViewById(R.id.edit_seller_email);
            sellerAdd1=(EditText)findViewById(R.id.edit_seller_address1);
            sellerAdd2=(EditText)findViewById(R.id.edit_seller_address2);
            sid = getIntent().getStringExtra("sid");
            new fetchSellerDetails().execute();
            addSeller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if((!sellerName.getText().toString().isEmpty())
                            && (!sellerPhone.getText().toString().isEmpty())
                            && (!sellerMail.getText().toString().isEmpty())
                            && (!sellerAdd1.getText().toString().isEmpty())
                            && (!sellerAdd2.getText().toString().isEmpty())
                            )
                    {

                        name=sellerName.getText().toString();
                        phone=sellerPhone.getText().toString();
                        email=sellerMail.getText().toString();
                        add1=sellerAdd1.getText().toString();
                        add2=sellerAdd2.getText().toString();
                        Log.d("Params:",name+" "+phone+" "+email+" "+add2+" "+add1);
                        new addSeller().execute();
                    }
                    else
                    {
                        Snackbar.make(addSeller,"One or more fields empty!",Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

        }


        public class addSeller extends AsyncTask
        {

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonOBject;

            @Override
            protected void onPreExecute() {
                findViewById(R.id.progress_bar_edit_seller).setVisibility(View.VISIBLE);
                super.onPreExecute();
            }



            @Override
            protected Object doInBackground(Object[] objects) {

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("sname",name));
                params.add(new BasicNameValuePair("mobile",phone));
                params.add(new BasicNameValuePair("address1",add1));
                params.add(new BasicNameValuePair("address2",add2));
                params.add(new BasicNameValuePair("email",email));
                params.add(new BasicNameValuePair("sid",sid));
                Log.d("Params:",name+" "+phone+" "+email+" "+add2+" "+add1);
                jsonOBject=jsonParser.makeHttpRequest(INSERT_SELLER_URL,"POST",params,null);

                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                findViewById(R.id.progress_bar_edit_seller).setVisibility(View.GONE);

                if(jsonOBject!=null)
                {

                    Log.d("RESPONSE::",jsonOBject.toString());
                    try {
                        if(jsonOBject.getBoolean("success"))
                        {
                            Snackbar.make(addSeller,"Updated! Seller ID: "+sid,Snackbar.LENGTH_LONG).show();
                        }
                        else
                        {
                            Snackbar.make(addSeller,"Error Occured!",Snackbar.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Snackbar.make(addSeller,"Try Again!",Snackbar.LENGTH_SHORT).show();
                }
                super.onPostExecute(o);
            }
        }


        public class fetchSellerDetails extends AsyncTask
        {
            JSONParser jsonParser= new JSONParser();
            JSONObject jsonObject;

            @Override
            protected void onPreExecute() {
                findViewById(R.id.progress_bar_edit_seller).setVisibility(View.VISIBLE);

                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("transform","1"));
                params.add(new BasicNameValuePair("filter","sid,eq,"+sid));
                jsonObject=jsonParser.makeHttpRequest(GET_SELLER_URL,"GET",params,null);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {


                findViewById(R.id.progress_bar_edit_seller).setVisibility(View.GONE);

                if(jsonObject!=null)
                {

                    Log.d("RESPONSE:",jsonObject.toString());
                    try {
                        JSONObject sellerDetails = jsonObject.getJSONArray("sellers").getJSONObject(0);
                        name = sellerDetails.getString("sname");
                        phone=sellerDetails.getString("mobile");
                        email=sellerDetails.getString("email");
                        add1=sellerDetails.getString("address1");
                        add2=sellerDetails.getString("address2");
                        sellerName.setText(name);
                        sellerMail.setText(email);
                        sellerAdd1.setText(add1);
                        sellerAdd2.setText(add2);
                        sellerPhone.setText(phone);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Snackbar.make(addSeller,"Try Again!",Snackbar.LENGTH_SHORT).show();
                }
                super.onPostExecute(o);
            }
        }


    }


