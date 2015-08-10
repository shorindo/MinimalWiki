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
package com.shorindo.minimalwiki.tag;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.shorindo.minimalwiki.Logger;

/**
 * 
 */
public class BeanWriteTag extends TagSupport {
    private static final Logger LOG = Logger.getLogger(BeanWriteTag.class);
    private String name;
    private String property;
    
    public int doStartTag() throws JspException {
        try {
            Object bean = pageContext.getAttribute(name, PageContext.REQUEST_SCOPE);
            if ("request".equals(name)) bean = pageContext.getRequest();
            if ("context".equals(name)) bean = pageContext.getServletContext();
            pageContext.getOut().print(getBeanValue(bean, property));
        } catch (Exception e) {
            throw new JspException("HelloTag: " + e.getMessage());
        }

        return SKIP_BODY;
    }

    public int doEndTag() {
        return EVAL_PAGE;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setProperty(String property) {
        this.property = property;
    }
    
    protected String getBeanValue(Object bean, String property) {
        if (bean instanceof String) {
            return bean.toString();
        } else {
            String getterName = "get" +
                    property.substring(0, 1).toUpperCase() +
                    property.substring(1);
            LOG.debug("method=" + getterName);
            try {
                Method method = bean.getClass().getMethod(getterName);
                return method.invoke(bean).toString();
            } catch (Exception e) {
                LOG.info(e.getMessage());
            }
        }
        return null;
    }
}
