package com.tfg.productor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.tfg.consumidor.Analizador;
import com.tfg.consumidor.AnalizadorTermico;
import com.tfg.ventanas.VentanaInicial;

public class App 
{
	final static String DATABASE ="Temperaturas";
	 static Query query;
	 static QueryResult queryResult;
	 static InfluxDB influxDB;
	 final static Scanner sc = new Scanner(System.in);
	 public boolean salir = true;	 
	 private static VentanaInicial vI;
	 private Date ultimaFecha;
	 private ArrayList <Analizador> listaAnalizadores;
	 private static Logger logger = LogManager.getLogger(App.class);
	 public StringBuilder ejecucion = new StringBuilder();
	 
	    public static void main( String[] args ) throws InterruptedException {
	    	
			logger.info("Inicio del sistema");
			
    	SwingUtilities.invokeLater(new Runnable(){
			public void run(){
	        	try {
					vI= new VentanaInicial();
					vI.setVisible(true);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	
	        	
			}
    	});
        	
        	
    }
    public void iniciar(int tipo) {
    	
        	final String serverURL = "http://localhost:8086", username = "root", password = "root";
        	influxDB = InfluxDBFactory.connect(serverURL, username, password);
        	Pong response = null;
        	try {
        		response = influxDB.ping();
        		System.out.println("Conexión con la base de datos realizada correctamente");
        		vI.areaTexto.append("Conexión con la base de datos realizada correctamente.\n");
        		ejecucion.append("Conexión con la base de datos realizada correctamente.\n");
        		logger.info("Conexión con la base de datos realizada correctamente.");
        		iniciarAnalizador(tipo);
        	}catch(Exception e) {
        		 System.err.println("Error al conectar con la base de datos.");
        		 vI.areaTexto.append("Error al conectar con la base de datos.\n");
        		 ejecucion.append("Error al conectar con la base de datos.\n");
        		 logger.error("Error al conectar con la base de datos.", e.getCause());
        		 iniciar(tipo);
        		 
        	}
	}
    
    public  ArrayList<Analizador> listaAnalizadores() throws InterruptedException {
	    	
        	salir =false;
        	
        	listaAnalizadores = new ArrayList <Analizador>();
        	
        	//Aqui instancio los hilos de proceso de cada tipo de Analizador
        	ArrayList <Double> fechas = new ArrayList <Double>();
        	ArrayList <Double> datosTemperatura = new ArrayList <Double>();
        	AnalizadorTermico analizadorTemp = new AnalizadorTermico(0, datosTemperatura, fechas, ultimaFecha );
        	
        	
        	listaAnalizadores.add(analizadorTemp);
        	logger.info("Añadido Analizador de temperatura a la lista de Analizadores.");
        	
        	return listaAnalizadores;
        	
        	
    }
    
    private void iniciarAnalizador(int tipo) throws InterruptedException {
    	if(tipo==0) {
    		logger.info("Iniciando análisis de la temperatura");
    		AnalizadorTermico analizadorTemp = null;
    		for(int i=0; i<listaAnalizadores.size(); i++) {
    			if(listaAnalizadores.get(i).getTipo() == tipo && tipo==0) {
    				analizadorTemp =(AnalizadorTermico) listaAnalizadores.get(i);
    				analizarTemperatura(analizadorTemp);
    			}
    			
    		}
    		
    	}
    }
    private void analizarTemperatura(AnalizadorTermico analizadorTemp){
    	List<List<Object>> listaTemperatura = null;
    	List<List<Object>> listaTemperatura2;
    	ArrayList <Double> fechas = analizadorTemp.getTiempos();
    	ArrayList <Double> datosTemperatura = analizadorTemp.getNuevosDatos();
    	Calendar calendar;
    	this.setSalir(false);
    	
    	logger.info("Sentencia SQL SELECT a la base de datos de la medida temperatura");
    	
    		query= new Query("SELECT * FROM temperatura", DATABASE);
    	
    		queryResult = influxDB.query(query); 

    
    	listaTemperatura = listaTemperatura2 = queryResult.getResults().get(0).getSeries().get(0).getValues();

		
    	if(listaTemperatura.size()<10) {
    		System.out.println("No hay datos de temperatura almacenados suficientes. Mínimo 10. Se comprobará de nuevo en 30 segundos.");
    		vI.areaTexto.append("No hay datos de temperatura almacenados suficientes. Mínimo 10. Se comprobará de nuevo en 30 segundos.\n");
    		ejecucion.append("No hay datos de temperatura almacenados suficientes. Mínimo 10. Se comprobará de nuevo en 30 segundos.\n");
    		logger.warn("No hay datos de temperatura almacenados suficientes. Mínimo 10. Se comprobará de nuevo en 30 segundos.");
    		try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		analizarTemperatura(analizadorTemp);
    	}else {
    		if(analizadorTemp.getvI()==null) { //Solo inicia la primera vez que se da a iniciar análisis
				analizadorTemp.setvI(vI);
				analizadorTemp.setEjecucion(ejecucion);
				analizadorTemp.start();
    		}
			System.out.println("Iniciado Analizador de los datos de Temperatura, a continuación se comprobará si hay datos nuevos del sensor.");
			vI.areaTexto.append("Iniciado Analizador de los datos de Temperatura, a continuación se comprobará si hay datos nuevos del sensor.\n");
			ejecucion.append("Iniciado Analizador de los datos de Temperatura, a continuación se comprobará si hay datos nuevos del sensor.\n");
	    	
			logger.info("Inicio del análisis de la temperatura");
			
			for(int i=listaTemperatura2.size()-10; i< listaTemperatura2.size(); i++) {
				datosTemperatura.add((Double) listaTemperatura2.get(i).get(1));
				calendar = javax.xml.bind.DatatypeConverter.parseDateTime(listaTemperatura.get(i).get(0).toString());
				analizadorTemp.setUltimaFecha(calendar.getTime());
				fechas.add((double)calendar.getTimeInMillis());
			}
									
						System.out.println("Primer Análisis Térmico.");
						vI.areaTexto.append("Primer Análisis Térmico.\n");
						ejecucion.append("Primer Análisis Térmico.\n");
						logger.info("Primer Análisis Térmico.");
						
						synchronized(analizadorTemp) {
    						 analizadorTemp.despierta=true;
     						 logger.info("Notificación al Analizador de Temperatura para iniciar el cálculo de la tendencia.");        						 
    						 analizadorTemp.notify(); //Datos nuevos
						 }
    	}
						

    	while(!salir) {
    			
    			boolean nuevosDatos = false;
    			int pos = listaTemperatura.size()-10; //ULTIMOS X DATOS de la lista de temperatura anterior
    			int contador=listaTemperatura2.size()-10;
    			ultimaFecha = null;
    			datosTemperatura.clear();
    			fechas.clear();
    			logger.info("Fechas de cada medida en formato double para el análisis mediante la regresión lineal.");
    			for(int i=pos; i< listaTemperatura2.size(); i++) {
    				datosTemperatura.add((Double) listaTemperatura2.get(i).get(1));
    				calendar = javax.xml.bind.DatatypeConverter.parseDateTime(listaTemperatura.get(i).get(0).toString());
    				analizadorTemp.setUltimaFecha(calendar.getTime());
    				fechas.add((double)calendar.getTimeInMillis());
    			}
    			
    			
    			for(int i=pos; i< listaTemperatura.size(); i++) { //Comprueba ultimos pos datos de la tabla temperatura    					
    					if(!(listaTemperatura.get(i).get(0).equals(listaTemperatura2.get(contador).get(0)))) { 
    						//Si alguno no coincide con la lista anterior despierta al analizador
    						nuevosDatos = true;
    						
    						System.out.println("Hay datos nuevos del sensor. Comienza el Análisis Térmico.");
    						vI.areaTexto.append("Hay datos nuevos del sensor. Comienza el Análisis Térmico.\n");
    						ejecucion.append("Hay datos nuevos del sensor. Comienza el Análisis Térmico.\n");
    						logger.info("Hay datos nuevos del sensor. Comienza el Análisis Térmico.");
    						
    						synchronized(analizadorTemp) {
        						 analizadorTemp.despierta=true;
         						 logger.info("Notificación al Analizador de Temperatura para iniciar el cálculo de la tendencia.");        						 
        						 analizadorTemp.notify(); //Datos nuevos
    						 }
    						
    						break;
    					}
    					contador++; //Aumenta el contador para comprobar otra prosicion
    				
    			}
    			
    			if(nuevosDatos==false) {
    				System.out.println("No hay nuevos datos de temperatura en la base de datos.");
					vI.areaTexto.append("No hay nuevos datos de temperatura en la base de datos.\n");
					ejecucion.append("No hay nuevos datos de temperatura en la base de datos.\n");
					logger.info("No hay nuevos datos de temperatura en la base de datos.");
    			}
    			
    			nuevosDatos=false;
    			try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			System.out.println("Espere 15 segundos.");
				vI.areaTexto.append("Espere 15 segundos.\n");
				ejecucion.append("Espere 15 segundos.\n");
				logger.info("Espera de 15 segundos");
				
				for(int i=0; i<15; i++) {
					if(salir!=true) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						break;
					}
				}
    			
				analizadorTemp.despierta=false;
				logger.info("Analizador de temperatura \"durmiendo\"");
				listaTemperatura = listaTemperatura2; //Renueva la lista antigua con los datos nuevos
				
    			queryResult = influxDB.query(query);  //Coge datos de la base de datos 
    			logger.info("Nueva solicitud de datos para comprobar si hay datos nuevos.");
    			listaTemperatura2 = queryResult.getResults().get(0).getSeries().get(0).getValues();
    			 
			
    	}
    }
    
