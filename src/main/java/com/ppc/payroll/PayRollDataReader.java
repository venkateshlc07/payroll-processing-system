package com.ppc.payroll;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PayRollDataReader {
    private String filePath;
    public PayRollDataReader(String filePath){
        this.filePath = filePath;
    }

    public List<String> read(){
        InputStream inputStream = PayrollApplication.class
                .getClassLoader()
                .getResourceAsStream(filePath);

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }
}
