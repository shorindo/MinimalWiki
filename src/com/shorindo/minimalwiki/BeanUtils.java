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

import java.lang.reflect.Method;

/**
 * 
 */
public class BeanUtils {
    public static Object getProperty(Object bean, String propertyName) {
        return null;
    }
    
    protected static Method getGetterMethod() {
        return null;
    }
    
    protected static Method getSetterMethod(Class<?> expect) {
        return null;
    }
    
    public static String toJSON(Object bean) {
        return null;
    }
    
    public static <T> T fromJSON(String json, Class<T> clazz) {
        return null;
    }
}
