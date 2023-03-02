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
package com.home.cloud.server.abstracts;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Fabrizio
 */
public abstract class HomeCloudServlet extends HttpServlet {
    
    /**
     * Meant to contain the logic of every response an HTTP Servlet of this 
     * project sends.
     * 
     * @param       response    The HttpServletResponse object.     
     */
    protected void processResponse(HttpServletResponse response) {}
    
    /**
     * Meant to contain the logic of every response an HTTP Servlet of this 
     * project sends.
     * @param response      The HttpServletResponse object.
     * @param file          The file to send.
     * @throws IOException  In case the file is not found, or unable to be opened. 
     */
    protected void processResponse(HttpServletResponse response, File file) throws IOException {}
    
    /**
     * Meant to contain the logic of every response an HTTP Servlet of this 
     * project sends.
     * 
     * This method, like the previous ones, holds the same purpose. But this one
     * was designed to reduce the body of the implementation, by simply passing an
     * output stream and a file.
     * 
     * @param servletOutStream       The ServletOutputStream object. Meant for sending binary data
     * @param file                   The File object. Representing the file in question to send.
     * @throws IOException           In case File doesn't exists or can't be opened.
     */
    protected void processResponse(ServletOutputStream servletOutStream, File file) throws IOException {}
    
    
    /**
     * Meant to contain the logic of every response an HTTP Servlet of this 
     * project sends.
     * 
     * This method, like the previous ones, holds the same purpose. But this one
     * was designed to reduce the body of the implementation, by simply passing
     * the servlet Writer.
     * 
     * @param   servletWriter       The PrintWriter object of this Servlet.
     */
    protected void processResponse(PrintWriter servletWriter) {} 
}
