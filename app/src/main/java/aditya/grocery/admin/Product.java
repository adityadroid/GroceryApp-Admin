package aditya.grocery.admin;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by adi on 9/10/16.
 */
public class Product implements Parcelable {
    String name, description,price,img,stock,category,pid,sid;
    int qty;

    public Product(String name, String description, String price, String img, String stock, String category, String pid, String sid)
    {
        this.name=name;
        this.description=description;
        this.price=price;
        this.img=img;
        this.stock=stock;
        this.category=category;
        this.pid=pid;
        this.sid=sid;
    }

    public Product(String name, String description, String price, String img, String stock, String category, String pid, int qty)
    {
        this.name=name;
        this.description=description;
        this.price=price;
        this.img=img;
        this.stock=stock;
        this.category=category;
        this.pid=pid;
        this.qty=qty;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getQty() {
        return qty;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public String getPid() {
        return pid;
    }
    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(description);
        out.writeString(price);
        out.writeString(category);
        out.writeString(img);
        out.writeString(pid);
        out.writeString(stock);
        out.writeInt(qty);
        out.writeString(sid);

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Product(Parcel in) {
        name = in.readString();
        description=in.readString();
        price=in.readString();
        category=in.readString();
        img = in.readString();
        pid = in.readString();
        stock = in.readString();
        qty= in.readInt();
        sid=in.readString();
    }




    /*Comparator for sorting the list by Student Name*/
    public static Comparator<Product> SortByName = new Comparator<Product>() {

        public int compare(Product p1, Product p2) {
            String pName1 = p1.getName().toUpperCase();
            String pName2 = p2.getName().toUpperCase();

            //ascending order
            return pName1.compareTo(pName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};



    /*Comparator for sorting the list by Student Name*/
    public static Comparator<Product> PriceLowToHigh = new Comparator<Product>() {

        public int compare(Product p1, Product p2) {
            String pName1 = p1.getPrice().toUpperCase();
            String pName2 = p2.getPrice().toUpperCase();


            return pName1.compareTo(pName2);
        }};
    public static Comparator<Product> PriceHighTOLow = new Comparator<Product>() {

        public int compare(Product p1, Product p2) {
            String pName1 = p1.getPrice().toUpperCase();
            String pName2 = p2.getPrice().toUpperCase();


            return pName2.compareTo(pName1);
        }};


}
