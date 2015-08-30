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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 */
@XmlRootElement(name="sitemap")
public class SiteMap {
    private List<Page> pageList = new ArrayList<Page>();
    
    @XmlElement(name="page")
    public List<Page> getPageList() {
        return pageList;
    }

    public void setPageList(List<Page> pageList) {
        this.pageList = pageList;
    }

    public static class Page {
        private String name;
        private long size;
        private Date updateTime;
        
        public Page(String name, long size, Date updateTime) {
            this.name = name;
            this.size = size;
            this.updateTime = updateTime;
        }

        @XmlElement(name="name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        @XmlElement(name="size")
        public long getSize() {
            return size;
        }
        
        public void setSize(long size) {
            this.size = size;
        }
        
        @XmlElement(name="updateTime")
        public Date getUpdateTime() {
            return updateTime;
        }
        
        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }
    }
}
