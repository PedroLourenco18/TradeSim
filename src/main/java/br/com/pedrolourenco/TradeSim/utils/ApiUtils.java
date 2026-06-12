package br.com.pedrolourenco.TradeSim.utils;

import org.springframework.stereotype.Component;

@Component
public class ApiUtils {
    public boolean hasAttribute(Class<?> clazz, String attribute){
        try {
            clazz.getDeclaredField(attribute);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
