/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg60130500039.sec1.assignment;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface Stack {
    boolean isEmpty();
    boolean isFull();
    void initializeValueStack();
    void enqueue(Object newList);
    void dequeue(Object newList);
    Object peek();
}
