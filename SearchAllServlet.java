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
import javax.transaction.UserTransaction;
import sit.jpa.project.controller.CategoryJpaController;
import sit.jpa.project.controller.ProductJpaController;
import sit.jpa.project.model.Category;
import sit.jpa.project.model.Product;

/**
 *
 * @author ADMIN
 */
public class SearchAllServlet extends HttpServlet {

    @PersistenceUnit(unitName = "UrbanFruitsPU")
    EntityManagerFactory emf;
    @Resource
    UserTransaction utx;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productName = request.getParameter("productName");
        boolean check = false;
        String[] StringArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String number = productName.substring(0, 1);

        for (int i = 0; i < StringArray.length; i++) {
            if (number.equals(StringArray[i])) {
                check = true;
            }
        }
        if (check == true) {
            double price = Double.parseDouble(productName);
            ProductJpaController productJpa = new ProductJpaController(utx, emf);
            List<Product> Result = productJpa.SearchAllByPrice(price);
            request.setAttribute("products", Result);
            getServletContext().getRequestDispatcher("/ShowAllView.jsp").forward(request, response);
        } else if (check == false) {
            ProductJpaController productJpa = new ProductJpaController(utx, emf);
            List<Product> Result = productJpa.findByProductName(productName);
            request.setAttribute("products", Result);
            getServletContext().getRequestDispatcher("/ShowAllView.jsp").forward(request, response);

        }
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
