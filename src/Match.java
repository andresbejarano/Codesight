/*
 * Match.java
 *
 * Desarrollado por: Andres Mauricio Bejarano Posada
 * Construida en: NetBeans IDE 5.5
 *
 * Almacena los datos de la comparación de los archivos y se encarga de
 * mostrarlos de una forma ordenada dependiendo del metodo invocado.
 *
 * Esta clase almacena la información de la siguiente manera:
 *  -> Code1 y Code2: Almacenan los indices de inicio de las secuencias
 *     similares en ambos archivos respectivamente.
 *  -> Count: Almacena por indice la cantidad de fichas que tiene de longitud
 *     la secuencia que está en comun en ambos archivos.
 *  -> Checked: Vector de datos booleanos donde se indica si la secuencia ha
 *     sido marcada como valida o no para ser mostrada.
 *  -> Threshold: Almacena el valor del umbral de comparación.
 *  -> Sig1 y Sig2: Hacen referencias a las firmas (Signatures) de los archivos
 *     que son comparados.
 *  -> Color: Matriz donde se almacenan los colores con los que van a ser
 *     mostradas las lineas que aparecen como copiadas.
 */

import java.util.*;

public class Match {
    
    private Vector Code1, Code2, Count, Checked;
    private Token[] Tokens1,Tokens2;
    private Integer Threshold;
    private Signature Sig1, Sig2;
    public static final int[][] colors = {{143,231,37},{244,24,181},{76,148,192},{173,180,88},
                              {212,160,56},{246,22,70},{120,92,176},{165,215,53},
                              {165,121,103},{231,37,143},{24,181,244},{148,192,76},
                              {180,88,173},{160,56,212},{22,70,246},{92,176,120},
                              {215,53,165},{121,103,165},{231,143,37},{24,244,181},
                              {148,76,192},{180,173,88},{160,212,56},{22,246,70},
                              {92,120,176},{215,165,53},{121,165,103},{37,143,231},
                              {181,244,24},{192,76,148},{88,173,180},{56,212,160},
                              {70,246,22},{176,120,92},{53,165,215},{103,165,121}};
    
    /* Constructor de la clase. Recibe por parametos las firmas de los archivos
     * que son comparados, dos vectores que contienen las secuencias de fichas
     * de cada uno de los archivos, y el umbral de comparacion.
     */
    public Match(Signature sig1, Signature sig2, Token[] A, Token[] B,int threshold) {
        Sig1 = sig1;
        Sig2 = sig2;
        Tokens1 = A;
        Tokens2 = B;
        Threshold = new Integer(threshold);
        Code1 = new Vector(); //Indica el token de inicio del primer archivo
        Code2 = new Vector(); //Indica el token de inicio del segundo archivo
        Count = new Vector(); //Indica la cantidad de tokens que hay en similitud a partir de los tokens de inicio
        Checked = new Vector(); //Indica si el resultado es marcado como valido
    }
    
    /* Procedimiento que añade a los vectores las posiciones de inicio de una
     * secuencia que fue encontrada comun en ambos archivos. i indica la
     * posicion de inicio en el archivo 1, j indica la posicion de inicio en
     * el archivo 2 y k indica la longitud de la secuencia encontrada.
     */
    public void add(int i, int j, int k){
        Code1.add(new Integer(i));
        Code2.add(new Integer(j));
        Count.add(new Integer(k));
        Checked.add(Boolean.FALSE);
    }
    
    /* Funcion que retorna el tamaño de los vectores donde se almacenan los
     * datos de la comparacion.
     */
    public int getSize(){
        return Code1.size();
    }
    
    /* Funcion que devuelve una matriz de enteros con los datos de los vectores
     * que almacenan los resultados de la comparacion de los archivos.
     */
    public int[][] getData(){
        int[][] val = new int[Code1.size()][3];
        for(int i = 0;i < Code1.size();i++){
            val[i][0] = ((Integer)Code1.get(i)).intValue();
            val[i][1] = ((Integer)Code2.get(i)).intValue();
            val[i][2] = ((Integer)Count.get(i)).intValue();
        }
        return val;
    }
    
    /* Funcion que devuelve una matriz de Integer con los datos de los vectores
     * que almacenan los resultados de la comparacion de los archivos.
     */
    public Integer[][] getIntegerData(){
        Integer[][] val = new Integer[countChecked()][3];
        int j = 0;
        for(int i = 0;i < Code1.size();i++){
            if(((Boolean)Checked.get(i)).booleanValue()){
                val[j][0] = new Integer(((Integer)Code1.get(i)).intValue());
                val[j][1] = new Integer(((Integer)Code2.get(i)).intValue());
                val[j][2] = new Integer(((Integer)Count.get(i)).intValue());
                j += 1;
            }
        }
        return val;
    }
    
    /* Funcion que devuelve una matriz de Objects con los resultados de la
     * comparacion de los archivos. Si las secuencias fueron marcadas como
     * true entonces seran retornadas en esta matriz. El ultimo campo es un
     * espacio en blanco donde se almacenara el color correspondiene a la
     * secuencia copiada.
     */
    public Object[][] getObjectData(){
        Object[][] val = new Object[countChecked()][5];
        int j = 0;
        for(int i = 0;i < Code1.size();i++){
            if(((Boolean)Checked.get(i)).booleanValue()){
                val[j][0] = new Integer(j + 1);
                val[j][1] = new Integer(((Integer)Code1.get(i)).intValue());
                val[j][2] = new Integer(((Integer)Code2.get(i)).intValue());
                val[j][3] = new Integer(((Integer)Count.get(i)).intValue());
                val[j][4] = "";
                j += 1;
            }
        }
        return val;
    }
    