    public void pararAnalizador() {
    	//PONER VARIABLE TIPO PARA IMPRIMIR SI ES TIPO 0 QUE ES EL DE TEMPERATURA
    	if(salir==false) {
	    	setSalir(true);
	    	System.out.println("Análisis de Temperatura finalizado");
			vI.areaTexto.append("Análisis de Temperatura finalizado.\n");
			ejecucion.append("Análisis de Temperatura finalizado.\n");
			logger.info("Análisis de Temperatura finalizado.");
    	}else {
    		System.out.println("Primero debe iniciar el analizador de temperatura");
			vI.areaTexto.append("Primero debe iniciar el analizador de temperatura.\n");
			ejecucion.append("Primero debe iniciar el analizador de temperatura.\n");
			logger.warn("Primero debe iniciar el analizador de temperatura.");
    	}
    }
    
    public StringBuilder imprimeListaTemperaturas(){
    	final String serverURL = "http://localhost:8086", username = "root", password = "root";
    	List<List<Object>> listaTemperatura;
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'" , Locale.FRANCE);
    	influxDB = InfluxDBFactory.connect(serverURL, username, password);
    	Pong response = null;
    	logger.info("Llamada a la función que imprime la lista de los datos de temperatura almacenados.");
    	try {
    		response = influxDB.ping();
    		System.out.println("Conexión con la base de datos realizada correctamente");
    		vI.areaTexto.append("Conexión con la base de datos realizada correctamente.\n");
    		ejecucion.append("Conexión con la base de datos realizada correctamente.\n");
    		logger.info("Conexión con la base de datos realizada correctamente");
    	}catch(Exception e) {
    		 System.err.println("Error al conectar con la base de datos.");
    		 vI.areaTexto.append("Error al conectar con la base de datos.\n");
    		 ejecucion.append("Error al conectar con la base de datos.\n");
    		 
    	}
    	logger.info("Sentencia SQL SELECT a la base de datos de la medida temperatura");
    	query= new Query("SELECT * FROM temperatura", DATABASE);
    	queryResult = influxDB.query(query); 
    
    	listaTemperatura = queryResult.getResults().get(0).getSeries().get(0).getValues();
    	
    	StringBuilder listaimprimir = new StringBuilder();
    	Calendar calendar ;
    	
    	for(int i =0; i< listaTemperatura.size(); i++) {
    		calendar = javax.xml.bind.DatatypeConverter.parseDateTime(listaTemperatura.get(i).get(0).toString());
			System.out.println(calendar.getTime()+"    "+listaTemperatura.get(i).get(1)+" ºC    ");
			listaimprimir.append(calendar.getTime()+"    "+listaTemperatura.get(i).get(1)+" ºC    \n");
    	}
    	logger.info("Lista de datos de temperatura almacenada en el sistema, lista para imprimir.");

    	return listaimprimir;
    }
    
