/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_scraping;

import java.awt.Container;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Pc
 */
public class resultados implements Runnable{

    /**
     * @param args the command line arguments
     */
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    private String deporte = "";
    private String fecha = "2017-03-19";
    private String titulo  ="", publicacion ="", descripcion = "", telefono = "", vendedor  = "", precio = ""; 
    private String datosVendedor[];
    private String tipoVendedor = "", nombreVendedor = "";
    private String resultado = "", nextLink="";
    private SimpleDateFormat formateador = new SimpleDateFormat("yyyy/MM/dd");
    private DecimalFormat formatter = new DecimalFormat("#.##");
    private int cont = 0;
             
    private InputStreamReader isr = new InputStreamReader(System.in);
    private BufferedReader br = new BufferedReader (isr);
    
    private Document doc, info, modal;
    private ArrayList <NodoInfo> links = new ArrayList();
    private ArrayList <NodoInfo> links2 = new ArrayList();
    private NodoInfo p;
    private int timeout = (90 * 1000);
    
    private Container contenedor;
    private JLabel Etiqueta;

    private boolean isAlive = false;
    private boolean estatus = true;
    private boolean activo = false;

    private int idFiltro = 6;
    private int paginas = 1;
    private int posicion = 0, pag = 0;

    private String enlace = "";
    private String atributo = "", attr[], modalURL = "";
    
    private boolean seguirHilo=false;
    private boolean hiloIniciado=false;
    private Thread hilo;
    
    private HtmlPage page;
    
    private File fichero;
    
    
    
    public resultados(){
    	
    }
    /*
    public void ejecutar(int idFiltro, String enlace){
        if(idFiltro == 0){
                System.out.println("Extrayendo informacion de IDEALISTA: " + enlace);
                extraerIdeaLista(enlace); 
                System.out.println("Extraccion Finalizda");
            }
            else 
            if(idFiltro == 1){
                System.out.println("Extrayendo informacion de FOTOCASA: " + enlace);
                extraerFotoCasa(enlace);
                System.out.println("Extraccion Finalizda");
            }
            else 
            if(idFiltro == 2){
                System.out.println("Extrayendo informacion de YOENCONTRE: " + enlace);
                extraerYaEcontre(enlace); 
                System.out.println("Extraccion Finalizda");   
            }
            else 
            if(idFiltro == 3){
                
            }
            else 
            if(idFiltro == 4){ 
                 
            }
    }
    
    public void ejecutar2(int idFiltro){
        if(idFiltro == 0){
            extraerInfoIdeaLista(); 
            System.out.println("Extraccion Finalizda");
        }
        else 
        if(idFiltro == 1){
            //System.out.println("Extrayendo informacion de FOTOCASA: " + enlace);
            //extraerInfoIdeaLista(); 
            System.out.println("Extraccion Finalizda");
        }
        else 
        if(idFiltro == 2){
            //System.out.println("Extrayendo informacion de YOENCONTRE: " + enlace);
            //extraerInfoIdeaLista(); 
            System.out.println("Extraccion Finalizda");   
        }
        else 
        if(idFiltro == 3){

        }
        else 
        if(idFiltro == 4){ 

        }
    }*/
    
    public String extraerIdeaLista(String url, int paginacion){
        String nextUrl = url;
        Elements title;
        String texto="";
        int r;
        
        posicion = url.indexOf("pagina-");  
            
        if(posicion >= 0){
            pag = Integer.parseInt(url.substring((posicion + 7), (posicion + 8)));
            url = url.substring(0,posicion); 
            r = pag;
            paginacion += pag;
        }
        else{
            r = 1;
        }  
        
        try {
            links.clear();

            do{
                int i = 1;
                
                if(isHiloIniciado()){
                    
                    doc = Jsoup.connect(nextUrl).userAgent(USER_AGENT).timeout(timeout).get();

                    //if(sonEspacios(title)){
                       // title = doc.select("div.h1-container");
                    //}
                    //texto = doc.select("div#h1-container > span").text();
                    //texto += " " + doc.select("div#h1-container > h1").text();
                    //System.out.println("TITULO:" + texto);
                        
                    Elements a = doc.select("div.item-info-container > a");

                    for (Element element : a) 
                    {
                        if(isHiloIniciado()){
                            p = new NodoInfo();
                            nextLink = "https://www.idealista.com" + element.attr("href");                 
                            p.setUrl(nextLink);
                            links.add(p);

                            /*if(i==3)
                                break;/**/
                            i++;
                        }
                        else{
                            break;
                        }  
                    }
                    System.out.println(url + "pagina-" + (r+1) +".htm");
                    nextUrl = url + "pagina-" + (r+1) +".htm";    
                }
                else{
                    break;
                }
              
                r++;
            }while(r < paginacion);
             
            //resultado = "Cantidad de Publicaciones Encontradas: " + texto;
            resultado = "Cantidad de Publicaciones Encontradas: " + cantindadPublicaciones();
            resultado += "\n\n";

                int i = 1;
            for (NodoInfo link : links) 
            {
                if(isHiloIniciado()){
                     
                    System.out.println("Conectando a:" +  link.getUrl());
                    info = Jsoup.connect(link.getUrl()).userAgent(USER_AGENT).timeout(timeout).get();

                    titulo = info.select("h1.txt-bold").text();
                    precio = info.select("span.txt-big.txt-bold").first().text();
                    descripcion = info.select("div.adCommentsLanguage.expandable").text();
                    vendedor = info.select("div.advertiser-data.txt-soft > p").first().text();
                    telefono = info.select("p.txt-bold._browserPhone.icon-phone").text();
                    //vendedor = info.select("a.about-advertiser-name").text();
                    
                    //System.out.println("vendedor 0: "+ vendedor);

                    if(encontrarPalabra(vendedor, "Profesional") || encontrarPalabra(vendedor, "Particular")){

                        datosVendedor = vendedor.split("-");
                        //System.out.println(datosVendedor.length);   
                            if(datosVendedor.length > 1){
                                tipoVendedor = datosVendedor[0];
                                nombreVendedor = datosVendedor[1];
                                
                                //System.out.println("vendedor 1: "+ nombreVendedor);
                               // System.out.println("Tipo vendedor : "+ tipoVendedor);
                            }
                    }
                    
                    
                    vendedor = info.select("a.about-advertiser-name").text();
                    
                    if(sonEspacios(nombreVendedor)){
                        nombreVendedor = vendedor;
                        //System.out.println("vendedor 2: "+ nombreVendedor);
                    }
                    else{
                        nombreVendedor += " - " + vendedor;
                       // System.out.println("vendedor 3: "+ nombreVendedor);
                    }

                    
                    link.setNombrePublicacion(titulo);
                    link.setPrecio(precio);
                    link.setDescripcion(descripcion);
                    link.setTipoVendedor(tipoVendedor);
                    link.setNombreVendedor(nombreVendedor);
                    link.setNumTelefono(telefono);

                    resultado += "[" + i + "] Conectando a:" +  link.getUrl();
                    resultado += "\n";
                    resultado += "TITULO: " + link.getNombrePublicacion();
                    resultado += "\n";
                    resultado += "PRECIO: " + link.getPrecio();
                    resultado += "\n";
                    resultado += "TIPO VENDEDOR: " + link.getTipoVendedor();
                    resultado += "\n";
                    resultado += "NOMBRE VENDEDOR: " + link.getNombreVendedor();
                    resultado += "\n";
                    resultado += "TELEFONO: " + link.getNumTelefono();
                    resultado += "\n";
                    resultado += "DESCRIPCION: " + link.getDescripcion();
                    resultado += "\n\n\n";

                    System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                    this.setActivo(true);

                    /*if(i==3)
                        break;/***/
                    i++; 
                }
            }    
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Tiempo de Espera Agotado, intente la descarga nuevamente"); 
            estatus = false;
        }
        
        return resultado;
    }
     
