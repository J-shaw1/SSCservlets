package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.EmailLogin;

/**
 * Sends a new email
 * @author joe
 */
public class NewEmail extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Will create a new email using the information in the form.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        RequestDispatcher rd;

        //If the request is new then send them to the login page
        if (session.isNew()) {
            rd = request.getRequestDispatcher("index.html");
            try {
                session.invalidate();
                rd.forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }

        //Get the Email login object 
        EmailLogin login = (EmailLogin) session.getAttribute("login");

        //Get all the information from the form
        String to = request.getParameter("to");
        String cc = request.getParameter("cc");
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");

        //Get the print writer for letting people know that it send/failed
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            login.sendEmail(to, cc, subject, body);
            //Let the user know that it send
            out.write("Email sent");

        } catch (MessagingException ex) {
            //Let the user know that it failed
            out.write("Email failed to send");
            ex.printStackTrace();
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
