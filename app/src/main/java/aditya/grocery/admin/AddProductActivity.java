package aditya.grocery.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.farhanahmed.httpfilerequest.HttpFileRequest;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tangxiaolv.telegramgallery.GalleryActivity;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


public class AddProductActivity extends AppCompatActivity {

    File file;
    HttpFileRequest httpFileRequest;
    //HashMap<String, String> params = new HashMap<>();
   EditText productName,productPrice,productDesc, productQty,productSID;
    BootstrapButton uploadIMGButton, chooseFileButton;
    String name,price,desc,qty,sid,cat;
    Spinner chooseCat;
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
    TextView productIMGTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseCat=(Spinner)findViewById(R.id.categories_product);
        productName=(EditText)findViewById(R.id.product_name_product);
        productDesc=(EditText)findViewById(R.id.description_product);
        productPrice=(EditText)findViewById(R.id.price_product);
        productSID=(EditText)findViewById(R.id.seller_id_product);
        uploadIMGButton=(BootstrapButton) findViewById(R.id.upload_product);
        chooseFileButton=(BootstrapButton)findViewById(R.id.open_image_picker_product);
        productIMGTitle=(TextView)findViewById(R.id.name_image_product);
        productQty=(EditText)findViewById(R.id.quantity_product);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        chooseCat.setAdapter(adapter);
        chooseCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat= adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 GalleryActivity.openActivity(AddProductActivity.this,true, 9, 12);

            }
        });
        uploadIMGButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!productName.getText().toString().isEmpty()
                    && !productSID.getText().toString().isEmpty()
                        && !productPrice.getText().toString().isEmpty()
                        && !productDesc.getText().toString().isEmpty()
                        && !productQty.getText().toString().isEmpty()

                        )

                {
                    name=productName.getText().toString();
                    price=productPrice.getText().toString();
                    qty=productQty.getText().toString();
                    sid=productSID.getText().toString();
                    desc=productDesc.getText().toString();

                    if(file!=null) {
                        new postimg2().execute();
                    }
                    else
                    {
                        Snackbar.make(uploadIMGButton,"Choose a Image!",Snackbar.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Snackbar.make(uploadIMGButton,"One or more fields empty",Snackbar.LENGTH_SHORT).show();
                }


            }
        });
//        params.put("name","pro");
//        params.put("price","10");
//        params.put("description","asd");
//        params.put("quantity","11");
//        params.put("category","food");
//        params.put("sid","2");





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (12 == requestCode && resultCode == Activity.RESULT_OK) {
            //list of photos of seleced
            List<String> photos = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
                Log.d("IMAGE:", photos.get(0));
                file = new File(photos.get(0));
                productIMGTitle.setText(photos.get(0));
        }

    }


    public  class postimg2 extends AsyncTask
    {

//        @Override
//        protected void onPreExecute() {
//            findViewById(R.id.progress_bar_product_upload).setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            findViewById(R.id.progress_bar_product_upload).setVisibility(View.GONE);
//            super.onPostExecute(o);
//        }

        @Override
        protected Object doInBackground(Object[] objects) {



            Ion.with(getBaseContext()).load("POST","http://thegroceryapp.esy.es/upload.php")
                    .uploadProgressHandler(new ProgressCallback()
            {
                @Override
                public void onProgress(long uploaded, long total)
                {

                    if(uploaded==total)
                    {
                        findViewById(R.id.progress_bar_product_upload).setVisibility(View.GONE);

                    }
                    else
                    {
                        findViewById(R.id.progress_bar_product_upload).setVisibility(View.VISIBLE);

                    }
                    System.out.println("uploaded " + (int)uploaded + " Total: "+total);
                }
            }).setMultipartParameter("name",name)
                    .setMultipartParameter("price",price)
                    .setMultipartParameter("description",desc)
                    .setMultipartParameter("quantity",qty)
                    .setMultipartParameter("category",cat)
                    .setMultipartParameter("sid",sid)
            .setMultipartFile("file",file).asString().setCallback(new FutureCallback<String>()
            {
                @Override
                public void onCompleted(Exception e, String result)
                {
                    if(result!=null)
                    {
                        if(result.equals("success!"))
                        {
                            Toast.makeText(getApplicationContext(),"Product Uploaded Successfully!",Toast.LENGTH_SHORT).show();
                        }

                        Log.d("status:",result);

                    }
                    else
                    {

                        Toast.makeText(getApplicationContext(),"Product Upload Failed, Try Again!",Toast.LENGTH_SHORT).show();

                    }
                }
            });
            return null;
        }
    }

    public class postimg extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects) {



            SyncHttpClient client = new SyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("name","pro");
            params.put("price","10");
            params.put("description","asd");
            params.put("quantity","11");
            params.put("category","food");
            params.put("sid","2");
            try {
                params.put("file", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            client.post("http://thegroceryapp.esy.es/upload.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    Log.d("RESPONsE:","failed");

                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    Log.d("RESPONsE:",responseString);
                }

//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                // error handling
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                // success
//            }
            });
            return null;
        }
    }





}

