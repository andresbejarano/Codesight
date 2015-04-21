/*
 * Signature.java
 *
 * Desarrollado por: Andres Mauricio Bejarano Posada
 * Construida en: NetBeans IDE 5.5
 *
 * Esta clase se encarga de definir una serie de tokens dependiendo del código
 * fuente que se le ingrese. Se denomina Signature (firma) ya que es única por
 * código, lo que significa que el orden de la secuencia que se genera es único
 * por archivo, a menos que existan otros códigos exactamente iguales.
 *
 * Esta clase almacena la información de la siguiente forma:
 * -> file: Objeto de la clase File que hace referencia al archivo que es
 *    analizado.
 * -> totalLines: Indica el número total de líneas que tiene el archivo, esto
 *    incluye líneas en blanco y comentarios.
 * -> LexicalStream: Es una cadena donde está el mismo código pero transformado
 *    en una serie de fichas (tokens).
 * -> Tokens: Vector donde se almacenan las fichas.
 * -> Punctuation: Vector estático donde se definene todos los simbolos que se
 *    usan en la codificación en C++.
 * -> ReservedWords: Vector estatico donde se definen todas las palabras
 *    reservadas del lenguaje C++.
 */

import java.util.*;

public class Signature {
    
    private java.io.File file;
    private Integer totalLines;
    private String LexicalStream;
    private Vector Tokens;
    public static String[] Punctuation = {" ","\t",".",",","<",">","/","?",";",
     ":","'","\"","~","[","]","{","}","\\","|","!","@","#","$","%","^","&","*",
     "(",")","-","+","=","^"};
    public static String[] ReservedWords = {"asm","auto","bool","break","case",
    "catch","char","class","const","continue","default","delete","do","double",
    "else","enum","extern","float","for","friend","goto","if","inline","int",
    "long","namespace","new","operator","private","protected","public",
    "register","return","short","signed","sizeof","static","struct","switch",
    "template","this","throw","try","typedef","union","unsigned","using",
    "virtual","void","volatile","while"};
    
