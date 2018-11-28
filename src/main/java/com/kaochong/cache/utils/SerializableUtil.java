package com.kaochong.cache.utils;

import java.io.*;

public class SerializableUtil {

    public static String writeObject(Object o) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOut);
        objectOutputStream.writeObject(o);
        return byteOut.toString("ISO-8859-1");
    }

    public static Object readObject(String s) throws IOException, ClassNotFoundException {
        if (s == null) {
            return null;
        }
        ByteArrayInputStream byteIn = new ByteArrayInputStream(s.getBytes("ISO-8859-1"));
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return objIn.readObject();
    }
}
