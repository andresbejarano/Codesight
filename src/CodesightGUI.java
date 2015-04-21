/*
 * CodesightGUI.java
 *
 * Desarrollado por: Andres Mauricio Bejarano Posada
 * Construida en: NetBeans IDE 5.5
 *
 * Es la clase que maneja la interfaz con el usuario a nivel de aplicativo en
 * general. Esta clase es la que debe ser llamada con el fin que la aplicación
 * fucione.
 */

import java.util.*;

public class CodesightGUI extends javax.swing.JFrame implements java.io.FilenameFilter {
    
    public static final String[] Types = {".cpp",".c",".h"};
    
    /* Constructor de la clase. Se encarga de cargar los componentes gráficos y
     * centrar el cuadro del programa en la pantalla del usuario.
     */
    public CodesightGUI() {
        initComponents();
        java.awt.Dimension Dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)((int)Dim.getWidth() - this.getWidth()) / 2,((int)(int)Dim.getHeight() - this.getHeight()) / 2);
    }
    
    /* Método heredado de la interface FilenameFilter. Se encarga de decirle a
     * la clase cuales son los archivos que debe abrir de acuerdo con su
     * extensión. Recibe como parámetro la dirección de la carpeta que va a
     * vigilar y el nombre del archivo a abrir, el cual debe esatr dentro de
     * la carpeta. Retorna true si la extensión del archivo se encuentra dentro
     * de las extensiones válidad, las cuales fueron pasadas al constructor. En
     * caso contrario retorna false.
     */
    public boolean accept(java.io.File dir, String name){
        boolean sw = false;
        int i = 0;
        while(!sw && i < Types.length){
            if(name.endsWith(Types[i]))
                sw = true;
            else
                i++;
        }
        return sw;
    }
    
    /* Método generado automáticamente por el IDE, el cual carga los 
     * componentes visuales en memoria para luego ser llamados en el
     * constructor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Codesight - Detector de similitud de codigo");

        jMenu1.setText("Archivo");

        jMenuItem1.setText("Nueva deteccion");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem1);

        jMenu1.add(jSeparator1);

        jMenuItem2.setText("Salir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1001, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /* Procedimiento que se ejecuta cada vez que se hace clic en la opción
     * "Salir". Se encarga de cerrar la aplicacion.
     */
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    
    /* Procedimiento que se ejecuta cada vez que se hace clic en la opción
     * "Nueva Detección". Se encarga de llamar a un objeto de la clase
     * Datadialog, el cual contiene la ventana de ejecución de la comparación
     * y muestra de resultados.
     */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        javax.swing.JFileChooser Chooser = new javax.swing.JFileChooser();
        Chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        int selected = Chooser.showOpenDialog(this);
        if(selected == javax.swing.JFileChooser.APPROVE_OPTION){
            String path = Chooser.getSelectedFile().getAbsolutePath();
            java.io.File[] Files = new java.io.File(path).listFiles(this);
            if(Files.length <= 0)
                javax.swing.JOptionPane.showMessageDialog(null,"La carpeta seleccionada no contiene archivos validos de C++.","Codesight",javax.swing.JOptionPane.WARNING_MESSAGE);
            else {
                javax.swing.JInternalFrame page = new javax.swing.JInternalFrame("Codesight - " + path);
                page.add(new Datadialog(Files));
                jDesktopPane1.add(page);
                page.pack();
                page.setIconifiable(true);
                page.setClosable(true);
                page.setBounds(jDesktopPane1.getComponentCount() * 10,jDesktopPane1.getComponentCount() * 10,930,630);
                page.show();
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    
    /* Procedimiento principal de la aplicación.
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CodesightGUI().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
    
}
