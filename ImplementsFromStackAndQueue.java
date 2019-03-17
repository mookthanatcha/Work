/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg60130500039.sec1.assignment;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ADMIN
 */
public class ImplementsFromStackAndQueue implements Stack,Queue{
    private Object[] list;
    private int maxStackSize = 100;
    private int topStack = 0;
    
    public ImplementsFromStackAndQueue(){
        list = new Object[maxStackSize];
    }
    
    public ImplementsFromStackAndQueue(int size){
        list = new Object[size];
        maxStackSize = size;
    }

    @Override
    public boolean isEmpty() {
       return(topStack == 0);
    }

    @Override
    public boolean isFull() {
        return (topStack == maxStackSize);
    }

    @Override
    public void initializeValueStack() {
        for (int i = 0; i < maxStackSize; i++) {
            list[i] = null;
            topStack = 0;
        }
       
    }
    
    @Override
    public void push(Object newList){
        if(isFull()){           
            list[topStack] = newList;
            topStack++;
            
        }
    }

    @Override
    public Object peek() {
        if (isEmpty()) {
            return list[topStack - 1];
        }
        return list[topStack -1 ];
    }

    @Override
    public void pop(Object newList) {
        if (isEmpty()) {
           topStack--;
           list[topStack] = null;
        }
    }

    @Override
    public void enqueue(Object newList) {
        if(isFull()){           
            list[topStack] = newList;
            topStack++;
            
        }
    }

    @Override
    public void dequeue(Object newList) {
        if (isEmpty()) {
           list[topStack] = null;         
        }
    }


}
