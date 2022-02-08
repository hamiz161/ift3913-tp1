package TP1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Users\\hamiz\\test\\test");
        //Scanner sc = new Scanner(file);

        System.out.println(paquet_LOC(file));
    }

    private static int classe_LOC(Scanner sc){
        int count = 0;
        String ligne;
        while (sc.hasNext()) {
            ligne = sc.nextLine();
            if(!ligne.equals("")){
                count++;
            }
        }
        return count;
    }

    private static int classe_CLOC(Scanner sc){
        int count = 0;
        boolean isComment = false;
        String ligne;
        while (sc.hasNext()) {
            ligne = sc.nextLine();
            if (ligne.contains("/*")){
                isComment = true;
            }
            if (ligne.contains("*/")){
                count++;
                isComment = false;
            }
            if(!ligne.equals("") && (ligne.contains("//") || isComment)){
                count++;
            }
        }
        return count;
    }

    private static int paquet_LOC(File file) throws FileNotFoundException{
        int count = 0;
        String path = file.getAbsolutePath() + "\\";
        if(file.list() == null){
           return 0;
        }
        for(String s : file.list()){
            File file2 = new File(path+s);
            if(file2.isFile()){
                if(s.contains(".java")){
                    Scanner sc = new Scanner(file2);
                    count += classe_LOC(sc);
                }
            }
            else{
                count += paquet_LOC(file2);
            }
        }

        return count;
    }
    private static int paquet_CLOC(File file) throws FileNotFoundException{
        int count = 0;
        String path = file.getAbsolutePath() + "\\";
        if(file.list() == null){
            return 0;
        }
        for(String s : file.list()){
            File file2 = new File(path+s);
            if(file2.isFile()){
                if(s.contains(".java")){
                    Scanner sc = new Scanner(file2);
                    count += classe_CLOC(sc);
                }
            }
            else{
                count += paquet_CLOC(file2);
            }
        }
        return count;
    }
}
