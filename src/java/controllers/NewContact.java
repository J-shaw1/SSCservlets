package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.DatabaseUtils;

/**
 * Servlet for adding a new contact to the database
 *
 * @author joe
 */
public class NewContact extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Will add a contact to the database, using information from the
     * form
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();

        //If the session is new then send them back to the homepage
        RequestDispatcher rd;
        if (session.isNew()) {
            rd = request.getRequestDispatcher("index.html");
            try {
                session.invalidate();
                rd.forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }

        //Get the print writer so the text can be added to the page
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            //Try to add the cotact to the database
            DatabaseUtils.addContact(request.getSession(),
                    request.getParameter("addForename"),
                    request.getParameter("addSurname"),
                    request.getParameter("addEmail"));

            //Tell the user that it added the contact
            out.write("Contact added");

        } catch (SQLException ex) {
            //Tell user that it failed to add the contact
            out.write("Failed to add contact");
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
