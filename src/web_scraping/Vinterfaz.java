package web_scraping;


import java.awt.Color;


import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultEditorKit;
import jxl.write.WriteException;
import org.apache.commons.validator.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.jvnet.substance.SubstanceLookAndFeel;


public class Vinterfaz extends JFrame implements ActionListener, ItemListener{
	
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    
	//private miModelo modelo  = new miModelo();
	private DefaultTableModel modelos;
	
	private JScrollPane scrollPane;

	private JMenuBar barraMenu = new JMenuBar();
	private JMenu menuArchivo = new JMenu("Archivo");
	private JMenu menuReportes = new JMenu("Reportes");
	private JMenu menuAyuda = new JMenu("Ayuda");
	
	private JMenuItem itemEditar, itemAdmin;
	private JMenuItem itemAcerca, itemManual, itemCronograma, itemVisualizar, itemExportar;
	
	private JButton btnBuscar, btnDescargar, botonDetener, btnLimpiar, btnAsignar;
	private JTextField txtfPaginacion;
        private JTextField txtfBuscar = new JTextField();
	private JLabel lcantidad, lbuscar, lportal, lfecha, lpaginacion;
	
	private Container contenedor;
	private JPanel panelInterfaz;
	
	private JComboBox comboFiltro;
	
	private JTable tabla;
	
	private int idCrono = 0, idTeg = 0;
	private int idFiltro = 0;
	
	private String fecha="", hora ="", fechaHora;
	private Date date;
	private int h, m;
	private Calendar calendario = Calendar.getInstance();

	private Connection conn = null;

	private JFileChooser file;
	
	private  String ruta = "reporte\\reporteCronograma.jasper";
        
        private resultados consulta;
        
        private JTextArea txtarea = new JTextArea();;
        
        private UrlValidator validar = new UrlValidator();
        
        private String portal = "";
        
        private String enlaceAnt = "", res = "";
        
        private Document doc;
        private ArrayList <NodoInfo> links = new ArrayList();
        private NodoInfo p;
        private int timeout = (90 * 1000);
        
        private String titulo  ="", publicacion ="", descripcion = "", telefono = "", vendedor  = "", precio = "";
        private String resultado = "", nextLink="";
        
        private Integer minutos = 0 , segundos = 0, milesimas = 0;
        //min es minutos, seg es segundos y mil es milesimas de segundo
        private String min="", seg="", mil="";
        
        private String[] arrayPortales = new String[6];


	public Vinterfaz(){	
            
            arrayPortales[0] = "idealista.com";
            arrayPortales[1] = "fotocasa.es";
            arrayPortales[2] = "yaencontre.com";
            arrayPortales[3] = "milanuncios.com";
            arrayPortales[4] = "pisos.com";
            arrayPortales[5] = "vibbo.com";
            
            setSize(695, 495);
            setTitle("EXTRACTOR DE PORTALES, " + this.getFechaActual());
	    setLocationRelativeTo(this.getParent());	    
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    iniciarComponentes();
	    setResizable(false);
	    //filtrar("");

	    Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagen/icono.png"));
	    setIconImage(icon);
	    setVisible(true);
            
            
           
            
            consulta = new resultados();
            
            //Vinterfaz ping = new Vinterfaz("pruebas");
            //new Thread(ping).start();
            
	}

