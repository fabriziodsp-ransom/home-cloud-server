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

import com.home.cloud.server.abstracts.HomeCloudServlet;
import com.home.cloud.server.exceptions.FileIsNotDirectoryException;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fabrizio
 */
public class ShowDirectoryContents extends HomeCloudServlet {

    private ArrayList<String> folderPaths = new ArrayList<>();
    private ArrayList<String> filePaths = new ArrayList<>();
    private String path = null;

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
        path = request.getParameter("p");

        try {
            Path dir = Paths.get(path);

            if (dir.toFile().isFile()) {
                throw new FileIsNotDirectoryException(dir.toString() + " Reason: is file.");
            }

            Files.walk(dir, 1).forEach(pathToFile
                    -> filterPaths(pathToFile.toFile(), dir));

        } catch (InvalidPathException | NoSuchFileException e) {

            PrintWriter out = response.getWriter();
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-control", "max-age");
            response.setStatus(404);

            out.print("{\"error\": true, \"message\": \"Path not found: "
                    + e.getMessage().replace("\\", "/")
                    + "\"}");
            out.close();
        } catch (FileIsNotDirectoryException e) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-control", "max-age");
            response.setStatus(400);

            out.print("{\"error\": true, \"message\": \"Unable to navigate into: "
                    + e.getMessage().replace("\\", "/")
                    + "\"}");
            out.close();
        }
        processResponse(response);
    }

    /**
     * Filters files from directories
     *
     *
     * @param file      The file in question.
     * @param parent    The parent path of the file.
     */
    private void filterPaths(File file, Path parent) {
        if (file.isDirectory() && !file.equals(parent.toFile())) {
            this.folderPaths.add("\"" + file.getName() + "\"");
        }

        if (!file.isDirectory() && !file.equals(parent.toFile())) {
            this.filePaths.add("\"" + file.getName() + "\"");
        }
    }

    /**
     * Flushes ArrayLists so the next call doesn't contain the previous
     * information.
     */
    private void flushArrayLists() {
        this.folderPaths.clear();
        this.filePaths.clear();
    }

    @Override
    protected void processResponse(HttpServletResponse response) {

        try (PrintWriter out = response.getWriter();) {
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-control", "max-age");
            response.setStatus(200);
            
            out.print("{\"current_dir\": \"" + this.path + "\",\"files\":"
                    + this.filePaths
                    + ",\"folders\":"
                    + this.folderPaths + "}");
        } catch (IOException ex) {
            Logger.getLogger(ShowDirectoryContents.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            flushArrayLists();
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
        return "Shows the contents of the current directory in a JSON response";
    }// </editor-fold>

}
