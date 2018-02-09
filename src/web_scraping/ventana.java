/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_scraping;

import java.awt.Container;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author JoseEspinoza
 */
public class ventana extends JDialog {
    
    private Container contenedor;
    private JLabel Etiqueta;
    
    public ventana(){
        setTitle("DESCARGANDO INFORMACION");
        setSize(200,230);
        setLocationRelativeTo(null);
        setResizable(false); 

        //setDefaultCloseOperation(0); // Desactiva el boton cerrar 
        //setVisible(true);

        contenedor = getContentPane(); //Instanciamos el Contenedor		
        
        contenedor.setLayout(null);

        Etiqueta = new JLabel();
        //Etiqueta.setText("Creando Archivos del Sistema");
        Etiqueta.setBounds(20, 0, 200, 200);
        ImageIcon icon =  new ImageIcon(getClass().getResource("/imagen/cargando.gif"));
       
        Etiqueta.setIcon(icon);
        icon.setImageObserver(Etiqueta);
        contenedor.add(Etiqueta);/**/
        this.repaint();
        //setVisible(true);
    }
}
