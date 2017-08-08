package com.javaccy.config;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class CustomNamingStrategy extends ImprovedNamingStrategy {

    @Override
    public String classToTableName(String className) {
        String s = super.classToTableName(className);
        return s.startsWith("t_") ? s : "t_" + s;
    }

    @Override
    public String columnName(String columnName) {
        return super.columnName(columnName);
    }
}
