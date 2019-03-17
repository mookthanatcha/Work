/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.project.model;

import sit.jpa.project.model.Product;

/**
 *
 * @author Bud
 */
public class LineItem {
    
    private Product product; 
    private double price;
    private int quantity;

    public LineItem(Product product) {
        this(product,1);
    }
    
    public LineItem(Product product,int quantity) {
        this.product = product;
        this.price = product.getPrice();
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double TotalPrice (){
        return this.price*this.quantity;
    }


}
