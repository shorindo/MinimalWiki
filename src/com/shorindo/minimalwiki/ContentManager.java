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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXB;

import com.shorindo.minimalwiki.SiteMap.Page;

/**
 * 
 */
public class ContentManager {
    private static Logger LOG = Logger.getLogger(ContentManager.class);
    private File root;

    public ContentManager(File root) {
        this.root = root;
    }
    
    public void putWikiText(WikiName wikiName, String wikiText) throws IOException {
        validatePath(wikiName.getFullName());
        File wikiFile = getWikiFile(wikiName.getFullName());
        if (!wikiFile.exists()) {
            wikiFile.getParentFile().mkdirs();
        }
        Writer writer = new OutputStreamWriter(new FileOutputStream(wikiFile), "UTF-8");
        writer.write(wikiText);
        writer.close();
        writer = new OutputStreamWriter(new FileOutputStream(getWikiHistory(wikiName.getFullName())), "UTF-8");
        writer.write(wikiText);
        writer.close();
    }
    
    public String getWikiText(WikiName wikiName) throws IOException {
        validatePath(wikiName.getFullName());
        File wikiFile = getWikiFile(wikiName.getFullName());
        if (wikiFile.exists()) {
            StringBuffer sb = new StringBuffer();
            Reader reader = new InputStreamReader(new FileInputStream(wikiFile));
            int len = 0;
            char[] buff = new char[2048];
            while ((len = reader.read(buff)) > 0) {
                sb.append(buff, 0, len);
            }
            reader.close();
            return sb.toString();
        } else {
            return null;
        }
    }
    
    public SiteMap getSiteMap() throws IOException {
        File file = new File(root, "sitemap.xml");
        return JAXB.unmarshal(new FileInputStream(file), SiteMap.class);
    }

    public SiteMap createSiteMap() throws IOException{
        SiteMap siteMap = new SiteMap();
        for (File file : listWikiFiles()) {
            Page page = new Page(
                    file2WikiName(file).getFullName(),
                    file.length(),
                    new Date(file.lastModified())
                    );
            siteMap.getPageList().add(page);
        }
        Collections.sort(siteMap.getPageList(), new Comparator<Page>() {
            public int compare(Page o1, Page o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        File file = new File(root, "sitemap.xml");
        JAXB.marshal(siteMap, new FileOutputStream(file));
        return siteMap;
    }

    protected String getWikiName(String uri) {
        if (uri == null || "".equals(uri) || "/".equals(uri)) {
            return "start";
        } else {
            uri = uri.replaceAll("^/(.*)", "$1")
                    .replaceAll("^(.*?)/$", "$1")
                    .replaceAll("//+", "/");
            String parts[] = uri.split("/");
            return parts[parts.length - 1];
        }
    }
    
    protected String normalizeWikiName(String wikiName) {
        return wikiName
                .replaceAll("^/", "")
                .replaceAll("/$", "");
    }

    protected File getWikiFile(String wikiName) throws IOException {
        File textFile = new File(root.getAbsolutePath());
        wikiName = normalizeWikiName(wikiName) + ".txt";
        for (String name : wikiName.split("/")) {
            textFile = new File(textFile, URLEncoder.encode(name, "UTF-8"));
        }
        LOG.debug("getWikiFile(" + wikiName + "," + textFile.getAbsolutePath() + ")");
        return textFile;
    }
    
    protected File getWikiHistory(String wikiName) throws IOException {
        File histDir = new File(root.getAbsolutePath());
        String histName = normalizeWikiName(wikiName) + ".hist";
        for (String name : histName.split("/")) {
            histDir = new File(histDir, URLEncoder.encode(name, "UTF-8"));
        }
        if (!histDir.exists()) {
            histDir.mkdirs();
        }
        File histFile = new File(histDir, (new Date()).getTime() + ".txt");
        return histFile;
    }
    
    protected void validatePath(String uri) throws IOException {
        File curr = new File(root, uri);
        while (curr != null) {
            if (root.equals(curr)) {
                return;
            }
            curr = curr.getParentFile();
        }
        throw new IOException(uri + " is not valid path.");
    }

    public List<File> listWikiFiles() {
        return listWikiFiles(root);
    }

    private List<File> listWikiFiles(File base) {
        List<File> fileList = new ArrayList<File>();
        for (File file : base.listFiles()) {
            if (file.isDirectory() && !file.getName().endsWith(".hist")) {
                fileList.addAll(listWikiFiles(file));
            } else if (file.getName().endsWith(".txt")) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    
    public WikiName file2WikiName(File file) throws IOException {
        File curr = file;
        String result = "";
        while (curr != null && !root.equals(curr)) {
            String name = URLDecoder.decode(curr.getName(), "UTF-8");
            if (curr.isFile()) {
                result = name.replaceAll("\\.txt$", "");
            } else {
                result = name + "/" + result;
            }
            curr = curr.getParentFile();
        }
        LOG.debug("file2WikiName():" + result.toString());
        return new WikiName(result.toString());
    }
}