    /* Funcion que devuelve el numero de secuencias que fueron marcadas como
     * true.
     */
    public int countChecked(){
        int total = 0;
        for(int i = 0;i < Code1.size();i+=1){
            if(((Boolean)Checked.get(i)).booleanValue())
                total += 1;
        }
        return total;
    }
    
    /* Funcion que devuelve una matriz de Objects con la informacion de las
     * fichas que fueron encontradas como pertenecientes a una secuencia
     * similar en ambos archivos. Recibe por parametro el numero del archivo
     * del cual desea saber los datos de su secuencia de fichas.
     */
    public Object[][] getTokenData(int select){
        Object[][] data = null;
        switch(select){
            case 1:
                data = new Object[Tokens1.length][5];
                for(int i = 0;i < Tokens1.length;i += 1){
                    data[i][0] = new Integer(i);
                    data[i][1] = Tokens1[i].getToken();
                    data[i][2] = Tokens1[i].getWord();
                    data[i][3] = new Integer(Tokens1[i].getLine());
                    data[i][4] = new Boolean(Tokens1[i].getMark());
                }
            break;
            case 2:
                data = new Object[Tokens2.length][5];
                for(int i = 0;i < Tokens2.length;i += 1){
                    data[i][0] = new Integer(i);
                    data[i][1] = Tokens2[i].getToken();
                    data[i][2] = Tokens2[i].getWord();
                    data[i][3] = new Integer(Tokens2[i].getLine());
                    data[i][4] = new Boolean(Tokens2[i].getMark());
                }
            break;
        }
        return data;
    }
    
    /* Procedimiento que intercambia de la posicion i a la posicion j los
     * valores de los vectores Code1, Code2 y Count.
     */
    private void switchPosition(int i, int j){
        int a = ((Integer)Code1.get(i)).intValue();
        int b = ((Integer)Code2.get(i)).intValue();
        int c = ((Integer)Count.get(i)).intValue();
        Code1.set(i,new Integer(((Integer)Code1.get(j)).intValue()));
        Code2.set(i,new Integer(((Integer)Code2.get(j)).intValue()));
        Count.set(i,new Integer(((Integer)Count.get(j)).intValue()));
        Code1.set(j,new Integer(a));
        Code2.set(j,new Integer(b));
        Count.set(j,new Integer(c));
    }
    
    /* Funcion que organiza ascendentemente los vectores Code1, Code2 y Count.
     * El algoritmo que utiliza es Burbuja y el vector llave que dirige el
     * ordenamiento es indicado a traves de la variable type.
     */
    public void Sort(int type){
        int i = 0;
        int j = 0;
        for(i = 0;i < Count.size();i += 1){
            for(j = i + 1;j < Count.size();j += 1){
                switch(type){
                    case 1: //Code1
                        if(((Integer)Code1.get(j)).compareTo((Integer)Code1.get(i)) > 0)
                            switchPosition(i,j);
                    break;
                    case 2: //Code2
                        if(((Integer)Code2.get(j)).compareTo((Integer)Code2.get(i)) > 0)
                            switchPosition(i,j);
                    break;
                    case 3: //Count
                        if(((Integer)Count.get(j)).compareTo((Integer)Count.get(i)) > 0)
                            switchPosition(i,j);
                    break;
                }
            }
        }
    }
    
    /* Procedimiento que marca una secuencia de fichas dependiendo si se 
     * encuentran o no en una de las secuencias comunes encontradas en ambos
     * archivos.
     */
    public void mark(){
        int ini = 0;
        int a = 0;
        int b = 0;
        int count = 0;
        for(int i = 0;i < getSize();i+=1){
            a = ((Integer)Code1.get(i)).intValue();
            b = ((Integer)Code2.get(i)).intValue();
            count = ((Integer)Count.get(i)).intValue();
            ini = 0;
            while((ini < count) && !Tokens1[a + ini].getMark() && !Tokens2[b + ini].getMark())
                ini += 1;
            if(ini == count){ //Si todos los tokens no estan marcados
                for(ini = 0;ini < count;ini += 1){
                    Tokens1[a + ini].mark();
                    Tokens2[b + ini].mark();
                }
                Checked.set(i,Boolean.TRUE);
            }
        }
    }
    
    /* Devuelve la cantidad de fichas del archivo indicado por parametro.
     */
    public int getTokensLength(int id){
        int tokens = 0;
        switch(id){
            case 1: tokens = Tokens1.length; break;
            case 2: tokens = Tokens2.length; break;
        }
        return tokens;
    }
    
    /* Devuelve la referencia a un objeto Signature correspondiente a la firma
     * del archivo indicado por parametro.
     */
    public Signature getSignature(int num){
        Signature sig = null;
        switch(num){
            case 1: sig = Sig1; break;
            case 2: sig = Sig2; break;
        }
        return sig;
    }
    
}
