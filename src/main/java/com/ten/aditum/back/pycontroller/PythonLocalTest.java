package com.ten.aditum.back.pycontroller;

import com.ten.aditum.back.config.PythonConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * python调用测试
 */
public class PythonLocalTest {
    public static void main(String[] args) {
        String line;
        String[] arguments = new String[]{
                PythonConstants.PYTHON_PATH,
                PythonConstants.PTTHON_PROGRAM_BATH_PATH + "AccessTimeClusteringModel.py"};
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
