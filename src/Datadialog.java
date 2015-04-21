/*
 * Datadialog.java
 *
 * Desarrollado por: Andres Mauricio Bejarano Posada
 * Construida en: NetBeans IDE 5.5
 *
 * Clase que se encarga de la interfaz directa con el usuario. Esta clase
 * genera un objeto Codesight y sobre el realiza las operaciones propias de la
 * aplicación.
 *
 * Esta clase hereda de JPanel ya que en si es el panel que el usuario ve y
 * sobre el cual trabaja.
 *
 * La información primordial la guarda en la siguiente referencia:
 *  -> Program: Referencia a un objeto de la clase Codesight, el cual contiene
 *´    la logica para leer, transformar, analiar y medir la similitud entre los
 *     archivos.
 *     
 */

public class Datadialog extends javax.swing.JPanel {
    
    private Codesight Program;
        
    /* Constructos de la clase. Recibe por parametro un vector con los archivos
     * que va a ser analizados.
     *
     * En este procedimiento se le indica a los JTextPane que el formato del
     * contenido está escrito en HTML. Las listas que muestran los archivos
     * se cargan en este lugar.
     */
    public Datadialog(java.io.File[] files) {
        initComponents();
        jTextPane1.setContentType("text/html");
        jTextPane2.setContentType("text/html");
        jTextPane3.setContentType("text/html");
        Program = new Codesight(files);
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = Program.getFilesName();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectedIndex(0);
        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = Program.getFilesName();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.setSelectedIndex(0);
        jSpinner1.setValue(new Integer(30));
    }
    
    /* Procedimiento que transforma los simbolos especiales de una cadena en
     * sus equivalentes en codigo HTML. La cadena a transformar se pasa por
     * parámetro.
     */
    public String HTMLEntities(String line){
        String code = line;
        for(int i = 0;i < code.length();i += 1){
            if(code.substring(i,i + 1).equals("<"))
                code = code.substring(0,i) + "&lt;" + code.substring(i + 1);
            if(code.substring(i,i + 1).equals(">"))
                code = code.substring(0,i) + "&gt;" + code.substring(i + 1);
        }
        return code;
    }
    
    /* Procedimiento que escribe los porcentajes de la comparación de todos los
     * archivos en un JTextPane el cual se pasa por parámetro junto con el
     * umbral de comparación. La comparación se realiza dentro de este
     * procedimiento, y la tabla se genera a partir de los valores obtenidos.
     * El texto que se manda a escribir es una tabla en código HTML.
     */
    public void writePercents(javax.swing.JTextPane pane, int threshold){
        float[][] percents = Program.compareAll(threshold);
        String names[] = Program.getFilesName();
        String code = "<table border=\"1\" style=\"font-size:9px\">";
        int percentsLength = Program.getNumSignatures();
        int i = 0;
        int j = 0;
        code += "<tr><td align=\"center\" bgcolor=\"#F4F8B6\">U = " + String.valueOf(threshold) + "</td>";
        for(i = 0;i < percentsLength;i += 1)
            code += "<td bgcolor=\"#ADDFE4\" align=\"center\">" + names[i] + "</td>";
        code += "</tr>";
        for(i = 0;i < percentsLength;i += 1){
            code += "<tr><td bgcolor=\"#ADDFE4\" align=\"center\">" + names[i] + "</td>";
            for(j = 0;j < percentsLength;j += 1){
                code += "<td align=\"center\">";
                if(percents[i][j] < 0)
                    code += "-";
                else
                    code += String.valueOf(percents[i][j]) + "%";
                code += "</td>";
            }
            code += "</tr>";
        }
        code += "</table>";
        pane.setText(code);
        jLabel2.setText("Umbral de similitud = " + String.valueOf(threshold));
    }
    
