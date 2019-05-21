package com.ten.aditum.back.pycontroller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PythonLocalTest {
    public static void main(String[] args) {
        String line = null;
        String[] arguments = new String[]{
                "D:\\Users\\shihaowang\\AppData\\Local\\Programs\\Python\\Python37\\python.exe",
                "D:\\Users\\shihaowang\\Desktop\\Aditum-Personas\\com.ten.aditum\\personas\\AccessTimeClusteringModel.py"};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = in.readLine()) != null) {
                String str = new String(line.getBytes(), StandardCharsets.UTF_8);
                System.out.println(str);
            }
            in.close();
            int re = process.waitFor();
            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
