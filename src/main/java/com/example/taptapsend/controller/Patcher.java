package com.example.taptapsend.controller;

import java.lang.reflect.Field;

import org.springframework.stereotype.Component;

@Component
public class Patcher{

    public void patch(Object existing,Object incomplete) throws IllegalAccessException{
        Class<?> internClass = existing.getClass();
        Field[] internFields=internClass.getDeclaredFields();
        for(Field field : internFields){
            field.setAccessible(true);
            Object value = field.get(incomplete);
            if (value!=null) {
                field.set(existing, value);
            }
            field.setAccessible(false);
        }
    }
}
