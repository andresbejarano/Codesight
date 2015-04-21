/*
 * Codesight.java
 *
 * Desarrollado por: Andres Mauricio Bejarano Posada
 * Construida en: NetBeans IDE 5.5
 *
 * Esta clase es la principal en la aplicación pues es la que maneja la lógica
 * de analisis, transformación, comparación y medición del código copiado.
 *
 * La clase almacena la información en las siguientes referencias:
 *  -> Files: Vector donde se guardan los objetos File correspondiente a cada
 *     uno de los archivos leidos en la carpeta pasada por parámetro.
 *  -> Signatures: Vector de objetos de la clase Signature que contienen las
 *     secuencias de fichas (tokens) de los diferentes archivos.
 */

import java.util.*;

public class Codesight {
    
    private java.io.File[] Files;
    private Signature[] Signatures;
    
    /* Constructor de la clase. Recibe un vector con los archivos que van a ser
     * analizados.
     */
    public Codesight(java.io.File[] files){
        Files = files;
        Signatures = new Signature[files.length];
        for(int i = 0;i < files.length;i += 1)
            Signatures[i] = new Signature(files[i]);
    }
    
    /* Retorna una cadena que contiene todo el código de un archivo. Recibe por
     * parámetro el índice (posición) del archivo del cual solicita el código.
     */
    public String getSourceCode(int index){
        String sourcecode = "";
        try{
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(Files[index]));
            String line = null;
            int i = 0;
            while((line = reader.readLine()) != null){
                i += 1;
                sourcecode = sourcecode + i + "   " + line + "\n";
            }
            reader.close();
        }
        catch(Exception e){
            sourcecode = "Exception";
        }
        return sourcecode;
    }
    
    /* Retorna el nombre del archivo cuya posición se indica como parámetro.
     */
    public String getFileName(int index){
        return Files[index].getName();
    }
    
    /* Función que retorna en un vector los nombres de todos los archivos que
     * fueron leidos en el proceso de construcción de la clase.
     */
    public String[] getFilesName(){
        String names[] = new String[Files.length];
        for(int i = 0;i < Files.length;i++)
            names[i] = Files[i].getName();
        return names;
    }
    
    /* Función que retorna la referencia al objeto Signature del archivo cuya
     * posición es pasada por parámetro.
     */
    public Signature getSignature(int index){
        Signature sig = null;
        if(index < Signatures.length)
            sig = Signatures[index].copy();
        return sig;
    }
    
    /* Función que retorna la referencia al objeto Signature del archivo cuyo
     * nombre es pasado por parámetro.
     */
    public Signature getSignature(String name){
        Signature sig = null;
        int i = 0;
        while(i < Signatures.length && !name.equals(Signatures[i].getFileName()))
            i++;
        if(name.equals(Signatures[i].getFileName()))
            sig = Signatures[i].copy();
        return sig;
    }
    
    /* Función puente para comparar dos objetos Signature referenciados por
     * las posiciones dentro del vector. Tales posiciones se pasan por
     * parámetro al igual que el umbral de comparación.
     */
    public Match greedyStringTiling(int sig1, int sig2, int threshold){
        return greedyStringTiling(Signatures[sig1],Signatures[sig2],threshold);
    }
    
    /* Algoritmo de comparación de dos firmas (Singatures), las cuales son
     * pasadas por parámetro, al igual que el umbral de comparación. Este
     * procedimiento está basado en el algoritmo Greedy-String-Tiling y 
     * devuelve un objeto de la clase Match el cual contiene los resultados de
     * la comparación.
     */
    public Match greedyStringTiling(Signature sig1, Signature sig2, int threshold){
        Token[] A = sig1.getTokenData();
        Token[] B = sig2.getTokenData();
        int[][] matchdata;
        Match matches = new Match(sig1,sig2,A,B,threshold);
        int a, b, j, maxmatch;
        maxmatch = threshold;
        for(a = 0;a < sig1.getNumTokens();a++){
            for(b = 0;b < sig2.getNumTokens();b++){
                j = 0;
                while(((a + j) < sig1.getNumTokens()) && ((b + j) < sig2.getNumTokens()) && (A[a + j].getToken().equals(B[b + j].getToken())) && !A[a + j].getMark()  && !B[b + j].getMark())
                    j += 1;
                if(j > 0 && j >= maxmatch)
                    matches.add(a,b,j);
            }
        }
        if(matches.getSize() > 0){
            matches.Sort(3);
            matches.mark();
        }
        return matches;
    }
    
    /* Calcula el porcentaje de similitud entre dos firmas (Signatures), las
     * cuales se encuentran dentro del objeto de la clase Match que es pasado
     * por parámetro. Devuelve un número float que indica el porcentaje de
     * similitud entre los dos archivos previamente comparados.
     */
    public float getPercentMatch(Match match){
        float percent = 1;
        float coverage = 0;
        float total = 0;
        int copiedSequences = match.countChecked();
        Integer[][] data = match.getIntegerData();
        for(int i = 0;i < copiedSequences;i += 1)
            coverage += data[i][2].intValue();
        total = match.getTokensLength(1) + match.getTokensLength(2);
        percent = (2 * coverage) / total;
        return percent * 100;
    }
    
    /* Función que retorna una matriz con los porcentajes de similitud de todos
     * los archivos.
     */
    public float[][] compareAll(int Threshold){
        Match result = null;
        int i = 0, j = 0;
        float[][] table = new float[Signatures.length][Signatures.length];
        for(i = 0;i < Signatures.length;i += 1){
            for(j = i;j < Signatures.length;j += 1){
                if(i == j)
                    table[i][j] = -1;
                else{
                    result = greedyStringTiling(i,j,Threshold);
                    table[i][j] = round(getPercentMatch(result),2);
                    table[j][i] = round(getPercentMatch(result),2);
                }
            }
        }
        return table;
    }
    
    /* Función que retorna en un vector los nombres de los archivos que fueron
     * leidos en la carpeta durante el proceso de consrtucción del objeto.
     */
    public String[] getTitles(){
        String[] titles = new String[Signatures.length];
        for(int i = 0;i < Signatures.length;i += 1)
            titles[i] = String.valueOf(i);
        return titles;
    }
    
    /* Devuelve el número de firmas (Signatures) que tiene el objeto en el
     * arreglo Signatures.
     */
    public int getNumSignatures(){
        return Signatures.length;
    }
    
    /* Funcion que hace el redondeo de los valores con dos decimales despues de
     * la coma.
     */
    public float round(float percent, int places) {
        long factor = (long)Math.pow(10,places);
        double value = percent;
        value *= factor;
        long tmp = Math.round(value);
        return (float)tmp / factor;
    }
    
}
