package TP1;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static String classes = "chemin, class, classe_LOC, classe_CLOC, classe_DC \n";
    private static String paquets = "chemin, paquet, paquet_LOC, paquet_CLOC paquet_DC  \n" ;

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\hamiz\\IdeaProjects\\ift3913-tp1\\test\\fold2\\3ligne.java");
        File file2 = new File("C:\\Users\\hamiz\\IdeaProjects\\ift3913-tp1\\test\\fold2\\Fold1\\1ligne.java");
//        File file3 = new File("C:\\Users\\hamiz\\IdeaProjects\\ift3913-tp1\\test\\fold1\\2ligne.java");
//        System.out.println(classe_LOC(file));
//        System.out.println(classe_CLOC(file));



        createCSV("classes.csv",classes);
        //createCSV("paquets.csv",paquets);

        writeClassInCSV("classes.csv",file);
        writeClassInCSV("classes.csv",file2);
       //writeClassInCSV("classes.csv",file3);


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


    private static void createCSV(String nameFile,String data)  {

        try {

            FileWriter fw = new FileWriter(nameFile);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.print(data);
            pw.flush();
            pw.close();
        }
        catch(Exception e){
            System.out.println("Chemin de sauvegarde non specifié");
        }
    }

    private static void writeClassInCSV(String nameFile,File file){

        final int lengthExtention = 5;

        try{

            FileWriter fw = new FileWriter(nameFile,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            // recuperer les noms des classes

            String path = file.getPath();
            String idStr = path.substring(path.lastIndexOf('\\') + 1);
            idStr = idStr.substring(0,idStr.length()-lengthExtention);


            pw.println(file+","+ idStr+","+classe_LOC(file)+","+classe_CLOC(file)+","+ classe_DC(file));

            pw.flush();
            pw.close();



        }  catch (IOException e) {
            e.printStackTrace();
        }


    }



}
