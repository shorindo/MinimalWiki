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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class SearchEngine {
    private static final Logger LOG = Logger.getLogger(SearchEngine.class);
    private ContentManager manager;
    
    public SearchEngine(ContentManager manager) {
        this.manager = manager;
    }
    public List<SearchResult> search(String searchText) {
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        for (File file : manager.listWikiFiles()) {
            try {
                WikiName wikiName = manager.file2WikiName(file);
                String wikiText = manager.getWikiText(wikiName);
                SearchResult result = new SearchResult(wikiName, wikiText);
                if (result.matches(searchText)) {
                    resultList.add(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
    public static class SearchResult {
        private WikiName wikiName;
        private String content;
        
        public SearchResult(WikiName wikiName, String content) {
            this.wikiName = wikiName;
            this.content = content;
        }
        public boolean matches(String searchText) {
            return content.toUpperCase().contains(searchText.toUpperCase());
        }
        public WikiName getWikiName() {
            return wikiName;
        }
        public void setWikiName(WikiName wikiName) {
            this.wikiName = wikiName;
        }
        public String getSummary() {
            return content; //TODO
        }
        private String escape(String text) {
            return text
                    .replaceAll("\\\\", "\\\\")
                    .replaceAll("\"", "\\\"")
                    .replaceAll("\r", "\\r")
                    .replaceAll("\n", "\\n");
        }
        public String toJSON() {
            return "{" +
                    "\"name\":\"" + escape(wikiName.getName()) + "\"," +
                    "\"fullName\":\"" + escape(wikiName.getFullName()) + "\"," +
                    "\"summary\":\"" + escape(getSummary()) + "\"" +
                    "}";
        }
    }
}
