package com.wolfpeng.spring;

import com.wolfpeng.core.Application;
import com.wolfpeng.core.util.Utils;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;
import java.lang.reflect.Field;

public class Main {
    public static String name = "name";
    public static String name1 = "name1";

    public String name2 = "name2";


    public static Object reflect(String fieldName) {
        Class cls = Main.class;
        try {
            Field field = cls.getDeclaredField(fieldName);
            return field.get(cls);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {

        reflect(name);

        if (args.length < 1) {
            Utils.log("main arg error!");
            return;
        }
        FileSystemXmlApplicationContext applicationContext = null;
        try {

            applicationContext = new FileSystemXmlApplicationContext(args[0]);
        } catch (Exception e) {
            Utils.log(e.toString());
        }

        Application application = applicationContext.getBean(Application.class);

        application.onApplicationStart();
        application.onApplicationExit();

        applicationContext.destroy();
    }
}