	public void iniciarComponentes(){		
            contenedor = getContentPane(); /*Instanciamos el Contenedor*/
            contenedor.setBackground(Color.white);
            /*Con esto difinimos nosostros mismos los tama�os y pocision de los componentes*/
            contenedor.setLayout(null);		

            panelInterfaz = new JPanel();
            panelInterfaz.setLayout(null);
            panelInterfaz.setBounds(7, 10, 675, 447);
            panelInterfaz.setBackground(Color.white);
            panelInterfaz.setBorder(BorderFactory.createTitledBorder(""));
            contenedor.add(panelInterfaz);

            /*menuArchivo.setMnemonic('A');
        
            itemEditar = new JMenuItem("Jurado");
            itemEditar.addActionListener(this);
            itemEditar.setMnemonic('J');

            itemAdmin = new JMenuItem("TEG");
            itemAdmin.addActionListener(this);
            itemAdmin.setMnemonic('T');

            itemCronograma = new JMenuItem("Cronograma");
            itemCronograma.addActionListener(this);
            itemCronograma.setMnemonic('C');

            itemExportar = new JMenuItem("Exportar PDF");
            itemExportar.addActionListener(this);

            itemVisualizar = new JMenuItem("Visualizar");
            itemVisualizar.addActionListener(this);

            itemAcerca = new JMenuItem("Acerca de SETEG");
            itemAcerca.addActionListener(this);

            itemManual= new JMenuItem("Ver Manual");
            itemManual.addActionListener(this);

            menuAyuda.add(itemManual);
            menuAyuda.add(itemAcerca);

            menuReportes.add(itemExportar);
            menuReportes.add(itemVisualizar);

            menuArchivo.add(itemEditar);
            menuArchivo.add(itemAdmin);
            menuArchivo.add(itemCronograma);

            JMenuItem itemSalir = new JMenuItem("Salir");
         
            itemSalir.setMnemonic('S');

            menuArchivo.add(itemSalir);

            itemSalir.addActionListener(
                     new ActionListener(){
                         public void actionPerformed(ActionEvent evento)
                         {

                             dispose();

                         }
                     }
            );

            barraMenu.add(menuArchivo); 
            barraMenu.add(menuReportes);
            barraMenu.add(menuAyuda);

            //System.out.print(getSize());
            //setJMenuBar(barraMenu);	
            /**/

            //lportal = new JLabel("SELECCIONE UN PORTAL:");
            lportal = new JLabel("INGRESE LA URL DE: (IDEALISTA,FOTOCASA,YAENCONTRE,MILANUNCIOS,PISOS,VIBBO)");
            lportal.setBounds(10, 10, 450, 20);
            panelInterfaz.add(lportal);
            
            comboFiltro=new JComboBox();
            comboFiltro.setBounds(10, 35, 180, 28);
            comboFiltro.addItemListener(this);
            comboFiltro.addItem("IDEALISTA.COM");
            comboFiltro.addItem("FOTOCASA.ES");
            comboFiltro.addItem("YAENCONTRE.COM");           
            comboFiltro.addItem("MILANUNCIOS.COM");
            comboFiltro.addItem("PISOS.COM");
            comboFiltro.addItem("VIBBO.COM");
            
            //panelInterfaz.add(comboFiltro);

            idFiltro = comboFiltro.getSelectedIndex();
            
            lbuscar = new JLabel("INGRESE LA URL:");
            lbuscar.setBounds(200, 10, 250, 20);
            //panelInterfaz.add(lbuscar);
            
            txtfBuscar = new JTextField();
            txtfBuscar.setBounds(10, 35 , 575, 30);  
            //txtfBuscar.setBounds(200, 35 , 385, 30);  
            panelInterfaz.add(txtfBuscar);
            
            txtfBuscar.addMouseListener(new MouseAdapter() {
                public void mouseReleased(final MouseEvent e) {
                 if (e.isPopupTrigger()) {
                  final JPopupMenu menu = new JPopupMenu();
                  JMenuItem item;
                  item = new JMenuItem(new DefaultEditorKit.CopyAction());
                  item.setText("Copiar");
                  item.setEnabled(txtfBuscar.getSelectionStart() != txtfBuscar.getSelectionEnd());
                  menu.add(item);
                  item = new JMenuItem(new DefaultEditorKit.PasteAction());
                  item.setText("Pegar");
                  item.setEnabled(txtfBuscar.isEditable());
                  menu.add(item);
                  menu.show(e.getComponent(), e.getX(), e.getY());
                 }
                }
            });
            
            lpaginacion = new JLabel("PAGINAS:");
            lpaginacion.setBounds(590, 10, 50, 20);
            panelInterfaz.add(lpaginacion);
            
            txtfPaginacion = new JTextField();
            txtfPaginacion.setBounds(590, 35 , 40, 30);  /*COORDENADAS: X, Y, LARGO, ANCHO*/
            panelInterfaz.add(txtfPaginacion);
            
            txtfPaginacion.addKeyListener(new KeyListener(){
  			@Override			
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
              		//validarCampos();
                    }       
                }
                @Override
                public void keyReleased(KeyEvent arg0) {}
                @Override
                public void keyTyped(KeyEvent evt) {	
                        char car = evt.getKeyChar();
                        if((car<'0' || car>'9')) evt.consume();
                        if(car=='\\') evt.consume();
                        if(txtfPaginacion.getText().length()>=3) evt.consume();
                        if(!(car!=(char)KeyEvent.VK_SPACE)){
                                evt.consume();
                        }
                }
            });

            btnBuscar = new JButton();
            btnBuscar.setBounds(635, 35, 30, 29);  /*COORDENADAS: X, Y, LARGO, ANCHO*/
            btnBuscar.setIcon(new ImageIcon(getClass().getResource("/imagen/buscar.png")));
            btnBuscar.addActionListener(this);
            panelInterfaz.add(btnBuscar);
            
            btnDescargar = new JButton();
            btnDescargar.setToolTipText("Descargar Excel");
            btnDescargar.setBounds(635, 70, 30, 30);  /*coordenadas: x, y, ancho, largo*/
            btnDescargar.setIcon(new ImageIcon(getClass().getResource("/imagen/descarga.png")));
            btnDescargar.addActionListener(this);
            btnDescargar.setEnabled(false);
            panelInterfaz.add(btnDescargar);

            btnLimpiar = new JButton();
            btnLimpiar.setToolTipText("Limpiar");
            btnLimpiar.setBounds(635, 105, 30, 30);  /*COORDENADAS: X, Y, LARGO, ANCHO*/
            btnLimpiar.setIcon(new ImageIcon(getClass().getResource("/imagen/clean.png")));
            btnLimpiar.addActionListener(this);
            btnLimpiar.setEnabled(false);
            panelInterfaz.add(btnLimpiar);

            botonDetener = new JButton();
            botonDetener.setToolTipText("Detener");
            botonDetener.setBounds(635, 140, 30, 30);  /*coordenadas: x, y, ancho, largo*/
            botonDetener.setIcon(new ImageIcon(getClass().getResource("/imagen/finalizar.png")));
            botonDetener.addActionListener(this);
            botonDetener.setEnabled(false);
            //panelInterfaz.add(botonDetener);

            lcantidad = new JLabel("Tiempo de Ejecucion: ");
            lcantidad.setBounds(10, 422, 110, 20);
            panelInterfaz.add(lcantidad);
            
            lfecha = new JLabel("00:00:00");
            lfecha.setBounds(115, 422, 250, 20);
            panelInterfaz.add(lfecha);

            txtarea.setEditable(false);
            txtarea.setLineWrap(true);
            txtarea.setWrapStyleWord(true);
            
            txtarea.addMouseListener(new MouseAdapter() {
                public void mouseReleased(final MouseEvent e) {
                 if (e.isPopupTrigger()) {
                  final JPopupMenu menu = new JPopupMenu();
                  JMenuItem item;
                  item = new JMenuItem(new DefaultEditorKit.CopyAction());
                  item.setText("Copiar");
                  item.setEnabled(txtarea.getSelectionStart() != txtarea.getSelectionEnd());
                  menu.add(item);
                  menu.show(e.getComponent(), e.getX(), e.getY());
                 }
                }
               });
            
            scrollPane = new JScrollPane(txtarea);
            scrollPane.setBounds(10, 70, 620, 350);

            
            
            panelInterfaz.add(scrollPane);	
	}
	
	
	private boolean sonEspacios(String cad){
		 for(int i =0; i<cad.length(); i++)
		 if(cad.charAt(i) != ' ')
		 return false;
		 
		 return true;
	}


	private String getFechaHoraActual(){
            date = new Date();
            DateFormat hourdateFormat = new SimpleDateFormat("dd-MM-yyyy hhmmss");

            fecha = hourdateFormat.format(date);		
		
	    h = calendario.get(Calendar.HOUR);
	    m = calendario.get(Calendar.MINUTE);
	    
	    hora = h +":"+ m;
	    fechaHora = fecha;	
            
            return fechaHora;
	}
        
        private String getFechaActual(){
            date = new Date();
            DateFormat hourdateFormat = new SimpleDateFormat("dd-MM-yyyy");

            fecha = hourdateFormat.format(date);		
		
	    h = calendario.get(Calendar.HOUR);
	    m = calendario.get(Calendar.MINUTE);
	    
	    hora = h +":"+ m;
	    fechaHora = fecha;	
            
            return fechaHora;
	}

	public void actionPerformed(ActionEvent e) {		
		if(e.getSource()==botonDetener){
                    consulta.pararHIlo();
                    botonDetener.setEnabled(false);
		}

		if(e.getSource()==btnBuscar){
                    String enlace = txtfBuscar.getText();
                    String paginas = txtfPaginacion.getText();
                    //txtarea.setText("");
                    comboFiltro.setEnabled(false);
                    txtfBuscar.setEnabled(false);
                    btnLimpiar.setEnabled(false);
                    btnDescargar.setEnabled(false);
                    btnBuscar.setEnabled(false);
                    txtfPaginacion.setEnabled(false);
                    botonDetener.setEnabled(true);

                        if(sonEspacios(enlace)){
                            //if(sonEspacios(enlace))
                                JOptionPane.showMessageDialog(this,"Ingrese una Url");
                            
                            //if(sonEspacios(paginas))
                             //   JOptionPane.showMessageDialog(this,"Ingrese el numero maximo de paginas");
                            
                            comboFiltro.setEnabled(true);
                            txtfBuscar.setEnabled(true);
                            btnBuscar.setEnabled(true);
                            txtfPaginacion.setEnabled(true);
                            botonDetener.setEnabled(false);
                        }
                        else{
                            if((validar.isValid(enlace))){
                                /*    
                                if(idFiltro == 0){
                                    portal = "idealista.com";
                                }
                                else 
                                if(idFiltro == 1){
                                    portal = "fotocasa.es"; 
                                }
                                else 
                                if(idFiltro == 2){
                                    portal = "yaencontre.com";
                                }
                                else 
                                if(idFiltro == 3){
                                    portal = "milanuncios.com";
                                }
                                else 
                                if(idFiltro == 4){ 
                                    portal = "pisos.com";
                                }
                                else 
                                if(idFiltro == 5){ 
                                    portal = "vibbo.com";
                                }/**/
                                
                                if(validarPortal2(enlace)) {
                                //if(validarPortal(enlace, portal)) { 
                                    txtarea.setText("");
                                    txtarea.setText(res);
                                    
                                    consulta.setEnlace(enlace);
                                    consulta.setIdFiltro(idFiltro);
                                    
                                    if(sonEspacios(paginas)){
                                        consulta.setPaginas(1);
                                    }
                                    else{
                                        consulta.setPaginas(Integer.parseInt(paginas));
                                    }
                                        
                                    reiniciarCrono();
                                    
                                    consulta.iniciarHilo();
                                    doWork();
                                    
                                }/**/
                                //lcantidad.setText("Cantidad de Registros: " +Integer.toString(consulta.cantindadPublicaciones()));                                
                            }
                            else{
                                JOptionPane.showMessageDialog(this,"Ingrese una Url valida"); 
                                comboFiltro.setEnabled(true);
                                txtfBuscar.setEnabled(true);
                                btnBuscar.setEnabled(true);
                                txtfPaginacion.setEnabled(true);
                                botonDetener.setEnabled(false);
                            }
                        
                                            
                    }
		}
		
		if(e.getSource()==btnDescargar){
                    guardarArchivo(portal);
                    //txtarea.setText("");
                    //txtfBuscar.setText("");
		}
		if(e.getSource()==btnLimpiar){
			
			int seleccion = JOptionPane.showOptionDialog(
					   this,
					   "Esta Seguro de Eliminar la informacion extraida", 
					   "Seleccione opcion",
					   JOptionPane.YES_NO_CANCEL_OPTION,
					   JOptionPane.QUESTION_MESSAGE,
					   null,   // null para icono por defecto.
					   new Object[] { "Eliminar", "Cancelar"},   // null para YES, NO y CANCEL
					   "Cancelar");
					 
					if (seleccion != -1){

						if(seleccion == 0){
                                                    txtarea.setText("");
                                                    txtfBuscar.setText("");
                                                    enlaceAnt = "";
                                                    btnDescargar.setEnabled(false);
                                                    btnLimpiar.setEnabled(false);
                                                    lcantidad.setText("Cantidad de Registros: 0");
                                                    reiniciarCrono();
						}
					}
		}			
	}
        
        private void reiniciarCrono(){
            this.milesimas = 0;
            this.segundos = 0;
            this.minutos = 0;
        }
        
        private boolean validarPortal2(String url){
            if(url.equals(enlaceAnt) && consulta.isEstatus()){
                JOptionPane.showMessageDialog(this,"La Url ingresada es igual a la anterior, por favor limpie el cuadro de busqueda");
                comboFiltro.setEnabled(true);
                txtfBuscar.setEnabled(true);
                btnBuscar.setEnabled(true);
                txtfPaginacion.setEnabled(true);
                botonDetener.setEnabled(false);
                
                if(!sonEspacios(txtarea.getText())){
                    btnLimpiar.setEnabled(true);
                    btnDescargar.setEnabled(true); 
                    
                } 
                return false;
            }
            else{
                for(int i = 0; i < arrayPortales.length; i++){
                
                    if(url.toLowerCase().indexOf(arrayPortales[i].toLowerCase()) != -1){
                        portal = arrayPortales[i].toLowerCase();
                        idFiltro = i;
                        txtarea.setText("");
                        res = "Realizando Busqueda en : "+ url;
                        res += "\n\n"; 
                        enlaceAnt = url;
                        return true;
                    }     
                }
            
                JOptionPane.showMessageDialog(this,"Ingrese una Url valida");
                comboFiltro.setEnabled(true);
                txtfBuscar.setEnabled(true);
                btnBuscar.setEnabled(true);
                txtfPaginacion.setEnabled(true);
                botonDetener.setEnabled(false);
                return false;
            }
            
        }
        
        private boolean validarPortal(String url, String nombrePortal){
            
            //System.out.println(consulta.isEstatus());
            
            if(url.equals(enlaceAnt) && consulta.isEstatus()){
                JOptionPane.showMessageDialog(this,"La Url ingresada es igual a la anterior, por favor limpie el cuadro de busqueda");
                comboFiltro.setEnabled(true);
                txtfBuscar.setEnabled(true);
                btnBuscar.setEnabled(true);
                txtfPaginacion.setEnabled(true);
                botonDetener.setEnabled(false);
                
                if(!sonEspacios(txtarea.getText())){
                    btnLimpiar.setEnabled(true);
                    btnDescargar.setEnabled(true); 
                    
                } 
                return false;
            }
            /*else
                if(Integer.parseInt(paginas) <= 0){
                    JOptionPane.showMessageDialog(this,"Ingrese una cantidad de paginas mayor a 0");
                    comboFiltro.setEnabled(true);
                    txtfBuscar.setEnabled(true);
                    return false;
                }*/
            else{
                int resultado = url.toLowerCase().indexOf(nombrePortal.toLowerCase());
                txtarea.setText("");
                
                if(resultado != -1) {
                    txtarea.setText("");
                    res = "Realizando Busqueda en : "+ url;
                    res += "\n\n"; 
                    enlaceAnt = url; 
                    return true;
                }
                else{
                    //txtarea.setText("");
                    JOptionPane.showMessageDialog(this,"Ingrese una Url valida para el portal seleccionado");
                    comboFiltro.setEnabled(true);
                    txtfBuscar.setEnabled(true);
                    btnBuscar.setEnabled(true);
                    txtfPaginacion.setEnabled(true);
                    botonDetener.setEnabled(false);
                    return false;
                }
            }           
        }
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == comboFiltro){
			if (e.getStateChange() == ItemEvent.SELECTED) {
				idFiltro = comboFiltro.getSelectedIndex();
                                //txtarea.setText("");
                                enlaceAnt = "";
                                //lcantidad.setText("Cantidad de Registros: 0");
			}		
		}
		
	}
        
        private void guardarArchivo(String portal) {
            String nombre="";
            JFileChooser file=new JFileChooser();
            file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            file.showSaveDialog(this);
            File guarda =file.getSelectedFile();
            if(guarda !=null)
            {
                
                try {
                    consulta.downloadExcel(guarda.getAbsolutePath(), portal, getFechaHoraActual());
                    JOptionPane.showMessageDialog(null,
                            "El archivo se a guardado Exitosamente en la ruta " + guarda.getAbsolutePath(),
                            "Información",JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    Logger.getLogger(Vinterfaz.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WriteException ex) {
                    Logger.getLogger(Vinterfaz.class.getName()).log(Level.SEVERE, null, ex);
                }/**/
            }
            
        }
        
        protected void doWork() {
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Here not in the EDT
                while(consulta.seguirHilo()){
                   // System.out.println(consulta.seguirHilo());
                    // Simulates work
                    //Thread.sleep(10);
                    //System.out.println("hilo vivo");
                    publish(); // published values are passed to the #process(List) method
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                //try {
                    // chunks are values retrieved from #publish()
                    // Here we are on the EDT and can safely update the UI
                    //txtarea.setText(chunks.get(chunks.size() - 1).toString());
                    //Thread.sleep( 4 );
                    //Incrementamos 4 milesimas de segundo
                    milesimas += 4;
                    
                    //Cuando llega a 1000 osea 1 segundo aumenta 1 segundo
                    //y las milesimas de segundo de nuevo a 0
                    if( milesimas == 100 )
                    {
                        milesimas = 0;
                        segundos += 1;
                        //Si los segundos llegan a 60 entonces aumenta 1 los minutos
                        //y los segundos vuelven a 0
                        if( segundos == 60 )
                        {
                            segundos = 0;
                            minutos++;
                        }
                    }
                    
                    //Esto solamente es estetica para que siempre este en formato
                    //00:00:000
                    if( minutos < 10 ) min = "0" + minutos;
                    else min = minutos.toString();
                    if( segundos < 10 ) seg = "0" + segundos;
                    else seg = segundos.toString();
                    
                    if( milesimas < 10 ) mil = "00" + milesimas;
                    else if( milesimas < 100 ) mil = "0" + milesimas;
                    else mil = milesimas.toString();
                    
                    //Colocamos en la etiqueta la informacion
                    lfecha.setText( min + ":" + seg + ":" + mil );
                    
                    if(consulta.isActivo()){
                        txtarea.setText(res + consulta.imprimeResultados());
                        consulta.setActivo(false);
                        
                    }
                /*} catch (InterruptedException ex) {
                    Logger.getLogger(Vinterfaz.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                    
            }

            @Override
            protected void done() {
                // Invoked when the SwingWorker has finished
                // We are on the EDT, we can safely update the UI
                if(!sonEspacios(txtarea.getText())){
                    btnLimpiar.setEnabled(true);
                    btnDescargar.setEnabled(true);  
                   
                } 
                //txtarea.append("done");
                comboFiltro.setEnabled(true);
                txtfBuscar.setEnabled(true);
                btnBuscar.setEnabled(true);
                txtfPaginacion.setEnabled(true);
                    
                JOptionPane.showMessageDialog(null,"Busqueda Finalizada");
            }
        };
        worker.execute();
    }

   
}
