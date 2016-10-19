package aditya.grocery.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by adi on 9/10/16.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<Product> productList;
    Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AwesomeTextView title, price,shortDesc;
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            title = (AwesomeTextView) view.findViewById(R.id.feedProductTitle);
            price = (AwesomeTextView) view.findViewById(R.id.feedProductPrice);
            shortDesc=(AwesomeTextView)view.findViewById(R.id.feedProductShortDesc);
            imageView=(ImageView)view.findViewById(R.id.feedProductIMG);

        }
    }
    public  ProductAdapter(List<Product> productList, Context mContext)
    {
        this.productList=productList;
        this.mContext=mContext;
    }



    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_feed_item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyViewHolder holder, final int position) {

        Product product = productList.get(position);
        holder.title.setText(product.getName());
        holder.price.setText("Rs "+product.getPrice());
        String desc = product.getDescription().length()>20?product.getDescription().substring(0,20)+"..":product.getDescription()+"..";
        holder.shortDesc.setText(desc);
        String temp ="http://thegroceryapp.esy.es/uploads/"+product.getImg();
                temp = temp.replaceAll(" ", "%20");
        Log.d("url",temp);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,ProductDetailsActivity.class);
                i.putExtra("product",(Parcelable) productList.get(position));
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,ProductDetailsActivity.class);
                i.putExtra("product",(Parcelable) productList.get(position));

            }
        });
        Picasso.with(mContext).load(temp).into(holder.imageView);
        
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