    public String extraerFotoCasa(String url, int paginacion){
        String nextUrl = url;
        int r;
        
        posicion = url.indexOf("/l");  
            //System.out.println(posicion + "," + url.length());
            
            if(posicion >= 0){
                if((url.length() - posicion) == 4){
                    pag = Integer.parseInt(url.substring((posicion + 3)));
                    url = url.substring(0,(posicion + 2)); 
                    System.out.println(url);
                    //System.out.println(pag);  
                    r = pag;
                    paginacion += pag;
                }
                else{
                    r = 1;
                }  
            }
            else{
                r = 1;
            }
        
        try {
            links.clear();

            do{
                int i = 1;
                
                if(isHiloIniciado()){
                    doc = Jsoup.connect(nextUrl).userAgent(USER_AGENT).timeout(timeout).get();

                    Elements a = doc.select("div.re-Searchresult-item");

                    for (Element element : a) {
                        if(isHiloIniciado()){
                            p = new NodoInfo();
                            nextLink = "https://www.fotocasa.es" + element.getElementsByClass("re-Card-title").attr("href");                 
                            p.setUrl(nextLink);

                            titulo = element.getElementsByClass("re-Card-title").text();
                            p.setNombrePublicacion(titulo);
                            p.setPrecio(element.select("span[class=re-Card-price]").text());
                            p.setNumTelefono(element.select("a[class=re-Button re-Button--flat]").text());
                            links.add(p);


                            System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                            /*if(i==3)
                                break;/**/
                            i++;
                        }
                        else{
                            break;
                        }
                    }

                    System.out.println(url +"/"+ (r+1));
                    nextUrl = url +"/"+ (r+1);

                    r++;
                }
                else{
                    break;
                }
            }while(r < paginacion);

            resultado = "Cantidad de Publicaciones Encontradas: " + cantindadPublicaciones();
            resultado += "\n\n";
            
            int i = 1;
            for (NodoInfo link : links) {
                if(isHiloIniciado()){
                    System.out.println("-"+ link.getUrl());
                    info = Jsoup.connect(link.getUrl()).userAgent(USER_AGENT).timeout(timeout).get();

                    link.setDescripcion(info.select("div[class=detail-section-content]").text());

                    vendedor = info.select("span[class=agency-contact]").text();

                    //System.out.println("vendedor: "+ vendedor);

                    if(this.sonEspacios(vendedor)){
                        vendedor = info.select("div[class=contact-client-professional c-align bold]").text();
                    }

                    datosVendedor = vendedor.split(":");
                            //System.out.println(datosVendedor.length);   
                    if(datosVendedor.length > 1){
                        tipoVendedor = datosVendedor[0];
                        nombreVendedor = datosVendedor[1];

                        //System.out.println("vendedor 1: "+ nombreVendedor);
                        //System.out.println("Tipo vendedor : "+ tipoVendedor);
                    }

                    link.setNombreVendedor(nombreVendedor);

                        resultado += "[" + i + "] Conectando a:" +  link.getUrl();
                        resultado += "\n";
                        resultado += "TITULO: " + link.getNombrePublicacion();
                        resultado += "\n";
                        resultado += "PRECIO: " + link.getPrecio();
                        resultado += "\n";
                        resultado += "TIPO VENDEDOR: " + link.getTipoVendedor();
                        resultado += "\n";
                        resultado += "NOMBRE VENDEDOR: " + link.getNombreVendedor();
                        resultado += "\n";
                        resultado += "TELEFONO: " + link.getNumTelefono();
                        resultado += "\n";
                        resultado += "DESCRIPCION: " + link.getDescripcion();
                        resultado += "\n\n\n";

                        this.setActivo(true);

                    //if(i==3)
                    //   break;

                    i++; 
                }
                else{
                    break;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Tiempo de Espera Agotado, intente la descarga nuevamente"); 
            estatus = false;
        }/**/
        return resultado;
    }
    
    public String extraerYaEcontre(String url, int paginacion){
        String nextUrl = url;       
        int r = 0;
        
        posicion = url.indexOf("pag-");  
        //System.out.println(url.substring((posicion + 4), (posicion + 5)));
            
            if(posicion >= 0){
                pag = Integer.parseInt(url.substring((posicion + 4), (posicion + 5)));
                url = url.substring(0, (posicion-1)); 
  
                System.out.println(url);
                //System.out.println(pag);  
                r = pag;
                paginacion += pag;
                 
            }
            else{
                r = 1;
            }/**/
        
        try {
            links.clear();

            do{
                int i = 1;

                if(isHiloIniciado()){
                    doc = Jsoup.connect(nextUrl).userAgent(USER_AGENT).timeout(timeout).get();

                    Elements articles = doc.select("article.row.result_inm");

                    for (Element element : articles) {
                        if(isHiloIniciado()){
                            p = new NodoInfo();
                            nextLink = element.select("a.secondary-font.js-inmueble-link.js-override-event.js-event-title").attr("href");                 
                            p.setUrl(nextLink);

                            titulo = element.select("a.secondary-font.js-inmueble-link.js-override-event.js-event-title").text();

                            p.setNombrePublicacion(titulo);
                            p.setPrecio(element.select("span.price").text());
                            p.setNumTelefono(element.select("span.inline-icon.yaencontre-graphic-iconos--telefono").next().text());

                            //System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                            links.add(p);

                            System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                            System.out.println("["+ i +"] publicacion encontrada: " + p.getUrl());
                            /*if(i==3)
                                break;/**/

                            i++;
                        }
                        else{
                            break;
                        }
                    } 

                    System.out.println(url + "/pag-" + (r + 1));
                    nextUrl = url + "/pag-" +(r + 1);

                    r++;
                }
                else{
                    break;
                }
            }while(r < paginacion);
            
            resultado = "Cantidad de Publicaciones Encontradas: " + cantindadPublicaciones();
            resultado += "\n\n";

                int i = 1;
                for (NodoInfo link : links) {
                    if(isHiloIniciado()){
                        System.out.println("Publicacion N# " + i);
                        info = Jsoup.connect(link.getUrl()).userAgent(USER_AGENT).timeout(timeout).get();

                        link.setDescripcion(info.select("div.mb-sm > p.m-none").text());

                        vendedor = info.select("div.agency-data > h4").text();
                        link.setNombreVendedor(vendedor);

                        resultado += "[" + i + "] Conectando a:" +  link.getUrl();
                        resultado += "\n";
                        resultado += "TITULO: " + link.getNombrePublicacion();
                        resultado += "\n";
                        resultado += "PRECIO: " + link.getPrecio();
                        resultado += "\n";
                        resultado += "TIPO VENDEDOR: " + link.getTipoVendedor();
                        resultado += "\n";
                        resultado += "NOMBRE VENDEDOR: " + link.getNombreVendedor();
                        resultado += "\n";
                        resultado += "TELEFONO: " + link.getNumTelefono();
                        resultado += "\n";
                        resultado += "DESCRIPCION: " + link.getDescripcion();
                        resultado += "\n\n\n";

                        this.setActivo(true);

                        /*if(i==3)
                            break;/**/
                        i++;
                    }
                    else{
                        break;
                    }
                }    
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Tiempo de Espera Agotado, intente la descarga nuevamente"); 
            estatus = false;
        }

        return resultado;
    }
    
    public String extraerMilAnuncios(String url, int paginacion){
        String nextUrl = url;       
        int r = 0;
        boolean tipo = false;
        
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

    
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.setCssErrorHandler(new com.gargoylesoftware.htmlunit.SilentCssErrorHandler());
        webClient.waitForBackgroundJavaScript(10000);
        
        posicion = url.indexOf("?pagina=");  
        //System.out.println(url.substring((posicion + 4), (posicion + 5)));
            
            if(posicion >= 0){
                pag = Integer.parseInt(url.substring((posicion + 8), (posicion + 9)));
                url = url.substring(0, (posicion-2)); 
  
                System.out.println(url);
               // System.out.println(pag);  
                r = pag;
                paginacion += pag;
                //System.out.println("r = " + r);
                 
            }
            else{
                posicion = url.indexOf("&pagina=");  
                
                if(posicion >= 0){
                    pag = Integer.parseInt(url.substring((posicion + 8), (posicion + 9)));
                    url = url.substring(0, (posicion-2)); 

                    System.out.println(url);
                    //System.out.println(pag);  
                    r = pag;
                    paginacion += pag;
                    tipo = true;
                    //System.out.println("r = " + r);
                }
                else{
                    url = url.substring(0, (url.length()-1)); 
                    r = 1; 
                }
            	
                //System.out.println("r = " + r);
            }/**/
        
        try {
            links.clear();
            //System.out.println("conectando " + url);
            doc = Jsoup.connect(nextUrl).userAgent(USER_AGENT).timeout(timeout).get();

            do{
                int i = 1;

                if(isHiloIniciado()){
                    doc = Jsoup.connect(nextUrl).userAgent(USER_AGENT).timeout(timeout).get();

                    Elements articles = doc.select("div.aditem-detail > a");

                    for (Element element : articles) {
                        if(isHiloIniciado()){
                            p = new NodoInfo();
                            nextLink = "https://www.milanuncios.com" + element.attr("href");                 
                            p.setUrl(nextLink);

                            titulo = element.text();

                            p.setNombrePublicacion(titulo);
                            
                            //System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                            links.add(p);

                            System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                            System.out.println("["+ i +"] publicacion encontrada: " + p.getUrl());
                            /*if(i==3)
                                break;
                            /**/
                            i++;
                        }
                        else{
                            break;
                        }/**/
                    } 

                    if(tipo){
                        System.out.println(url + "&pagina=" + (r + 1));
                        nextUrl = url + "&pagina=" +(r + 1);
                    }
                    else{
                        System.out.println(url + "/?pagina=" + (r + 1));
                        nextUrl = url + "/?pagina=" +(r + 1);
                    }

                    r++;
                }
                else{
                    break;
                }/**/
            }while(r < paginacion);/**/
            
            resultado = "Cantidad de Publicaciones Encontradas: " + cantindadPublicaciones();
            resultado += "\n\n";

                int i = 1;
                for (NodoInfo link : links) {

                    if(isHiloIniciado()){
                        System.out.println("Publicacion N# " + i + " URL: " + link.getUrl());
                        info = Jsoup.connect(link.getUrl()).userAgent(USER_AGENT).timeout(timeout).get();

                        link.setDescripcion(info.select("p.pagAnuCuerpoAnu").text());
                        link.setPrecio(info.select("div.pagAnuPrecioTexto").text());
                        
                        tipoVendedor = info.select("div[class=pagAnuContactSellerType pagAnuContactSellerTypePro]").text();
                        
                        if(sonEspacios(tipoVendedor))
                           tipoVendedor = info.select("div[class=pagAnuContactSellerType pagAnuContactSellerTypePriv]").text();
                         
                        
                        
                        nombreVendedor = info.select("div.pagAnuContactNombre").text();
                        link.setTipoVendedor(tipoVendedor);
                        link.setNombreVendedor(nombreVendedor);
                                
                        atributo = info.getElementsByClass("pagAnuContactButton").attr("onClick");
            		
            		String attr[] = atributo.split("'");
            		
                        System.out.println("tipoVendedor:" + link.getTipoVendedor());
                        System.out.println("nombreVendedor:" + link.getNombreVendedor());
                        System.out.println("precio:" + link.getPrecio());
                        System.out.println("attr:" + atributo);
            		System.out.println("\n");	
                        if(attr.length > 1){
                            modalURL = "https://www.milanuncios.com/datos-contacto/?id=" + attr[1];
                            System.out.println("Conectando a: " + modalURL);
                            page = webClient.getPage(modalURL);

                            modal = Jsoup.parse(page.asXml());
                            link.setNumTelefono(modal.select("div.telefonos").text());
                            //vendedor = modal.select("strong").text();
                            System.out.println("Telefono: " + link.getNumTelefono());
                           
                            /*if(encontrarPalabra(vendedor, "Profesional")){

                                posicion = vendedor.indexOf("Profesional");
                                tipoVendedor = vendedor.substring(posicion, vendedor.length() -1);
                                nombreVendedor = vendedor.substring(0, posicion -1);
                                
                                System.out.println("vendedor 1: "+ nombreVendedor);
                                System.out.println("Tipo vendedor : "+ tipoVendedor);

                                link.setTipoVendedor(tipoVendedor);
                                link.setNombreVendedor(nombreVendedor);
                            }
                            else
            			if(encontrarPalabra(vendedor, "Particular")){		
                                    posicion = vendedor.indexOf("Particular");           				
                                    tipoVendedor = vendedor.substring(posicion, vendedor.length() -1);
                                    nombreVendedor = vendedor.substring(0, posicion -1);      
                                    System.out.println("vendedor 2: "+ nombreVendedor);
                                    System.out.println("Tipo vendedor : "+ tipoVendedor);

                                    link.setTipoVendedor(tipoVendedor);
                                    link.setNombreVendedor(nombreVendedor);
            			}*/
            		}
                        
            		System.out.println("");
                        System.out.println("");
                        
                        resultado += "[" + i + "] Conectando a:" +  link.getUrl();
                        resultado += "\n";
                        resultado += "TITULO: " + link.getNombrePublicacion();
                        resultado += "\n";
                        resultado += "PRECIO: " + link.getPrecio();
                        resultado += "\n";
                        resultado += "TIPO VENDEDOR: " + link.getTipoVendedor();
                        resultado += "\n";
                        resultado += "NOMBRE VENDEDOR: " + link.getNombreVendedor();
                        resultado += "\n";
                        resultado += "TELEFONO: " + link.getNumTelefono();
                        resultado += "\n";
                        resultado += "DESCRIPCION: " + link.getDescripcion();
                        resultado += "\n\n\n";

                        this.setActivo(true);

                        /*if(i==3)
                            break;
                        /**/
                        i++;
                    }
                    else{
                        break;
                    }/**/
                } /**/   
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Tiempo de Espera Agotado, intente la descarga nuevamente"); 
            estatus = false;
        }/**/

        return resultado;
    }
    
    public String extraerPisos(String url, int paginacion){
        String nextUrl = url;       
        int r = 0;
        
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);


        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.setCssErrorHandler(new com.gargoylesoftware.htmlunit.SilentCssErrorHandler());
        webClient.waitForBackgroundJavaScript(10000);
       
        posicion = url.length();
        String car = url.substring((posicion - 2), (posicion - 1 ));
        
        if(isNumeric(car)){
            //System.out.println(" - - " + url.substring((url.length() - 2), (url.length() - 1 )));
            pag = Integer.parseInt(car);
            url = url.substring(0, (posicion -2 )); 

            System.out.println(url);
            //System.out.println(pag);  
            r = pag;
            paginacion += pag;
            System.out.println("r = " + r);
        }
        else{
            //System.out.println("- - " + url + "1"); 
            //url = url.substring(0, (url.length()-1)); 
            r = 1;
            System.out.println("r = " + r);
        }

        try {
            links.clear();
            System.out.println("conectando " + url);
            doc = Jsoup.connect(nextUrl).userAgent(USER_AGENT).timeout(timeout).get();

            do{
                int i = 1;

                if(isHiloIniciado()){
                    doc = Jsoup.connect(nextUrl).userAgent(USER_AGENT).timeout(timeout).get();

                    Elements articles = doc.select("h3.title > a");

                    for (Element element : articles) {
                        if(isHiloIniciado()){
                            p = new NodoInfo();
                            nextLink = "https://www.pisos.com" + element.attr("href");                 
                            p.setUrl(nextLink);

                            titulo = element.text();

                            p.setNombrePublicacion(titulo);
                            
                            //System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                            links.add(p);

                            System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                            System.out.println("["+ i +"] publicacion encontrada: " + p.getUrl());
                            
                            /*if(i==3)
                                break;/**/

                            i++;
                        }
                        else{
                            break;
                        }/**/
                    } 

                    System.out.println(url + (r + 1));
                    nextUrl = url + (r + 1);

                    r++;
                }
                else{
                    break;
                }/**/
            }while(r < paginacion);/**/
            
            resultado = "Cantidad de Publicaciones Encontradas: " + cantindadPublicaciones();
            resultado += "\n\n";

                int i = 1;
                for (NodoInfo link : links) {
                    if(isHiloIniciado()){
                        System.out.println("Publicacion N# " + i + " URL: " + link.getUrl());
                        info = Jsoup.connect(link.getUrl()).userAgent(USER_AGENT).timeout(timeout).get();

                        link.setDescripcion(info.select("div.description").text());
                        link.setPrecio(info.select("div.price > span").text());

                        atributo = info.getElementsByClass("button w445h50").attr("onClick");
            			
                        String attr[] = atributo.split("'");

                        //System.out.println("descripcion:" + link.getDescripcion());
                        System.out.println("precio:" + link.getPrecio());
                        System.out.println("attr:" + atributo);

                        if(attr.length > 1){
                            modalURL = "https://www.pisos.com/Contactar/contactarModal?_idAnuncio="+ attr[1] +"&_esPromo=False&_TOpContexto=&_IDEspecialistaSeleccionado=0&_idJornadaPA=&_idExtension=PaAnMod&_origenContacto=parrilla_segundaMano&contextQueryString";
                            modal = Jsoup.connect(modalURL).userAgent(USER_AGENT).timeout(timeout).get();
                            
                            nombreVendedor =  modal.select("div[class=line name]").text();
                            System.out.println("nombre vendedor: " + modal.select("div[class=line name]").text());
                            System.out.println("Telefono: " + modal.select("div.phone > span").text());

                            link.setNumTelefono(modal.select("div.phone > span").text());

                            link.setNombreVendedor(nombreVendedor);
                            

                        }

                        resultado += "[" + i + "] Conectando a:" +  link.getUrl();
                        resultado += "\n";
                        resultado += "TITULO: " + link.getNombrePublicacion();
                        resultado += "\n";
                        resultado += "PRECIO: " + link.getPrecio();
                        resultado += "\n";
                        resultado += "TIPO VENDEDOR: " + link.getTipoVendedor();
                        resultado += "\n";
                        resultado += "NOMBRE VENDEDOR: " + link.getNombreVendedor();
                        resultado += "\n";
                        resultado += "TELEFONO: " + link.getNumTelefono();
                        resultado += "\n";
                        resultado += "DESCRIPCION: " + link.getDescripcion();
                        resultado += "\n\n\n";

                        this.setActivo(true);

                        /*if(i==3)
                            break;/**/
                        
                        i++;
                    }
                    else{
                        break;
                    }
                } /**/   
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Tiempo de Espera Agotado, intente la descarga nuevamente"); 
            estatus = false;
        }/**/

        return resultado;
    }
    
    public void extraerVibbo(String url, int paginacion){
        String nextUrl = url;       
        int r = 0;
        
        posicion = url.length();
        String car = url.substring((posicion - 1), (posicion));
        //System.out.println(car);
        
        if(isNumeric(car)){
            //System.out.println(" - - " + url.substring((url.length() - 2), (url.length() - 1 )));
            pag = Integer.parseInt(car);
            url = url.substring(0, (posicion -2 )); 

            System.out.println(url);
            //System.out.println(pag);  
            r = pag;
            paginacion += pag;
            System.out.println("r = " + r);
        }
        else{
            //System.out.println("- - " + url + "1"); 
            //url = url.substring(0, (url.length()-1)); 
            r = 1;
            System.out.println("r = " + r);
        }
        
        try {
            links.clear();
            System.out.println("conectando a:" + nextUrl);

            do{
                int i = 1;

                if(isHiloIniciado()){
                    doc = Jsoup.connect(nextUrl).userAgent(USER_AGENT).timeout(timeout).get();

                    Elements publicacion = doc.select("div[class=flipper]").select("a.subjectTitle");
                    Elements b = doc.select("div[class=result_container_center]");
                    
                    for (Element element : publicacion) {
                        if(isHiloIniciado()){
                            p = new NodoInfo();
                            nextLink = "https:" + element.attr("href");                 
                            p.setUrl(nextLink);

                            //titulo = element.text();
                            //p.setNombrePublicacion(titulo);
                            
                            //System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                            links.add(p);

                            //System.out.println("["+ i +"] publicacion encontrada: " + titulo);
                            System.out.println("["+ i +"] publicacion encontrada: " + p.getUrl());
                            
                            /*if(i==3)
                                break;/**/

                            i++;
                        }
                        else{
                            break;
                        }/**/  
                    } 
                    
                    if(r==1){
                        url = "https:" + b.select("a").attr("href");
                        url = url.substring(0, (url.length()-2));
                        //System.out.println("Enlace: " + url);
                    }

                    System.out.println(url + (r + 1));
                    nextUrl = url + (r + 1);

                    r++;
                }
                else{
                    break;
                }/**/
            }while(r < paginacion);/**/
           
            resultado = "Cantidad de Publicaciones Encontradas: " + cantindadPublicaciones();
            resultado += "\n\n";

            int i = 1;
            for (NodoInfo link : links) {
                if(isHiloIniciado()){
                    System.out.println("Publicacion N# " + i + " URL: " + link.getUrl());
                    
                    info = Jsoup.connect(link.getUrl()).userAgent(USER_AGENT).timeout(timeout).get();
                    
                    titulo = info.select("h1.productTitle").text();
                    precio = info.select("span.price").text();
                    nombreVendedor = info.select("div.StoreName").text();
                    descripcion = info.select("p.descriptionLong.no_sel").text();

                    link.setNombreVendedor(nombreVendedor);
                   
                    link.setPrecio(precio);
                    link.setDescripcion(descripcion);
                    
                    /*String attr[] = titulo.split("/");
                    //System.out.println(attr.length);
                    if(attr.length > 1){
                        titulo = "";
                        //System.out.println("titulo:" + attr[0]);
                        for(int k = 0; k < attr.length; k++){
                            titulo = titulo + attr[k];
                            
                        }
                    }*/
                    
                    titulo = remove(titulo);
                    
                    
                    link.setNombrePublicacion(titulo);
                    //System.out.println("titulo:" + titulo);
                    String directorioTelefonos = "C:\\VIBBO\\TELEFONOS\\" + titulo;

                    link.setNumTelefono(directorioTelefonos);
                    crearDirectorio(directorioTelefonos);

                    // BUSCAR LAS IMAGENES DE TELÉFONO DE CADA LINK
                    int j = 1;
                    Elements imgs = info.select("div[class=tel]").select("img");

                    for (Element imageElement : imgs) {
                        String strImageURL = imageElement.attr("src");
                        //System.out.println("IMG " + (j) + " : " + strImageURL);

                        // DESCARGAR LAS IMAGENES
                        downloadImage("http:" + strImageURL, directorioTelefonos, j);
                        j++;
                    }/**/
                    
                    resultado += "[" + i + "] Conectando a:" +  link.getUrl();
                    resultado += "\n";
                    resultado += "TITULO: " + link.getNombrePublicacion();
                    resultado += "\n";
                    resultado += "PRECIO: " + link.getPrecio();
                    resultado += "\n";
                    resultado += "TIPO VENDEDOR: " + link.getTipoVendedor();
                    resultado += "\n";
                    resultado += "NOMBRE VENDEDOR: " + link.getNombreVendedor();
                    resultado += "\n";
                    resultado += "TELEFONO: " + link.getNumTelefono();
                    resultado += "\n";
                    resultado += "DESCRIPCION: " + link.getDescripcion();
                    resultado += "\n\n\n";

                    this.setActivo(true);

                    /*if(i==3)
                        break;/**/

                    i++;
                }
                else{
                    break;
                }
            } /**/  

        } catch (IOException ex) {
            Logger.getLogger(vibbo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(vibbo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static String remove(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "\\/:*?<>\"";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        //System.out.println("original: " + original);
        String ascii = ",";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(0));
        }//for i
        return output;
    }//remove1
    
    private boolean isNumeric(String cadena){
	try {
            Integer.parseInt(cadena);
            return true;
	} catch (NumberFormatException nfe){
            return false;
	}
    }
    
    
    
    private boolean encontrarPalabra(String texto, String palabra){
        
        if(texto.indexOf(palabra) != -1)
            return true;
        else
            return false;
        
    }
    
    private  void downloadImage(String strImageURL, String nombre, int nroImg) 
    {

        //get file name from image path
        //System.out.println(strImageURL);
        
        String strImageName = strImageURL.substring(strImageURL.lastIndexOf("/") + 1);
        
        //System.out.println(strImageName);

        //System.out.println("Saving: " + strImageName + ", from: " + strImageURL);

        try {

            //open the stream from URL
            URL urlImage = new URL(strImageURL);
            InputStream in = urlImage.openStream();

            byte[] buffer = new byte[4096];
            int n = -1;

            OutputStream os
                    = new FileOutputStream(nombre + "/" + nroImg + ".png");

            //write bytes to the output stream
            while ((n = in.read(buffer)) != -1) {
                os.write(buffer, 0, n);
            }

            //close the stream
            os.close();

            //System.out.println("Image saved");

        } catch (IOException e) {
            e.printStackTrace();
        }/**/

    }
    
    private  void crearDirectorio(String nombreDir)
    {
        //System.out.println(nombreDir);
        File theDir = new File(nombreDir);

        // if the directory does not exist, create it
        if (!theDir.exists()) 
        {
            //System.out.println("CREANDO DIRECTORIO: " + theDir.getName());
            
            boolean result = false;

            try 
            {
                theDir.mkdirs();
                result = true;
            } 
            
            catch (SecurityException se) 
            {
                se.printStackTrace();
            }
            
            if (result) 
            {
                //System.out.println("DIRECTORIO " + nombreDir + " CREADO EXITOSAMENTE");
            }
        }
    }
    
    
    public String imprimeResultados(){
        
        /*try {
            
            int i = 1;
            for (NodoInfo link : links) {
            
                System.out.println("Publicacion N# " + i);
                info = Jsoup.connect(link.getUrl()).userAgent(USER_AGENT).timeout(timeout).get();
                
                link.setDescripcion(info.select("div.mb-sm > p.m-none").text());
                
                vendedor = info.select("div.agency-data > h4").text();
                link.setNombreVendedor(vendedor);
                
                resultado  = "[" + i + "] Conectando a:" +  link.getUrl() ; 
                resultado += "\n";
                resultado += "TITULO: " + link.getNombrePublicacion();
                resultado += "\n";
                resultado += "PRECIO: " + link.getPrecio();
                resultado += "\n";
                resultado += "DESCRIPCION: " + link.getDescripcion();
                resultado += "\n";
                resultado += "NOMBRE: " + link.getNombreVendedor();
                resultado += "\n";
                resultado += "TELEFONO: " + link.getNumTelefono();
                resultado += "\n";
                resultado += "\n";
                resultado += "\n";
                
                i++;
            
            }
        } catch (IOException ex) {
            Logger.getLogger(resultados.class.getName()).log(Level.SEVERE, null, ex);
        }/**/
        
        return resultado;
    }

    private boolean sonEspacios(String cad){
        for(int i =0; i<cad.length(); i++)
        if(cad.charAt(i) != ' ')
        return false;

        return true;
   }
    
   private void addCaption(WritableSheet sheet, int column, int row, String s, CellView cv)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s);
        sheet.setColumnView(column, cv);
        sheet.addCell(label);

    } 
    
    
   private void addImage(WritableSheet sheet, int column, int row, String rutaImg, int i ,CellView cv)
            throws RowsExceededException, WriteException {
       
        fichero = new File(rutaImg + "\\" + i + ".png");
        
        if(fichero.exists()){
            WritableImage image = new WritableImage(
                    column , row, //column, row
                    1, 1, //width, height in terms of number of cells
                    new File(rutaImg + "\\" + i + ".png"));
            sheet.setColumnView(column, cv);
            sheet.addImage(image); 
        }
        

    }
   
    private void addTitle(WritableSheet sheet, int column, int row, String s, WritableCellFormat cFormat)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, cFormat);
        sheet.addCell(label);
    }
    
    public void downloadExcel(String rutaArchivo, String portal, String fechaHora) throws IOException, WriteException {
  
        //System.out.println("-" + rutaArchivo + portal);
        File file = new File(rutaArchivo + "\\" + portal +" "+ fechaHora + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        //wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook = Workbook.createWorkbook(file);
        workbook.createSheet(portal + ".com", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        
        WritableCellFormat cFormat = new WritableCellFormat();
        WritableFont font = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false);
        cFormat.setFont(font);
        cFormat.setWrap(true);
        
        WritableCellFormat cText = new WritableCellFormat();
        WritableFont fontText = new WritableFont(WritableFont.ARIAL, 11);
        cText.setFont(fontText);
        //cText.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(cText);
        cv.setAutosize(true);

        // Write a few headers
        //addCaption(excelSheet, 0, 0, "Contacto Directo: espinozajgx@gmail.com");
        
        
        int i = 1;
        
        if(portal.equalsIgnoreCase("idealista.com") || portal.equalsIgnoreCase("milanuncios.com")){
            addTitle(excelSheet, 0, 0, "URL", cFormat);
            addTitle(excelSheet, 1, 0, "Título", cFormat);
            addTitle(excelSheet, 2, 0, "Precio", cFormat);
            addTitle(excelSheet, 3, 0, "Tipo", cFormat);
            addTitle(excelSheet, 4, 0, "Vendedor", cFormat);
            addTitle(excelSheet, 5, 0, "Teléfono", cFormat);
            addTitle(excelSheet, 6, 0, "Descripción", cFormat);
            
            for (NodoInfo link : links) {   
                if(!link.getTipoVendedor().trim().equalsIgnoreCase("Profesional")){
                    System.out.println("PARTICULAR");
                    // System.out.println(link.getTipoVendedor());
                    addCaption(excelSheet, 0, i, link.getUrl(), cv);
                    addCaption(excelSheet, 1, i, link.getNombrePublicacion(), cv);
                    addCaption(excelSheet, 2, i, link.getPrecio(), cv);
                    addCaption(excelSheet, 3, i, link.getTipoVendedor(), cv);
                    addCaption(excelSheet, 4, i, link.getNombreVendedor(), cv);
                    addCaption(excelSheet, 5, i, link.getNumTelefono(), cv);
                    addCaption(excelSheet, 6, i, link.getDescripcion(), cv);/**/

                    i++; 
                }
            }
            
            
            for (NodoInfo link : links) {   
                if(link.getTipoVendedor().trim().equalsIgnoreCase("profesional")){
                    System.out.println("PROFESIONAL");
                    addCaption(excelSheet, 0, i, link.getUrl(), cv);
                    addCaption(excelSheet, 1, i, link.getNombrePublicacion(), cv);
                    addCaption(excelSheet, 2, i, link.getPrecio(), cv);
                    addCaption(excelSheet, 3, i, link.getTipoVendedor(), cv);
                    addCaption(excelSheet, 4, i, link.getNombreVendedor(), cv);
                    addCaption(excelSheet, 5, i, link.getNumTelefono(), cv);
                    addCaption(excelSheet, 6, i, link.getDescripcion(), cv);/**/

                    i++; 
                }
            }
        }
        else
            if(portal.equalsIgnoreCase("vibbo.com")){
                
                addTitle(excelSheet, 0, 0, "URL", cFormat);
                addTitle(excelSheet, 1, 0, "Título", cFormat);
                addTitle(excelSheet, 2, 0, "Precio", cFormat);

                addTitle(excelSheet, 3, 0, "Vendedor", cFormat);
                addTitle(excelSheet, 4, 0, "N#1", cFormat);
                addTitle(excelSheet, 5, 0, "N#2", cFormat);
                addTitle(excelSheet, 6, 0, "N#3", cFormat);
                addTitle(excelSheet, 7, 0, "Descripción", cFormat);
                
                 for (NodoInfo link : links) {
                    addCaption(excelSheet, 0, i, link.getUrl(), cv);
                    addCaption(excelSheet, 1, i, link.getNombrePublicacion(), cv);
                    addCaption(excelSheet, 2, i, link.getPrecio(), cv);
                    //addCaption(excelSheet, 3, i, link.getTipoVendedor(), cv);
                    addCaption(excelSheet, 3, i, link.getNombreVendedor(), cv);
                    addImage(excelSheet, 4, i, link.getNumTelefono(), 1 ,cv);
                    addImage(excelSheet, 5, i, link.getNumTelefono(), 2 ,cv);
                    addImage(excelSheet, 6, i, link.getNumTelefono(), 3 ,cv);
                    addCaption(excelSheet, 7, i, link.getDescripcion(), cv);
                    
                    i++;
                 }
            }
        else{
            addTitle(excelSheet, 0, 0, "URL", cFormat);
            addTitle(excelSheet, 1, 0, "Título", cFormat);
            addTitle(excelSheet, 2, 0, "Precio", cFormat);
            //addTitle(excelSheet, 3, 0, "Tipo", cFormat);
            addTitle(excelSheet, 4, 0, "Vendedor", cFormat);
            addTitle(excelSheet, 5, 0, "Teléfono", cFormat);
            addTitle(excelSheet, 6, 0, "Descripción", cFormat);
                
            for (NodoInfo link : links) {
                addCaption(excelSheet, 0, i, link.getUrl(), cv);
                addCaption(excelSheet, 1, i, link.getNombrePublicacion(), cv);
                addCaption(excelSheet, 2, i, link.getPrecio(), cv);
                addCaption(excelSheet, 3, i, link.getTipoVendedor(), cv);
                addCaption(excelSheet, 4, i, link.getNombreVendedor(), cv);
                addCaption(excelSheet, 5, i, link.getNumTelefono(), cv);
                addCaption(excelSheet, 6, i, link.getDescripcion(), cv);

                i++;
            }
        }

        workbook.write();
        workbook.close();
        /**/
    }
    
   
    
    /**
    * Con esta método compruebo el Status code de la respuesta que recibo al hacer la petición
    * EJM:
    * 		200 OK			300 Multiple Choices
    * 		301 Moved Permanently	305 Use Proxy
    * 		400 Bad Request		403 Forbidden
    * 		404 Not Found		500 Internal Server Error
    * 		502 Bad Gateway		503 Service Unavailable
    * @param url
    * @return Status Code
    */
   public static int getStatusConnectionCode(String url) {

       Response response = null;

       try {
           response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
       } catch (IOException ex) {
           System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
       }
       return response.statusCode();
   }
   
   /**
    * Con este método devuelvo un objeto de la clase Document con el contenido del
    * HTML de la web que me permitirá parsearlo con los métodos de la librelia JSoup
    * @param url
    * @return Documento con el HTML
    */
   public static Document getHtmlDocument(String url) {

       Document doc = null;
           try {
               doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
               } catch (IOException ex) {
                   System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
               }
       return doc;
   }
   
   
   public Document getHTMLAJAXDOCUMENT(String url){
        try {
            java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
            LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
            
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
            java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
            
            
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.setCssErrorHandler(new com.gargoylesoftware.htmlunit.SilentCssErrorHandler());
            webClient.waitForBackgroundJavaScript(10000);
            
            page = webClient.getPage(url);
            
            modal = Jsoup.parse(page.asXml());
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(resultados.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(resultados.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return modal;
        
   }

    public int cantindadPublicaciones(){ 
        
        if (links != null)
            return links.size();
        else
            return 0;
    }
    
    public ArrayList<NodoInfo> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<NodoInfo> links) {
        this.links = links;
    }
    
    

    public void setIdFiltro(int idFiltro) {
        this.idFiltro = idFiltro;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
        
        //System.out.println("Extrayendo informacion de: " + enlace);
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean isEstatus() {
        return estatus;
    }
    
    /*método para iniciar el hilo*/
    public void iniciarHilo(){
        hilo=new Thread(this);
        hilo.start();
        hiloIniciado = true;
        seguirHilo = true;
        resultado = "";
    }

    public boolean isHiloIniciado() {
        return hiloIniciado;
    }
    /*método para parar el hilo*/
    public void pararHIlo(){
        seguirHilo = false;
        hiloIniciado = false;
    }
    
    public boolean seguirHilo(){
        return seguirHilo;
    }
    
    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }
    
    public void run(){
        
        while(seguirHilo){
            
           // resultado +="Extrayendo informacion de IDEALISTA:" ;
            
           if(idFiltro == 0){
                System.out.println("Extrayendo informacion de IDEALISTA: " + enlace);
                extraerIdeaLista(enlace, paginas); 
                idFiltro = -1;
                System.out.println("Extraccion Finalizada");
                pararHIlo();
                hilo.stop();
                seguirHilo = false;
                
            }
            else 
            if(idFiltro == 1){
                System.out.println("Extrayendo informacion de FOTOCASA: " + enlace);
                extraerFotoCasa(enlace, paginas);
                idFiltro = -1;
                System.out.println("Extraccion Finalizada");
                pararHIlo();
                hilo.stop();
            }
            else 
            if(idFiltro == 2){
                System.out.println("Extrayendo informacion de YAENCONTRE: " + enlace);
                extraerYaEcontre(enlace, paginas); 
                idFiltro = -1;
                System.out.println("Extraccion Finalizada");
                pararHIlo();
                hilo.stop();  
            }
            else 
            if(idFiltro == 3){
                System.out.println("Extrayendo informacion de MILANUNCIOS: " + enlace);
                extraerMilAnuncios(enlace, paginas); 
                idFiltro = -1;
                System.out.println("Extraccion Finalizada");
                pararHIlo();
                hilo.stop();  
                
            }
            else 
            if(idFiltro == 4){ 
                System.out.println("Extrayendo informacion de PISOS: " + enlace);
                extraerPisos(enlace, paginas); 
                idFiltro = -1;
                System.out.println("Extraccion Finalizada");
                pararHIlo();
                hilo.stop();  
                 
            } 
           else 
            if(idFiltro == 5){ 
                System.out.println("Extrayendo informacion de VIBBO: " + enlace);
                extraerVibbo(enlace, paginas); 
                idFiltro = -1;
                System.out.println("Extraccion Finalizada");
                pararHIlo();
                hilo.stop();  
                 
            } 
        }
        
    }

}