    /* Procedimiento que escribe en el JTextPane, indicado por parámetro, el 
     * código del archivo cuyo indice se pasa por parámetro en la variable 
     * index. En este punto se colorea las lineas copiadas, por tanto se
     * requiere saber cuales lineas se encuentran dentro de las encontradas 
     * como copia. Para ello se pasa por parámetro el objeto de la clase
     * Match que contiene los resultados de la comparación y el numero del
     * archivo que se comparo (el primero o el segundo) indicado en la variable
     * num.
     */
    public void writeCode(javax.swing.JTextPane pane,Match result,int index,int num){
        java.io.File file = Program.getSignature(index).getFile();
        String text = "";
        java.util.Vector code = new java.util.Vector();
        java.util.Vector mark = new java.util.Vector();
        java.util.Vector color = new java.util.Vector();
        try{
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            while((text = reader.readLine()) != null){
                code.add(text);
                mark.add(Boolean.FALSE);
                color.add("#000000");
            }
            reader.close();
        }
        catch(Exception e){
            javax.swing.JOptionPane.showMessageDialog(this,"Excepcion grave","Codesight",javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        Object[][] data = result.getIntegerData();
        Object[][] tokens = result.getTokenData(num);
        int dataSize = data.length;
        int i = 0;
        int j = 0;
        int posInit = 0;
        int posEnd = 0;
        int tokenInit = 0;
        int tokenEnd = 0;
        int currentColor = 0;
        String colorCode = "";
        for(i = 0;i < dataSize;i += 1){
            tokenInit = ((Integer)data[i][num - 1]).intValue();
            tokenEnd = (((Integer)data[i][num - 1]).intValue() + ((Integer)data[i][2]).intValue()) - 1;
            posInit = ((Integer)tokens[tokenInit][3]).intValue();
            posEnd = (Integer)tokens[tokenEnd][3];
            colorCode = "#" + Integer.toHexString(Match.colors[currentColor][0]) + Integer.toHexString(Match.colors[currentColor][1]) + Integer.toHexString(Match.colors[currentColor][2]);
            for(j = posInit;j <= posEnd; j += 1){
                mark.set(j - 1,Boolean.TRUE);                
                color.set(j - 1,colorCode);
            }
            currentColor += 1;
        }
        String sourceCode = "";
        for(i = 0;i < code.size();i += 1){
            if(((Boolean)mark.get(i)).booleanValue()){
                if(i == 0)
                    sourceCode = "<pre><code><font color=\"" + (String)color.get(i) + "\">" + String.valueOf(i + 1) + " " + HTMLEntities((String)code.get(i)) + "</font></code></pre>";
                else
                    sourceCode += "<pre><code><font color=\"" + (String)color.get(i) + "\">" + String.valueOf(i + 1) + " " + HTMLEntities((String)code.get(i)) + "</font></code></pre>";
            }
            else{
                if(i == 0)
                    sourceCode = "<pre><code><font color=\"" + (String)color.get(i) + "\">" + String.valueOf(i + 1) + " " + HTMLEntities((String)code.get(i)) + "</font></code></pre>";
                else
                    sourceCode += "<pre><code><font color=\"" + (String)color.get(i) + "\">" + String.valueOf(i + 1) + " " + HTMLEntities((String)code.get(i)) + "</font></code></pre>";
                
            }
        }
        pane.setText(sourceCode);
    }
    
    /* Método generado automáticamente por el IDE, el cual carga los 
     * componentes visuales en memoria para luego ser llamados en el
     * constructor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setLayout(null);

        jPanel1.setLayout(null);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccion de archivos"));
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 20, 140, 130);

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jList2);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(170, 20, 140, 130);

        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton1);
        jButton1.setBounds(110, 210, 90, 23);

        jLabel1.setText("Umbral de similitud");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(40, 160, 110, 20);

        jPanel1.add(jSpinner1);
        jSpinner1.setBounds(160, 160, 90, 20);

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Actualizar los resultados generales");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel1.add(jCheckBox1);
        jCheckBox1.setBounds(10, 190, 290, 15);

        add(jPanel1);
        jPanel1.setBounds(10, 10, 320, 250);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados generales"));
        jTextPane3.setEditable(false);
        jScrollPane3.setViewportView(jTextPane3);

        jLabel2.setText("Umbral de similitud:");

        jButton2.setText("Ampliar");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        add(jPanel2);
        jPanel2.setBounds(10, 280, 320, 310);

        jPanel3.setLayout(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Visualizacion de resultados"));
        jTextPane1.setEditable(false);
        jScrollPane4.setViewportView(jTextPane1);

        jPanel3.add(jScrollPane4);
        jScrollPane4.setBounds(10, 50, 270, 270);

        jTextPane2.setEditable(false);
        jScrollPane5.setViewportView(jTextPane2);

        jPanel3.add(jScrollPane5);
        jScrollPane5.setBounds(290, 50, 270, 270);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable2.setEnabled(false);
        jScrollPane6.setViewportView(jTable2);

        jPanel3.add(jScrollPane6);
        jScrollPane6.setBounds(10, 330, 270, 100);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable3.setEnabled(false);
        jScrollPane7.setViewportView(jTable3);

        jPanel3.add(jScrollPane7);
        jScrollPane7.setBounds(290, 330, 270, 100);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable4.setEnabled(false);
        jScrollPane8.setViewportView(jTable4);

        jPanel3.add(jScrollPane8);
        jScrollPane8.setBounds(120, 440, 340, 100);

        jLabel3.setText("Porcentaje de similitud:");
        jPanel3.add(jLabel3);
        jLabel3.setBounds(10, 550, 240, 14);

        jLabel4.setText("Archivo A");
        jPanel3.add(jLabel4);
        jLabel4.setBounds(10, 30, 270, 14);

        jLabel5.setText("Archivo B");
        jPanel3.add(jLabel5);
        jLabel5.setBounds(290, 30, 270, 14);

        add(jPanel3);
        jPanel3.setBounds(340, 10, 570, 580);

    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        javax.swing.JTextPane pane = new javax.swing.JTextPane();
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(pane);
        scroll.setSize(800,600);
        pane.setContentType("text/html");
        pane.setEditable(false);
        pane.setText(jTextPane3.getText());
        javax.swing.JOptionPane.showMessageDialog(this,scroll,"Codesight - Resultados generales",javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton2ActionPerformed

    /* Método que se ejecuta cada vez que se hace clic en el boton Aceptar.
     * Este lee el umbral del JSpinner e invoca los métodos de comparación con
     *´el fin de mostrar la información en los JTextPane y los JTable.
     * Los archivos a comparar los toma a partir de los indices leios de los
     * JList.
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int threshold = -1;
        try{
            threshold = ((Integer)jSpinner1.getValue()).intValue();
            if(threshold < 0)
                javax.swing.JOptionPane.showMessageDialog(this,"El valor del umbral debe ser mayor que cero","Codesight",javax.swing.JOptionPane.WARNING_MESSAGE);
            else{
                if(jCheckBox1.isSelected()){
                    writePercents(jTextPane3,threshold);
                }
                Match result = Program.greedyStringTiling(jList1.getSelectedIndex(),jList2.getSelectedIndex(),threshold);
                jLabel4.setText(Program.getFileName(jList1.getSelectedIndex()));
                jLabel5.setText(Program.getFileName(jList2.getSelectedIndex()));
                writeCode(jTextPane1,result,jList1.getSelectedIndex(),1);
                writeCode(jTextPane2,result,jList2.getSelectedIndex(),2);
                jTable2.setModel(new javax.swing.table.DefaultTableModel(result.getTokenData(1),new String [] {"Index","Token","Word","Line","Mark"}));
                jTable3.setModel(new javax.swing.table.DefaultTableModel(result.getTokenData(2),new String [] {"Index","Token","Word","Line","Mark"}));
                jTable4.setModel(new javax.swing.table.DefaultTableModel(result.getObjectData(),new String[] {"#","Token A","Token B","Cantidad","Color"}));
                jTable4.setDefaultRenderer(Object.class,new TableRenderer());
                jLabel3.setText("Porcentaje de similitud: " + String.valueOf(Program.round(Program.getPercentMatch(result),2)) + "%");
                if(!jButton2.isEnabled())
                    jButton2.setEnabled(true);
            }
        }
        catch(NumberFormatException e){
            javax.swing.JOptionPane.showMessageDialog(this,"El umbral debe ser un valor numerico","Codesight",javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    // End of variables declaration//GEN-END:variables
    
}
