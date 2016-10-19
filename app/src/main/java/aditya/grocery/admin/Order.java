package aditya.grocery.admin;

/**
 * Created by adi on 17/10/16.
 */
public class Order {
String orderID, productNames, productQty, productPrice;
    int totalAmount=0;
    String status;
    public Order(String orderID, String productNames, String productQty, String productPrice, int totalAmount, String status)
    {
        this.orderID=orderID;
        this.productNames=productNames;
        this.productQty=productQty;
        this.productPrice=productPrice;
        this.totalAmount=totalAmount;
        this.status=status;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getProductNames() {
        return productNames;
    }

    public String getProductQty() {
        return productQty;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

