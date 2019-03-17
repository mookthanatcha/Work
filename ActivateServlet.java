/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import sit.jpa.project.controller.AccountJpaController;
import sit.jpa.project.controller.exceptions.RollbackFailureException;
import sit.jpa.project.model.Account;

/**
 *
 * @author Chonticha Sae-jiw
 */
public class ActivateServlet extends HttpServlet {
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
        String email = request.getParameter("email");
        String activateKey = request.getParameter("activateKey");
        
        if (email != null && email.trim().length() > 0) {
            AccountJpaController accountJPA = new AccountJpaController(utx, emf);
            Account account = accountJPA.findAccount(email);
            if (account != null) {
                String emailSendToJsp = account.getEmail();
                String activateSendToJsp = account.getActivateKey();
                request.setAttribute("email", emailSendToJsp);
                request.setAttribute("activate", activateSendToJsp);
                
                
            }
        }
        if (email != null && email.trim().length() > 0 && activateKey != null && activateKey.trim().length() > 0) {
            AccountJpaController accountJPA = new AccountJpaController(utx, emf);
            Account account = accountJPA.findAccount(email);
            if (account != null) {
                if (account.getActivateKey().equals(activateKey)) {
                    
                    try {
                        account.setActivateTimestamp(new Date());
                        accountJPA.edit(account);
                        System.out.println("Redirect to SuccessAactivsate");
                        getServletContext().getRequestDispatcher("/SuccessActivateView.jsp").forward(request, response);
//                        response.sendRedirect("UrbanFruits/SuccessActivate.jsp");
                        return;
                    } catch (RollbackFailureException ex) {
                        Logger.getLogger(ActivateServlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(ActivateServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
            }
            
        }
        getServletContext().getRequestDispatcher("/ActivateView.jsp").forward(request, response);
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
