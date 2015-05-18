/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import admin.Admini;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import js.server2;

/**
 *
 * @author mukherjee
 */
@WebServlet(name = "administrator", urlPatterns = {"/administrator"})
public class administrator extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try {
            /* TODO output your page here. You may use following sample code. */
            Admini AD = new Admini();

            if (request.getParameter("log") != null) {
                AD.getLog();
            } else if (request.getParameter("clearAlllog") != null) {
                AD.ClearAllLog();
            }  else if (request.getParameter("clearCurlog") != null) {
                AD.ClearCurlog();
            }else if (request.getParameter("filedel") != null) {
                AD.deleteChains(request.getParameter("filedel"));
            }else if (request.getParameter("filedelself") != null) {
                AD.deleteChainsSelf(request.getParameter("filedelself"));
            } else if (request.getParameter("restart") != null) {
                server2 SS = new server2();
                SS.restart();
            } else if (request.getParameter("ip") != null) {
                AD.DeloneIP();
            } else if (request.getParameter("start_server") != null) {
               AD.Start_server();
            } else if (request.getParameter("stop_server") != null) {
               AD.Stop_server();
            }

            response.sendRedirect("admin.jsp");

        } finally {
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
