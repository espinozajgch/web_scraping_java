package web_scraping;

import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class main {
public static final String url = "https://www.google.co.ve/search?q=mlb";
    
    
    public static void main(String[] args) {
	JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        //SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.OfficeSilver2007Skin");
           try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }/**/
         
       //vibbo portal = new vibbo();
      // portal.extraerVibbo();
        
	new Vinterfaz();
        
        //new Cronometro();
        
        //vibbo r = new vibbo();
        //r.extraerVibbo();
        //resultados r = new resultados();
       // r.iniciarHilo();
        //r.extraerVibbo("https://www.vibbo.com/pisos-y-casas-toda-espana/?ca=0_s&fPos=148&fOn=sb_location", 3);
        //r.extraerVibbo("https://www.vibbo.com/pisos-y-casas/?ca=0_s&x=3&w=3&c=107&o=2", 1);
        
        ///r.extraerMilAnuncios("https://www.milanuncios.com/venta-de-viviendas-en-girona/2", 1);
        //r.extraerMilAnuncios("https://www.milanuncios.com/venta-de-pisos-en-girona/?pagina=2", 0);
               
        //r.extraerPisos("https://www.pisos.com/venta/pisos-girona/", 1);
        //r.extraerIdeaLista("https://www.idealista.com/venta-viviendas/roses-girona/", 1);
        //r.extraerMilAnuncios("https://www.milanuncios.com/venta-de-viviendas-en-girona/?pagina=2", 1);
        //r.extraerFotoCasa("https://www.fotocasa.es/es/alquiler/pisos/madrid-provincia/todas-las-zonas/l", 1);
        //r.extraerYaEcontre("https://www.yaencontre.com/venta/pisos/madrid/pag-2", 1);
        
        
        
        
        /**/
    }
      
   
}
