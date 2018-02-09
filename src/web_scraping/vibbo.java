package web_scraping;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.LogFactory;

public class vibbo
{   
   
    public void extraerVibbo()
    {
        try {
            HtmlPage page;
            
            java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
            LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
            
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
            java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
            
            
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setTimeout(120000);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.setCssErrorHandler(new com.gargoylesoftware.htmlunit.SilentCssErrorHandler());
            webClient.waitForBackgroundJavaScript(10000);
            //Functions f = new Functions();
            
            String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
            

            String url = "https://www.vibbo.com/pisos-y-casas-toda-espana/?ca=0_s&fPos=148&fOn=sb_location";
            System.out.println("Conectado a: " + url);
            //driver.get(url);
           // page = webClient.getPage(url);
            int timeout = 10000;
            Document doc = Jsoup.connect(url).userAgent(USER_AGENT).timeout(timeout).get();

            //System.out.println(doc.html());
            //System.out.println(page.asXml());
            //Document doc = Jsoup.parse(page.asXml());
            
            Elements a = doc.select("div[class=flipper]").select("a.subjectTitle");
            
            Elements b = doc.select("div[class=result_container_center]");
            
            ArrayList<Object> links = new ArrayList();
            int i = 0;
            for (Element element : a)
            {
                links.add("https:" + element.attr("href"));
                System.out.println("https:" + element.attr("href"));
                //System.out.println("Enlace");
                
                if(i == 3)
                    break;
                
                i++;
            }
            
            for (Element element : b)
            {
                
                System.out.println("Enlace: " + element.select("a").attr("href"));
                
                if(i == 3)
                    break;
                
                i++;
            }
            
            
            i = 0;
            for (Object link : links)
            {
                Document info = Jsoup.connect((String) link).userAgent(USER_AGENT).timeout(30 * 1000).get();
                
                System.out.println("TITULO: " + info.select("h1.productTitle").text()); // TÍTULO
                System.out.println("PRECIO: " + info.select("span.price").text()); // PRECIO
                System.out.println("VENDEDOR: " + info.select("div.StoreName").text()); // VENDEDOR
                System.out.println("DESCRIPCION :" + info.select("p.descriptionLong.no_sel").text()); // CARACTERÍSTICAS
                
                Elements imageElements = info.select("div[class=tel]"); // IMAGENES DEL NÚMERO DE TELÉFONO
                
                // ---------------------------------------------------------------------
                
                /*String directorioTelefonos = "C:\\VIBBO\\TELEFONOS\\" + info.select("h1.productTitle").text();
                
                 crearDirectorio(directorioTelefonos);
                
                // ---------------------------------------------------------------------
                
                // BUSCAR LAS IMAGENES DE TELÉFONO DE CADA LINK
                int j = 1;
                Elements imgs = info.select("div[class=tel]").select("img");
                
                for (Element imageElement : imgs) {
                
                    String strImageURL = imageElement.attr("src");
                
                    System.out.println("IMG " + (j) + " : " + strImageURL);
                        
                // DESCARGAR LAS IMAGENES
                downloadImage("http:" + strImageURL, directorioTelefonos, j);

                j++;
                }/**/
                
                System.out.println("----------------------------------------------");
                System.out.println();
                
                if(i == 3)
                    break;
                
                i++;
            }/**/
            
            //driver.quit();
        } catch (IOException ex) {
            Logger.getLogger(vibbo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(vibbo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    private  void downloadImage(String strImageURL, String nombre, int nroImg) 
    {

        //get file name from image path
        System.out.println(strImageURL);
        
        String strImageName = strImageURL.substring(strImageURL.lastIndexOf("/") + 1);
        
        System.out.println(strImageName);

        System.out.println("Saving: " + strImageName + ", from: " + strImageURL);

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

            System.out.println("Image saved");

        } catch (IOException e) {
            e.printStackTrace();
        }/**/

    }
    
    private  void crearDirectorio(String nombreDir)
    {
        System.out.println(nombreDir);
        File theDir = new File(nombreDir);

        // if the directory does not exist, create it
        if (!theDir.exists()) 
        {
            System.out.println("CREANDO DIRECTORIO: " + theDir.getName());
            
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
                System.out.println("DIRECTORIO " + nombreDir + " CREADO EXITOSAMENTE");
            }
        }
    }
    
    private  void crearExcel(String rutaImagenes) throws IOException, WriteException
    {
        
        WritableWorkbook workbook = Workbook.createWorkbook(new File("C:/Test.xls"));

        WritableSheet sheet = workbook.createSheet("Teléfonos", 0);
        
        // RECORRER CADA IMAGEN Y COLCARLA EN EL EXCEL
        for(int i=1; i<=3; i++)
        {
            
            System.out.println(rutaImagenes + "\\" + i + ".png");
            WritableImage image = new WritableImage(
                    2 + i, 4, //column, row
                    1, 1, //width, height in terms of number of cells
                    new File(rutaImagenes + "\\" + i + ".png")); //Supports only 'png' images

            sheet.addImage(image);
        }

        //Writes out the data held in this workbook in Excel format
        workbook.write();

        //Close and free allocated memory 
        workbook.close();
    }
}