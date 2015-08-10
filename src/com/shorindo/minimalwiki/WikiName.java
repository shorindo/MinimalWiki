package com.shorindo.minimalwiki;

public class WikiName {
    private String wikiName;

    public WikiName(String path) {
        if (path == null || "".equals(path) || "/".equals(path)) {
            wikiName = "index";
        } else {
            wikiName = path
                .replaceAll("^/+", "")
                .replaceAll("/+$", "")
                .replaceAll("//+", "/")
                .replaceAll("^\\s+", "")
                .replaceAll("\\s+$", "")
                .replaceAll("[ ]", "_");
        }
    }

    public String getName() {
        return wikiName.replaceAll("^.*?/([^/]+)$", "$1");
    }

    public String getFullName() {
        return wikiName;
    }
    
    public String toString() {
        return wikiName;
    }
}
