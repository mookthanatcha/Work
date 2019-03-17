/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sit.jpa.project.controller.exceptions.RollbackFailureException;
import sit.jpa.project.model.Account;
import sit.jpa.project.model.Customer;
import sit.project.model.Encription;

/**
 *
 * @author Chonticha Sae-jiw
 */
public class RegisterServlet extends HttpServlet {
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
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String age = request.getParameter("age");
        String gender = request.getParameter("gender");
        HttpSession session = request.getSession(false);
        
        if(firstName != null && firstName.trim().length() > 0 &&
           lastName != null && lastName.trim().length() > 0 &&
           email != null && email.trim().length() > 0 &&
           password != null && password.trim().length() > 0 &&
           age != null && age.trim().length() > 0 &&
           gender != null && gender.trim().length() > 0){
            

            password = new Encription().Encription(password);
            AccountJpaController accountJPA = new AccountJpaController(utx, emf);
            Account account = new Account();
            account.setEmail(email);
            account.setPassword(password);
            account.setActivateKey(UUID.randomUUID().toString().replace("-", "").substring(0, 15));
            account.setDateRegis(new Date());

            CustomerJpaController custJPA = new CustomerJpaController(utx, emf);
            Customer cust = new Customer();
            int ages = Integer.parseInt(age);
            cust.setName(firstName);
            cust.setLastname(lastName);
            cust.setAge(ages);
            cust.setGender(gender);
            cust.setEmail(account);

            try {
                accountJPA.create(account);
                custJPA.create(cust);
                response.sendRedirect("Activate");
                return;
            } catch (RollbackFailureException ex) {
                Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }
        getServletContext().getRequestDispatcher("/RegisterView.jsp").forward(request, response);

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
