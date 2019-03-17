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
public class TestStackAndQueue {

    public static void main(String[] args) {
        ImplementsFromStackAndQueue s1 = new ImplementsFromStackAndQueue();
        s1.initializeValueStack();

        try {
            s1.push("Mook");
            s1.push(20.5);            
            System.out.println("--------"+s1.peek());
            s1.enqueue("Mook");
            System.out.println("---------" + s1.peek());
        } catch (Exception E) {
        }


    }
}
