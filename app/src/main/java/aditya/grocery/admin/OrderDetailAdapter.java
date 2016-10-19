package aditya.grocery.admin;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    private List<Order> orderList;


    Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button cancelOrderButton,acceptOrderButton;
        public TextView orderIDTextView, productNamesTextView, productQtyTextView,productPriceTextView, orderTotalTextView,orderStatusTextView;
        public MyViewHolder(View view) {
            super(view);
            orderIDTextView=(TextView)view.findViewById(R.id.order_id_order_details);
            productNamesTextView=(TextView)view.findViewById(R.id.product_names_order_details);
            productQtyTextView=(TextView)view.findViewById(R.id.product_qty_order_details);
            productPriceTextView=(TextView)view.findViewById(R.id.product_price_order_details);
            orderTotalTextView=(TextView)view.findViewById(R.id.order_total_order_details);
            orderStatusTextView=(TextView)view.findViewById(R.id.order_status_order_details);
            cancelOrderButton=(Button)view.findViewById(R.id.order_cancel_order_details);
            acceptOrderButton=(Button)view.findViewById(R.id.order_process_order_details);

        }
    }
    public  OrderDetailAdapter(List<Order> orderList, Context mContext)
    {
        this.orderList=orderList;
        this.mContext=mContext;
    }



    @Override
    public OrderDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final OrderDetailAdapter.MyViewHolder holder, final int position) {


        holder.orderIDTextView.setText(orderList.get(position).getOrderID());
        holder.productNamesTextView.setText(orderList.get(position).getProductNames());
        holder.productPriceTextView.setText(orderList.get(position).getProductPrice());
        holder.productQtyTextView.setText(orderList.get(position).getProductQty());
        holder.orderTotalTextView.setText(orderList.get(position).getTotalAmount()+"");
        String status = orderList.get(position).getStatus();
        if(status.equals("0"))
        {
            holder.orderStatusTextView.setText("PLACED");
            holder.orderStatusTextView.setTextColor(Color.BLUE);
            holder.cancelOrderButton.setVisibility(View.VISIBLE);
            holder.acceptOrderButton.setVisibility(View.VISIBLE);

        }
        else if(status.equals("-1"))
        {
            holder.orderStatusTextView.setText("CANCELLED");
            holder.orderStatusTextView.setTextColor(Color.RED);
            holder.cancelOrderButton.setVisibility(View.GONE);
            holder.acceptOrderButton.setVisibility(View.GONE);

        }
        else if(status.equals("1"))
        {
            holder.orderStatusTextView.setText("PROCESSING");
            holder.orderStatusTextView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.cancelOrderButton.setVisibility(View.VISIBLE);
            holder.acceptOrderButton.setVisibility(View.GONE);

        }
        else if(status.equals("2"))
        {
            holder.orderStatusTextView.setText("DELIVERED");
            holder.orderStatusTextView.setTextColor(Color.GREEN);
            holder.cancelOrderButton.setVisibility(View.GONE);
            holder.acceptOrderButton.setVisibility(View.GONE);


        }


        holder.cancelOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new cancelOrder(orderList.get(position).getOrderID(),holder.cancelOrderButton,position,"-1").execute();
            }
        });
        holder.acceptOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new cancelOrder(orderList.get(position).getOrderID(),holder.cancelOrderButton,position,"1").execute();

            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public class cancelOrder extends AsyncTask
    {


        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        String orderID;
        Button bt;
        int position;
        String status;
        public cancelOrder(String orderID,Button bt,int position,String status)
        {
            this.orderID=orderID;
            this.bt=bt;
            this.position=position;
            this.status=status;

        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(mContext,"Updating Status..",Toast.LENGTH_LONG).show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("orderid",orderID));
            params.add(new BasicNameValuePair("status",status));
            jsonObject= jsonParser.makeHttpRequest("http://thegroceryapp.esy.es/orders/updateorderstatus.php","POST",params,null);
            try {
                Log.d("SUCCESS",jsonObject.getBoolean("success")+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(jsonObject!=null)
            {

                Log.d("RESPONSE:",jsonObject.toString());

                try {
                    Log.d("SUCCESS",jsonObject.getBoolean("success")+"");
                    if(jsonObject.getBoolean("success"))
                    {
                        //Toast.makeText(mContext,"Order Cancelled!",Toast.LENGTH_SHORT).show();
                         Snackbar.make(bt,"Cancelled Order!", Snackbar.LENGTH_SHORT).show();
                        orderList.get(position).setStatus(status);
                        notifyDataSetChanged();
                    }
                    else
                    {
                        //Toast.makeText(mContext,"Try again!",Toast.LENGTH_SHORT).show();

                           Snackbar.make(bt,"Failed!", Snackbar.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                Snackbar.make(bt,"Failed!", Snackbar.LENGTH_SHORT).show();

            }
            super.onPostExecute(o);
        }
    }
}
