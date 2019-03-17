/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg60130500039.sec1.assignment;

/**
 *
 * @author ADMIN
 */
public interface Queue {
    boolean isEmpty();
    boolean isFull();
    void initializeValueStack();
    void push(Object newList);
    void pop(Object newList);
    Object peek();
}
