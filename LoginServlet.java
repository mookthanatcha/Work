/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import sit.jpa.project.controller.AccountJpaController;
import sit.jpa.project.controller.CustomerJpaController;
import sit.jpa.project.model.Account;
import sit.jpa.project.model.Customer;
import sit.project.model.Encription;

/**
 *
 * @author Chonticha Sae-jiw
 */
public class LoginServlet extends HttpServlet {
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
         HttpSession session = request.getSession(false);
        String email = request.getParameter("email");
        String password = request.getParameter("password");
    
        if(email != null && email.trim().length()>0 && password != null && password.trim().length()>0 ){
             AccountJpaController accountJPA = new AccountJpaController(utx, emf);
             Account accountSearch = accountJPA.findAccount(email);
             password = new Encription().Encription(password);
             
             if(accountSearch!=null){
                if(accountSearch.getEmail().equals(email)){
                    if(accountSearch.getPassword().equals(password)){
                        CustomerJpaController cusJpaCtrl = new CustomerJpaController(utx, emf);
                        Customer  cust = cusJpaCtrl.findByEmail(accountSearch.getEmail());
                        System.out.println(cust);
                        session.setAttribute("sesCustomer", cust);
                        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                        return;
                    }
                    
                    
                }
             }
             response.sendRedirect("LoginView.jsp");
             return;
        }
        getServletContext().getRequestDispatcher("/LoginView.jsp").forward(request, response);
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
