/*
 * Copyright 2015 Shorindo, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shorindo.minimalwiki;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 */
public class MinimalWikiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        File base = new File(config.getServletContext().getRealPath("/"));
        String name = base.getName();
        File data = new File(base.getParentFile(), name + "-data");
        System.out.println(data.getAbsolutePath());
        if (!data.exists()) {
            data.mkdirs();
        }
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path.endsWith(".jsp")) {
            RequestDispatcher dispatcher = request.getServletContext().getNamedDispatcher("jsp");
            dispatcher.forward(request, response);
        } else if (path.matches("^.*?\\.[^.]+$")) {
            RequestDispatcher dispatcher = request.getServletContext().getNamedDispatcher("default");
            dispatcher.forward(request, response);
        } else {
            String parts[] = (new String(path.getBytes("ISO-8859-1"), "UTF-8")).split("/");
            request.setAttribute("title", parts[parts.length - 1]);
            request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(request, response);
        }
    }

}
