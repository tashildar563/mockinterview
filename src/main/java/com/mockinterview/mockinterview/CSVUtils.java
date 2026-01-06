package com.mockinterview.mockinterview;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    public static List<Object[]> readCsv(String path) throws FileNotFoundException {
        List<Object[]> list =new ArrayList<>();
        File file = new File(path);
        int count = 0;
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(count++>1){
                    list.add(line.split(",", 3));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }


    public static void createFile(String path) {
        try {
            File file = new File(path);

            File parentDirectory = file.getParentFile();
            if(parentDirectory!=null && !parentDirectory.exists()){
                boolean dirsCreated = parentDirectory.mkdirs();
                if(!dirsCreated){
                    System.out.println("failed to create Directories : " + parentDirectory.getAbsolutePath());
                }
            }
            if(!file.exists()){
                boolean fileCreated = file.createNewFile();
                if(fileCreated){
                    System.out.println("file created At: "+path);
                }else{
                    System.out.println("failed to create file: "+path);
                }
            }else{
                System.out.println("file Already exists at: "+path);
            }
        }catch (IOException e){
            throw new RuntimeException("Error while checking / creating file: "+path);
        }
    }
}

