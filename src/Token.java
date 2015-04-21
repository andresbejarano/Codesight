/*
 * Token.java
 *
 * Desarrollado por: Andres Mauricio Bejarano Posada
 * Construida en: NetBeans IDE 5.5
 *
 * Clase que almacena la informacion correspondiente a una ficha (token).
 *
 * Esta clase almacena los datos en las siguientes variables:
 *  -> Token: almacena el tipo de ficha a la que corresponde.
 *  -> Word: Almacena la palabra o simbolo a la que esta asociada la ficha.
 *  -> Line: Indica el numero de linea a la que pertenece la ficha.
 *  -> Mark: Indica si la ficha ha sido marcada para el proceso de 
       identificacion de las fichas solapadas.
 */

public class Token {
    
    private String Token, Word;
    private Integer Line;
    private Boolean Mark;
    
    /* Constructor de la clase. Recibe como parametros el valor de la ficha que
     * tiene asociado, la palabra asociada a la ficha y el numero de linea
     * dentro del codigo del archivo.
     */
    public Token(String token, String word, int line) {
        Token = token;
        Word = word;
        Line = new Integer(line);
        Mark = Boolean.FALSE;
    }
    
    /* Funcion que devuelve un vector de Objects con los datos de la ficha.
     */
    public Object[] getData(){
        Object[] data = new Object[4];
        data[0] = Token;
        data[1] = Word;
        data[2] = Line;
        data[3] = Mark;
        return data;
    }
    
    /* Devuelve el valor de la variable Line.
     */
    public int getLine(){
        return Line.intValue();
    }
    
    /* Devuelve el valor de la variable Mark.
     */
    public boolean getMark(){
        return Mark.booleanValue();
    }
    
    /* Devuelve el valor de la variable Token.
     */
    public String getToken(){
        return Token;
    }
    
    /* Devuelve el valor de la variable Word.
     */
    public String getWord(){
        return Word;
    }
    
    /* Marca como true a la ficha.
     */
    public void mark(){
        Mark = Boolean.TRUE;
    }
    
    /* Marca como false a la ficha.
    */
    public void unmark(){
        Mark = Boolean.FALSE;
    }
    
    /* Copia el contenido de la ficha a otro objeto de la misma clase.
    */
    public Token copy(){
        Token copy = new Token(Token, Word, Line.intValue());
        if(Mark.booleanValue())
            copy.mark();
        return copy;
    }
    
}