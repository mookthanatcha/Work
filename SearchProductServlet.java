/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
 * @author Chonticha Sae-jiw
 */
public class SearchProductServlet extends HttpServlet {

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
        String categoryId = request.getParameter("categoryId");
        boolean check = false;
        String[] StringArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String number = productName.substring(0,1);
        int cId = Integer.parseInt(categoryId);
        for (int i = 0; i < StringArray.length; i++) {
            if (number.equals(StringArray[i])) {
                check = true;
            }
        }
        if (check == true) {

            double price = Double.parseDouble(productName);
            CategoryJpaController categoryJpa = new CategoryJpaController(utx, emf);
            Category category = categoryJpa.findCategory(cId);

            ProductJpaController productJpa = new ProductJpaController(utx, emf);
            List<Product> Result = productJpa.SearchByPrice(price, category);
            request.setAttribute("products", Result);
        } else if (check == false) {
            CategoryJpaController categoryJpa = new CategoryJpaController(utx, emf);
            Category category = categoryJpa.findCategory(cId);

            ProductJpaController productJpa = new ProductJpaController(utx, emf);
            List<Product> Result = productJpa.Search(productName, category);
            System.out.println(cId);
            request.setAttribute("products", Result);

        }

//        getServletContext().getRequestDispatcher("/ProductFruitView.jsp").forward(request, response);
        switch (cId) {
            case 1:
                System.out.println("veg");
                getServletContext().getRequestDispatcher("/ProductVegView.jsp").forward(request, response);
                break;
            case 2:
                getServletContext().getRequestDispatcher("/ProductFruitView.jsp").forward(request, response);
                break;
            case 3:
                getServletContext().getRequestDispatcher("/ProductDriedFruitView.jsp").forward(request, response);
                break;
            default:
                getServletContext().getRequestDispatcher("/ProductJuiceView.jsp").forward(request, response);
                break;
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
