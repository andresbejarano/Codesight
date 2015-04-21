/*
 * TableRenderer.java
 *
 * Desarrollado por: Andres Mauricio Bejarano Posada
 * Construida en: NetBeans IDE 5.5
 *
 * Clase encargada de coloreas la tabla de las secuencias encontradas como
 * comunes en ambos archivos.
 */
public class TableRenderer extends javax.swing.table.DefaultTableCellRenderer {
    
    /* Método heredado que se utiliza para dibujar los colores de la matriz 
     * para identificar una secuencia de fichas copiadas con un color en 
     * especifico.
     */
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        if(column == 4)
            this.setBackground(new java.awt.Color(Match.colors[row][0],Match.colors[row][1],Match.colors[row][2]));
        else
            this.setBackground(java.awt.Color.WHITE);
        return this;
    }
    
}
