package TP1;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static String classes = "chemin, class, classe_LOC, classe_CLOC, classe_DC \n";
    private static String paquets = "chemin, paquet, paquet_LOC, paquet_CLOC,  \n" ;

    public static void main(String[] args) throws IOException {
//        File file = new File("D:\\Téléchargement\\test");
//        System.out.println(paquet_LOC(file));


        createCSV("classes.csv",classes);
        createCSV("paquets.csv",paquets);



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


    private static void createCSV(String nameFile,String data) throws IOException {

        BufferedWriter bw = new BufferedWriter((new FileWriter(nameFile,true)));
        bw.write(data);


//        StringBuilder sb =new StringBuilder();
//        sb.append(data);
//        FileWriter file = new FileWriter(nameFile);
//        file.write(sb.toString());

    }



}
