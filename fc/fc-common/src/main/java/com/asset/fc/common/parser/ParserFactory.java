package com.asset.fc.common.parser;

import com.asset.fc.common.annotation.Parser;
import com.asset.fc.common.model.Job;
import java.io.File;
import java.util.HashMap;
import org.reflections.Reflections;

public class ParserFactory {

    private static HashMap<String, Parse> parsemap = new HashMap();

    public static Job Parse(File file) throws Exception {

        String fileExtention = file.getName().substring(file.getName().lastIndexOf(".") + 1); // get the extention of the file after the dot
        //search in map for that extention
        Parse p = parsemap.get(fileExtention);
        if (p == null) { // if this extenetion is not found throw an excption and get the name of that extention
            throw new Exception("This extention is not supported" + file.getName().substring(file.getName().lastIndexOf(".") + 1));
        }
        System.out.println("--------------------------------------------------------------");
        System.out.println(file + " is Parsed Sucessfully");
        System.out.println("--------------------------------------------------------------");
        return p.Prase(file); // parse the file depending on the detected extenetion.
    }

    public static Job Parse(String body, String ext) throws Exception {
        Parse p = parsemap.get(ext);
        if (p == null) {
            throw new Exception("This extention is not supported");
        }

        return p.Prase(body, ext); //parse the string accroding to the given extenstion

    }

    public static void Init() throws Exception {
        try {
            //get all the classes that are annotated with parser
            //Parse.class.isInstance(parseclass)
            Reflections ref = new Reflections("com.asset.fc.common.parser");
            for (Class<?> parseclass : ref.getTypesAnnotatedWith(Parser.class)) {
                Parser parser = parseclass.getAnnotation(Parser.class); // get the annotation of the class
                System.out.printf("Found class: %s, with meta name: %s%n", parseclass.getSimpleName(), parser.type()); // simple name -- name of the class, type= type of the class

                // check wheater the class implements the interface or not
                if (Parse.class.isAssignableFrom(parseclass)) {
                    //map the key(annotation type) to the value(object of the class) in the hashmap
                    parsemap.put(parser.type(), (com.asset.fc.common.parser.Parse) parseclass.newInstance());
                } else {
                    throw new Exception("File is not Found");
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("Exception is caught in the intilization function");
        }

    }
}
