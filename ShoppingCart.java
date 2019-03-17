/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.project.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.shape.Line;
import sit.jpa.project.model.Product;

/**
 *
 * @author Bud
 */
public class ShoppingCart {
    private Map<String,LineItem> cart;

    public ShoppingCart() {
        cart = new HashMap();
    }
    public void Add(Product p){
        LineItem line = cart.get(p.getProductId());
        if (line == null){
            cart.put(p.getProductId(),new LineItem(p));
        }else{
            line.setQuantity(line.getQuantity()+1);
        }
    }
    
    public void remove(Product p){
        cart.remove(p.getProductId());
    }
    
    public double getTotalPrice(){
        double sum =0;
        Collection <LineItem> lineItems = cart.values();
        for (LineItem lineItem : lineItems) {
            sum += lineItem.TotalPrice();
        }
        return sum;
    }
    
    public int getQuantity(){
        int sum =0;
        Collection <LineItem> lineItems = cart.values();
        for (LineItem lineItem : lineItems) {
            sum += lineItem.getQuantity();
        }
        return sum;
    }
    public List<LineItem> getLineItem(){
        return new ArrayList(cart.values());
    }    
    
}
    

