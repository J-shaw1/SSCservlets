package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.ContactDetail;
import model.DatabaseUtils;

/**
 * Searches for a contact in the address book
 * @author joe
 */
public class SearchContact extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Will search the database for a contact
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Check that the session is  not new
        HttpSession session = request.getSession();
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

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<h1>Search Results</h1>");

            //Get the reqults from the server
            ArrayList<ContactDetail> results = DatabaseUtils.searchContact(request.getParameter("searchForename"),
                    request.getParameter("searchSurname"), session);

            //Give out start of table
            out.println("<table border=\"1px\">");
            out.println("<tr>");
            out.println("<td>Forename</td>");
            out.println("<td>Surname</td>");
            out.println("<td>Email address</td>");
            out.println("</tr>");

            //Give create a new row for each contact that it found
            for (ContactDetail result : results) {
                out.println("<tr>");
                out.println("<td>" + result.getForename() + "</td>");
                out.println("<td>" + result.getSurname() + "</td>");
                out.println("<td>" + result.getEmail() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

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
