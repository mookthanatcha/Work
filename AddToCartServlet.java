/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import sit.jpa.project.controller.ProductJpaController;
import sit.jpa.project.model.Product;
import sit.project.model.LineItem;
import sit.project.model.ShoppingCart;

/**
 *
 * @author Bud
 */
public class AddToCartServlet extends HttpServlet {

    @PersistenceUnit(unitName = "UrbanFruitsPU")
    EntityManagerFactory emf;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String proID = request.getParameter("proId");
        String quantity = request.getParameter("quantity");
        

        if (session.getAttribute("cart") == null) {
            ShoppingCart cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        if (proID != null) {
            ProductJpaController prodJPA = new ProductJpaController(utx, emf);
            Product product = prodJPA.findProduct(proID);
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            System.out.println(proID);
            System.out.println(quantity);
            for (int i = 0; i < Integer.parseInt(quantity); i++) {
                cart.Add(product);
            }
            
            request.setAttribute("msg", "successful");
            session.setAttribute("cart", cart);
        }

        getServletContext().getRequestDispatcher("/AddToCartView.jsp").forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
