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
    private static final Logger LOG = Logger.getLogger(MinimalWikiServlet.class);
    private static ContentManager contentManager;
    File dataPath;

    @Override
    public void init(ServletConfig config) throws ServletException {
        File base = new File(config.getServletContext().getRealPath("/"));
        LOG.debug("base=" + base.getAbsolutePath());
        String name = base.getName();
        dataPath = new File(base.getParentFile(), name + "-data");
        if (!dataPath.exists()) {
            dataPath.mkdirs();
        }
        contentManager = new ContentManager(dataPath);
        try {
            contentManager.createSiteMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path.endsWith(".jsp")) {
            RequestDispatcher dispatcher = request.getServletContext().getNamedDispatcher("jsp");
            dispatcher.forward(request, response);
        } else if (path.matches("^.*?\\.[^.]+$")) {
            RequestDispatcher dispatcher = request.getServletContext().getNamedDispatcher("default");
            dispatcher.forward(request, response);
        } else {
            WikiName wikiName = new WikiName(new String(path.getBytes("ISO-8859-1"), "UTF-8"));
            LOG.debug("doGet(" + wikiName + ")");
            request.setAttribute("title", "index".equals(wikiName.getFullName()) ?
                    getServletContext().getServletContextName() :
                    wikiName.getName());
            String wikiText = contentManager.getWikiText(wikiName);
            if (wikiText != null) {
                request.setAttribute("active", "tab-view");
                request.setAttribute("wikiText", wikiText);
            } else {
                request.setAttribute("active", "tab-edit");
                request.setAttribute("wikiText", "");
            }
            request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        String method = request.getParameter("method");
        WikiName wikiName = new WikiName(new String(path.getBytes("ISO-8859-1"), "UTF-8"));
        LOG.debug("doPost(" + path + "," + method + ")");
        if ("save".equals(method)) {
            contentManager.putWikiText(wikiName, request.getParameter("wikiText"));
        }
    }

}