    /* Constructos de la clase. Recibe por parámetro el archivo que contiene
     * el código fuente. En este método se transforma el código en una serie
     * de fichas
     */
    public Signature(java.io.File file) {
        this.file = file;
        LexicalStream = "";
        int lineCounter = 0, i = 0;
        Tokens = new Vector();
        try{
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            String line = null;
            String[] lineTokens;
            Vector words = null;
            while((line = reader.readLine()) != null){
                lineCounter += 1;
                line = trimComments(line.trim());
                words = separateWords(line.trim());
                if(words != null){
                    lineTokens = getLexicalStream(words).split(" ");
                    for(i = 0;i < words.size();i++){
                        Tokens.add(new Token(lineTokens[i],(String)words.get(i),lineCounter));
                        LexicalStream = LexicalStream + " " + lineTokens[i];
                    }
                }
            }
            reader.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        totalLines = new Integer(lineCounter);
        LexicalStream = LexicalStream.trim();
    }
    
    /* Función que retorna la linea ingresada por parámetro pero sin
     * comentarios
     */
    public String trimComments(String line){
        if(line.length() > 1 && line.substring(0,2).equals("//")){
            line = "";
        }            
        return line;
    }
    
    /* Funcion que convierte un objeto de la clase Vector en una cadena de
     * caracteres
     */
    public String toString(Vector vector) {
        String cad = "";
        for(int i = 0;i < vector.size();i++)
            cad = cad + " " + (String)vector.get(i);
        return cad.trim();
    }
    
    /* Funcion que convierte un vector en una cadena de caracteres
     */
    public String toString(String[] vector) {
        String cad = "";
        for(int i = 0;i < vector.length;i++)
            cad = cad + " " + vector[i];
        return cad.trim();
    }
    
     /* Función que genera un nuevo objeto Signature que es exactamente el
      * mismo ya que recibe por parámetro el mismo archivo con el que fue
      * construido.
      */
    public Signature copy(){
        return new Signature(file);
    }
    
    /* Función que retorna el archivo que fue pasado por parámetro en el
     * constructor
     */
    public java.io.File getFile(){
        return file;
    }
    
    /* Función que retorna el nombre del archivo que fue pasado por parametro
     * en el constructor
     */
    public String getFileName(){
        return file.getName();
    }
    
    /* Funcion que retorna el número de lineas que tiene el archivo que fue 
     * pasado porm parámetro
     */
    public int getTotalLines(){
        return totalLines.intValue();
    }
    
    /* Función que retorna el número de tokens a la que fue convertido el
     * código del archivo.
     */
    public int getNumTokens(){
        return Tokens.size();
    }
    
    /* Función que retorna la cadena que fue generada a partir de la conversion
     * del código fuente a una serie de fichas.
     */
    public String getLexicalStream(){
        return LexicalStream;
    }
    
    /* Función que recibe un vector de palabras y las traduce a fichas y con
     * ellas genera una cadena de fichas que es la que se retorna
     */
    public String getLexicalStream(Vector word){
        String stream = "", token = "";
        for(int i = 0;i < word.size();i++){
            token = (String)word.get(i);
            if(isReservedWord(token))
                stream = stream + " LITERAL_" + token;
            else{
                if(isPunctuation(token)){
                    if(token.equals("(")) stream += " LPAREN";
                    if(token.equals(")")) stream += " RPAREN";
                    if(token.equals("{")) stream += " LCURLY";
                    if(token.equals("}")) stream += " RCURLY";
                    if(token.equals("[")) stream += " LBRACK";
                    if(token.equals("]")) stream += " RBRACK";
                    if(token.equals("=")) stream += " ASSIGN";
                    if(token.equals("<")) stream += " LT";
                    if(token.equals(">")) stream += " BT";
                    if(token.equals("+")) stream += " PLUS";
                    if(token.equals("-")) stream += " MINUS";
                    if(token.equals("*")) stream += " TIMES";
                    if(token.equals("/")) stream += " DIV";
                    if(token.equals("?")) stream += " QUEST";
                    if(token.equals("!")) stream += " ADMIR";
                    if(token.equals(",")) stream += " COMMA";
                    if(token.equals(".")) stream += " DOT";
                    if(token.equals(";")) stream += " SEMI";
                    if(token.equals(":")) stream += " TWODOT";
                    if(token.equals("@")) stream += " AT";
                    if(token.equals("#")) stream += " SHARP";
                    if(token.equals("%")) stream += " PERCENT";
                    if(token.equals("\"")) stream += " QUOTE";
                    if(token.equals("&")) stream += " AMPERS";
                    if(token.equals("$")) stream += " DOLLAR";
                    if(token.equals("\\")) stream += " BCKSLSH";
                    if(token.equals("'")) stream += " SQUOTE";
                    if(token.equals("~")) stream += " TILDE";
                    if(token.equals("^")) stream += " CARET";
                    if(token.equals("|")) stream += " VERBAR";
                }
                else{
                    if(isInt(token))
                        stream += " NUM_INT";
                    else{
                        if(isFloat(token))
                            stream += " NUM_FLOAT";
                        else{
                            if(isDouble(token))
                                stream += " NUM_DOUBLE";
                            else{
                                if(isLong(token))
                                    stream += " NUM_LONG";
                                else
                                    stream += " IDENT";
                            }
                        }
                    }
                }
            }
        }
        return stream.trim();
    }
    
    /* Funcion que retorna el vector de las palabras que están en el código
     * fuente del programa
     */
    public String[] getWords(){
        String[] words = new String[Tokens.size()];
        for(int i = 0;i < Tokens.size();i++)
            words[i] = ((Token)Tokens.get(i)).getWord();
        return words;
    }
    
    /* Funcion que retorna un vector con el nombre de las fichas a las que fue
     * traducido el codigo fuente
     */
    public String[] getTokens(){
        String[] tokens = new String[Tokens.size()];
        for(int i = 0;i < Tokens.size();i++)
            tokens[i] = ((Token)Tokens.get(i)).getToken();
        return tokens;
    }
    
    /* Funcion que retorna el vector asociado de marcas que tiene cada ficha
     * en su informacion
     */
    public Boolean[] getMarks(){
        Boolean[] marks = new Boolean[Tokens.size()];
        for(int i = 0;i < Tokens.size();i++)
            marks[i] = ((Token)Tokens.get(i)).getMark();
        return marks;
    }
    
    /* Funcion que retorna el vector de fichas a las que fue traducido el
     * codigo fuente
     */   
    public Token[] getTokenData(){
        Token[] tokens = new Token[Tokens.size()];
        for(int i = 0;i < Tokens.size();i += 1)
            tokens[i] = ((Token)Tokens.get(i)).copy();
        return tokens;
    }
    
    /* Funcion que indica si una palabra ingresada es un valor numerico del
     * tipo double
     */
    public boolean isDouble(String word){
        boolean sw = true;
        try{
            double num = Double.parseDouble(word);
        }
        catch(Exception e){
            sw = false;
        }
        return sw;
    }
    
    /* Funcion que indica si una palabra ingresada es un valor numerico del
     * tipo int
     */
    public boolean isInt(String word){
        boolean sw = true;
        try{
            int num = Integer.parseInt(word);
        }
        catch(Exception e){
            sw = false;
        }
        return sw;
    }
    
    /* Funcion que indica si una palabra ingresada es un valor numerico del
     * tipo float
     */
    public boolean isFloat(String word){
        boolean sw = true;
        try{
            float num = Float.parseFloat(word);
        }
        catch(Exception e){
            sw = false;
        }
        return sw;
    }
    
    /* Funcion que indica si una palabra ingresada es un valor numerico del
     * tipo long
     */
    public boolean isLong(String word){
        boolean sw = true;
        try{
            long num = Long.parseLong(word);
        }
        catch(Exception e){
            sw = false;
        }
        return sw;
    }
    
    /* Indica si el caracter ingresado pertenece a la lista de simbolos del 
     * lenguaje
     */
    private boolean isPunctuation(String letter){
        boolean sw = false;
        int i = 0;
        while(!sw && i < Punctuation.length){
            if(letter.equals(Punctuation[i]))
                sw = true;
            else
                i++;
        }
        return sw;
    }
    
    /* Indica si la palabra es una palabra reservada del lenguaje
     */
    private boolean isReservedWord(String word){
        boolean sw = false;
        int i = 0;
        while(!sw && i < ReservedWords.length){
            if(word.equals(ReservedWords[i]))
                sw = true;
            else
                i++;
        }
        return sw;
    }
    
    /* Marca como true a la ficha referenciada por el indice ingresado como
     * parametro
     */
    public void mark(int index){
        ((Token)Tokens.get(index)).mark();
    }
    
    /* Marca como false a la ficha referenciada por el indice ingresado como
     * parametro
     */
    public void unmark(int index){
        ((Token)Tokens.get(index)).unmark();
    }
    
    /* Funcion que retorna un vector con las palabras separadas de una linea
     * de codigo
     */
    public Vector separateWords(String line){
        Vector words = null;
        if(line != null && !line.trim().equals("")){
            words = new Vector();
            int i = 0;
            while(i < line.length()){
                if(isPunctuation(line.substring(i,i + 1))){
                    if(i == 0)
                        words.add(line.substring(i,i + 1).trim());
                    else{
                        words.add(line.substring(0,i));
                        if(!line.substring(i,i + 1).trim().equals(""))
                            words.add(line.substring(i,i + 1));
                    }
                    line = line.substring(i + 1,line.length()).trim();
                    i = 0;
                }
                else
                    i++;
            }
            if(i == line.length() && !line.trim().equals(""))
                words.add(line);
        }
        return words;
    }
    
}