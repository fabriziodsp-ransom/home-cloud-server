/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fabri.home.cloud.server.resources;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author usuario
 */
public class DownloadFile extends HttpServlet {

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
        
        try {
            File fileToDownload = new File((String) request.getParameter("p"));
            response.setContentType(Files.probeContentType(Paths.get(fileToDownload.getAbsolutePath())));
        
            response.setHeader("Content-disposition", "attachment; filename="+fileToDownload.getName());
            try (InputStream in = new FileInputStream(fileToDownload);
                    ServletOutputStream out = response.getOutputStream()) {
                
                byte[] buffer = new byte[(4*1024)];
                int numberBytesToRead;
                while ((numberBytesToRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, numberBytesToRead);
                }
            }
        } catch (NullPointerException | IOException e) {
            response.setContentType("application/json");
            response.setStatus(404);
            PrintWriter out = response.getWriter();
            
            out.print("{\"error\": true, \"message\": \""
                    +e.getMessage().replace("\\", "/")
                    +"\"}");
            out.close();
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
        return "Servlet used for downloading files at specified path.";
    }// </editor-fold>

}