    public  void medidasAPDF() throws FileNotFoundException {
    	PdfDocument pdf = new PdfDocument(new PdfWriter("C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\\DatosTemperatura.pdf"));
        Document document = new Document(pdf);
        String line = "Medidas sensor de Temperatura\n";
        document.add(new Paragraph(line).setFontSize((float) 24.00).setMarginLeft((float)95.00));
     
        Table table = new Table(2,true);
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Dato"))).setBold();
        String arrayImprimeListaTemperaturas [] = imprimeListaTemperaturas().toString().split("    ");
        for(int i=0; i< arrayImprimeListaTemperaturas.length; i++)
        	table.addCell(new Cell().add(new Paragraph(arrayImprimeListaTemperaturas[i].replace("\n", ""))));
        document.add(table);
        document.close();

        System.out.println("PDF con las medidas de las temperaturas generado correctamente en la carpeta: \"C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\" .");
        vI.areaTexto.append("PDF con las medidas de las temperaturas generado correctamente en la carpeta: \"C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\" .\n");
        ejecucion.append("PDF con las medidas de las temperaturas generado correctamente en la carpeta: \"C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\" .\n");
        logger.info("PDF con las medidas de las temperaturas generado correctamente en la carpeta: \"C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\" .");
    }
    
    public void informacionEjecucionAPDF() throws FileNotFoundException{
    	PdfDocument pdf = new PdfDocument(new PdfWriter("C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\\InformacionEjecucion.pdf"));
        Document document = new Document(pdf);
        String line = "Información de la ejecución\n";
        document.add(new Paragraph(line).setFontSize((float) 24.00).setMarginLeft((float)95.00));
        
        document.add(new Paragraph(ejecucion.toString()));

        document.close();
        System.out.println("PDF con la informacion de la ejecucion generado correctamente en la carpeta: \"C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\" .");
        vI.areaTexto.append("PDF con la informacion de la ejecucion generado correctamente en la carpeta: \"C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\" .\n");
        ejecucion.append("PDF con la informacion de la ejecucion generado correctamente en la carpeta: \"C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\" .\n");
        logger.info("PDF con la informacion de la ejecucion generado correctamente en la carpeta: \"C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros\" .");
    	
    }
	/**
	 * @return the salir
	 */
	public boolean isSalir() {
		return salir;
	}
	
	/**
	 * @param salir the salir to set
	 */
	public void setSalir(boolean salir) {
		this.salir = salir;
	}
	/**
	 * @return the vI
	 */
	public static VentanaInicial getvI() {
		return vI;
	}
	/**
	 * @param vI the vI to set
	 */
	public static void setvI(VentanaInicial vI) {
		App.vI = vI;
	}
        
}
