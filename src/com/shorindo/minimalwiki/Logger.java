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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Logger {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Map<Class<?>, Logger> loggerMap = new HashMap<Class<?>, Logger>();
    private Class<?> targetClass;

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = loggerMap.get(clazz);
        if (logger == null) {
            logger = new Logger(clazz);
            loggerMap.put(clazz, logger);
        }
        return logger;
    }
    
    private Logger(Class<?> clazz) {
        targetClass = clazz;
    }

    public void debug(String msg) {
        System.out.println(
                "[D]" +
                format.format(new Date()) + " " +
                targetClass.getSimpleName() + ":" +
                msg);
    }

    public void info(String msg) {
        System.out.println(
                "[I]" +
                format.format(new Date()) + " " +
                targetClass.getSimpleName() + ":" +
                msg);
    }
}