package com.ten.aditum.back.pycontroller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonLocalTest {
    public static void main(String[] args) {
        String line = null;
        String[] arguments = new String[]{"python", "D:\\Users\\shihaowang\\Desktop\\Aditum-Personas\\com.ten.aditum\\personas\\AccessTimeClusteringModel.py"};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            int re = process.waitFor();
            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
