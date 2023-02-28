/*
 * The MIT License
 *
 * Copyright 2023 Fabrizio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.home.cloud.server.endpoints;

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
import com.home.cloud.server.exceptions.FileIsDirectoryException;

/**
 *
 * @author Fabrizio
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
        File fileToDownload = new File(request.getParameter("p"));
        if (fileToDownload.isDirectory()) {
            response.setStatus(400);
            PrintWriter servletOutput = response.getWriter();
            response.setContentType("application/json");

            servletOutput.print("{\"error\": true, \"message\": \""
                    + fileToDownload.getAbsolutePath() + " is a folder."
                    + "\"}");
            servletOutput.close();
        }
        try (ServletOutputStream servletOutputStream = response.getOutputStream(); InputStream in = new FileInputStream(fileToDownload);) {

            response.setContentType(Files.probeContentType(Paths.get(fileToDownload.getAbsolutePath())));

            response.setHeader("Content-disposition", "attachment; filename=" + fileToDownload.getName());

            response.setStatus(200);

            byte[] buffer = new byte[(4 * 1024)];
            int numberBytesToRead;
            while ((numberBytesToRead = in.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, numberBytesToRead);
            }
        } catch (NullPointerException e) {
            PrintWriter servletOutput = response.getWriter();
            response.setContentType("application/json");

            servletOutput.print("{\"error\": true, \"message\": \""
                    + e.getCause().getMessage()
                    + "\"}");
            servletOutput.close();
        } catch (IOException e) {
            PrintWriter servletOutput = response.getWriter();
            response.setContentType("application/json");
            response.setStatus(500);

            servletOutput.print("{\"error\": true, \"message\": \""
                    + e.getMessage().replace("\\", "/")
                    + "\"}");
            servletOutput.close();
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet used for downloading files at aspecified path.";
    }// </editor-fold>

}
