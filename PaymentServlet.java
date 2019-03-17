/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
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
import sit.jpa.project.controller.CustomerJpaController;
import sit.jpa.project.controller.HistoryJpaController;

import sit.jpa.project.controller.ProductJpaController;
import sit.jpa.project.controller.exceptions.RollbackFailureException;
import sit.jpa.project.model.Customer;
import sit.jpa.project.model.History;
import sit.jpa.project.model.Product;
import sit.project.model.LineItem;
import sit.project.model.ShoppingCart;

/**
 *
 * @author Bud
 */
public class PaymentServlet extends HttpServlet {

    @PersistenceUnit(unitName = "UrbanFruitsPU")
    EntityManagerFactory emf;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String idCard = request.getParameter("idCard");
//        String[] productNo = request.getParameterValues("productNo");

        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart!=null) {
            List<LineItem> lines = cart.getLineItem();
            if (cart != null) {
                Customer customer = (Customer) session.getAttribute("sesCustomer");
                System.out.println("---------------------------------------------------"+customer.getName());
                CustomerJpaController custJpaCtrl = new CustomerJpaController(utx, emf);
                Customer cust = custJpaCtrl.findCustomer(customer.getCustId());
                System.out.println("cust----------------------------------------------------------"+cust.getCustId());
          
                
                HistoryJpaController hisJPA = new HistoryJpaController(utx, emf);
                
                for (LineItem line : lines) {
                    History his = new History();
                    his.setCustId(cust);
                    his.setPrice(line.getPrice());
                    his.setQuantity(line.getQuantity());
                    his.setTimeStamp(new Date());
                    his.setTotalPrice(cart.getTotalPrice());
                    his.setProductId(line.getProduct());
                    
                    
                    List<History> hisss = hisJPA.findHistoryEntities();
                    int count = hisss.size() + 1;
                    his.setHistoryId(count);
                    
                    try {
                        
                        hisJPA.create(his);
                        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        
                    } catch (RollbackFailureException ex) {
                        Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
                session.removeAttribute("cart");
                response.sendRedirect("PaymentView.jsp");
                return;
            }
        }
        response.sendRedirect("index.jsp");
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
