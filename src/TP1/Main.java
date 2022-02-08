package TP1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("D:\\Téléchargement\\test");
        System.out.println(paquet_LOC(file));
    }

    private static int classe_LOC(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
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

    private static int classe_CLOC(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
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
        for(String s : Objects.requireNonNull(file.list())){
            File file2 = new File(path+s);
            if(file2.isFile()){
                if(s.contains(".java")){
                    count += classe_LOC(file2);
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
        for(String s : Objects.requireNonNull(file.list())){
            File file2 = new File(path+s);
            if(file2.isFile()){
                if(s.contains(".java")){
                    count += classe_CLOC(file2);
                }
            }
            else{
                count += paquet_CLOC(file2);
            }
        }
        return count;
    }

    private static float classe_DC(File file) throws FileNotFoundException{
        return (float) classe_CLOC(file)/classe_LOC(file);
    }

    private static float paquet_DC(File file) throws FileNotFoundException{
        return (float) paquet_CLOC(file)/paquet_LOC(file);
    }



}
