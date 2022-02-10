package TP1;

import java.io.*;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private final static String classes = "chemin, class, classe_LOC, classe_CLOC, classe_DC, WMC, classe_BC \n";
    private final static String paquets = "chemin, paquet, paquet_LOC, paquet_CLOC, paquet_DC, WCP, paquet_BC \n" ;

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\Téléchargement\\testDemo7.java");
        File file2 = new File("D:\\Téléchargement\\test\\fold2\\Fold1\\1ligne.java");
        File dir = new File("D:\\Téléchargement\\jfreechart-master");

        //System.out.println(WCP(dir));

        createCSV("classes.csv",classes);
        createCSV("paquets.csv",paquets);


        CSVComplet("classes.csv","paquets.csv",dir);


    }

    /**
     *
     * @param file path de classe java
     * @return int ; nombre de lignes de code d'une classe
     * @throws FileNotFoundException
     */
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

    /**
     *
     * @param file  path de classe java
     * @return int : nombre de lignes de code d'une classe qui contiennent des commentaires
     * @throws FileNotFoundException
     */
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

    /**
     *
     * @param file paquet path
     * @return int : nombre de lignes de code d’un paquet (java package) -- la somme des LOC de ses classes
     * @throws FileNotFoundException
     */
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

    /**
     *
     * @param file  paquet path
     * @return int :nombre de lignes de code d’un paquet qui contiennent des commentaires
     * @throws FileNotFoundException
     */
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

    /**
     *
     * @param file  path de classe java
     * @return float :densité de commentaires pour une classe : classe_DC = classe_CLOC / classe_LOC
     * @throws FileNotFoundException
     */
    private static float classe_DC(File file) throws FileNotFoundException{
        return (float) classe_CLOC(file)/classe_LOC(file);
    }

    /**
     *
     * @param file paquet path de classe java
     * @return float :densité de commentaires pour un paquet : paquet_DC = paquet_CLOC / paquet_LOC
     * @throws FileNotFoundException
     */
    private static float paquet_DC(File file) throws FileNotFoundException{
        return (float) paquet_CLOC(file)/paquet_LOC(file);
    }

    /**
     * creation dn fichier CSV avac la premiere ligne  qui contient => chemin|classe|classe_LOC...
      * @param nameFile nom de fichies CSV ==> classes.CSV
     * @param data la pemiere ligne "chemin, class, classe_LOC, classe_CLOC, classe_DC \n";
     */
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

    /**
     *
     * @param nameFile le fichier à modifier
     * @param file  path de classe java
     */
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

            pw.println(file+","+ idStr+","+classe_LOC(file)+","+classe_CLOC(file)+","+ classe_DC(file)+","+WMC(file)+","+classe_BC(file));

            pw.flush();
            pw.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param nameFile le fichier à modifier
     * @param file path de Paquet java
     */
    private static void writePaquetInCSV(String nameFile,File file){
        try{
            FileWriter fw = new FileWriter(nameFile,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            // recuperer les noms des classes

            String path = file.getPath();
            String idStr = path.substring(path.lastIndexOf('\\') + 1);

            pw.println(file+","+ idStr+","+paquet_LOC(file)+","+paquet_CLOC(file)+","+ paquet_DC(file)+","+WCP(file)+","+paquet_BC(file));

            pw.flush();
            pw.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void CSVComplet(String fichierClasses, String fichierPaquets, File file){

        for(String s : Objects.requireNonNull(file.list())){
            File file2 = new File(file.getAbsolutePath()+"\\"+s);
            if(file2.isFile()){
                if(s.contains(".java")){
                    writeClassInCSV(fichierClasses,file2);
                }
            }
            else{
                writePaquetInCSV(fichierPaquets, file2);
                CSVComplet(fichierClasses, fichierPaquets, file2);
            }
        }
    }

    private static int WMC(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
        int count = 0;
        String ligne;
        while (sc.hasNext()) {
            ligne = sc.nextLine().toLowerCase(Locale.ROOT);
            if(ligne.contains("if")){
                count++;
            }
            if(ligne.contains("case")){
                count++;
            }
            if(ligne.contains("while")){
                count++;
            }
            if(ligne.contains("for")){
                count++;
            }
            if(ligne.contains("catch")){
                count++;
            }
        }
        return count + 1;

    }

    private static int WCP(File file) throws FileNotFoundException {
        int count = 0;
        for(String s : Objects.requireNonNull(file.list())){
            File file2 = new File(file.getAbsolutePath()+"\\"+s);
            if(file2.isFile()){
                if(s.contains(".java")){
                    count += WMC(file2);
                }
            }
            else{
                count += WCP(file2);
            }
        }
        return count;
    }

    /**
     *
     * @param file path de classe java
     * @return degré selon lequel une classe est bien commentée classe_BC = classe_DC / WMC
     * @throws FileNotFoundException
     */

    private static float classe_BC(File file) throws FileNotFoundException{
        return (float) classe_DC(file)/WMC(file);
    }


    /**
     *
     * @param file path de paquet
     * @return degré selon lequel un paquet est bien commentée paquet_BC = paquet_DC / WCP
     * @throws FileNotFoundException
     */

    private static float paquet_BC(File file) throws FileNotFoundException{
        return (float) paquet_DC(file)/WCP(file);
    }




}
