/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fabri.home.cloud.server.resources;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author usuario
 */
public class ShowDirectoryContents extends HttpServlet {
    private ArrayList<String> folderPaths = new ArrayList<>();
    private ArrayList<String> filePaths = new ArrayList<>();

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
        response.setContentType("application/json;charset=UTF-8");
        String path = request.getParameter("p");
        
        try {
            Path dir = Paths.get(path);
            
            Files.walk(dir,1).forEach(pathToFile -> 
                    filterPaths(pathToFile.toFile(), dir));
            
        } catch (InvalidPathException | IOException e) {
            response.setStatus(404);
            PrintWriter out = response.getWriter();
            
            out.print("{\"error\": true, \"message\": \""
                    +e.getMessage().replace("\\", "/")
                    +"\"}");
            out.close();
            flushArrays();
        }
        
        
        try (PrintWriter out = response.getWriter()) {
            out.print("{\"current_dir\": \""+path+"\",\"files\":"
                    +this.filePaths+
                    ",\"folders\":"+
                    this.folderPaths+"}");
            flushArrays();
        }
    }
    /**
     * Filters if {@param file} is directory or file.
     * @param file the file in question.
     */
    private void filterPaths(File file, Path parent) {
        if (file.isDirectory() && !file.equals(parent.toFile())) {
            this.folderPaths.add("\""+file.getName()+"\"");
        }
        
        if (!file.isDirectory() && !file.equals(parent.toFile())) {
            this.filePaths.add("\""+file.getName()+"\"");
        }
    }
    private void flushArrays() {
        this.folderPaths.clear();
        this.filePaths.clear();
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Shows the contents of the current directory in a JSON response";
    }// </editor-fold>

}
