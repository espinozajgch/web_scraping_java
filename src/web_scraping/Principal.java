package web_scraping;

import java.awt.BorderLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 *
 * @author neo
 */
public class Principal extends javax.swing.JFrame implements Runnable{
    boolean seguirHilo=false;
    boolean hiloIniciado=false;
    Thread hilo;
    int cont=0;
    private JTextField progressTextField;
    
    /**
     * Creates new form Principal
     */
    public Principal() {
        //initComponents();
    }   

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        System.exit(0);
    }                                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }
    /*método para iniciar el hilo*/
    public void iniciarHilo(){
        hilo=new Thread(this);
        hilo.start();
        hiloIniciado=true;
    }
    /*método para parar el hilo*/
    public void pararHIlo(boolean estado){
        seguirHilo=estado;
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton jbLanzarHilo;
    // End of variables declaration                   

    @Override
    public void run() {
        /*aquí va el código que se ejecutará en el hilo*/
        while(seguirHilo){
            System.out.println(cont+" :Hola mundo desde java usando hilos");
            cont++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}