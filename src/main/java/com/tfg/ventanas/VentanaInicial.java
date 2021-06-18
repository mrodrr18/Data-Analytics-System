package com.tfg.ventanas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.tfg.consumidor.Alerta;
import com.tfg.consumidor.Analizador;
import com.tfg.consumidor.AnalizadorTermico;
import com.tfg.productor.App;


@SuppressWarnings("serial")
public class VentanaInicial  extends JFrame	{
	
	public JPanel panel;
	public JTextArea areaTexto;
	private JTextField  cajaTexto;
	private App app = new App();
	private StringBuilder ejecucion = app.ejecucion;
	private ArrayList <Analizador> analizadores;
	private static Logger logger = LogManager.getLogger(VentanaInicial.class);
	
	public VentanaInicial() throws InterruptedException {
		
		this.setTitle("Data Analytics System");
		this.setSize(1000,600);
		this.setLocationRelativeTo(null); //Posicion de la ventana en el centro de la pantalla
		this.setMinimumSize(new Dimension(800,400));
		this.setResizable(false);
		//this.getContentPane().setBackground(Color.CYAN); //Pone color a la ventana 
		iniciarComponentes();		
		this.analizadores=app.listaAnalizadores();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void iniciarComponentes() throws InterruptedException {
		colocarPaneles();	
		colocarBotones();
		colocarAreasDeTexto();
	}
	
	private void colocarPaneles() {
		panel = new JPanel(); //Creacion del panel
		panel.setLayout(null); //Desactivamos el layout (diseño por defecto)
		
		this.getContentPane().add(panel); //Agregamos el panel a la ventana
		logger.info("Creado el panel de la ventana gráfica inicial.");
		//panel.setBackground(Color.CYAN); //Color de fondo del panel
	}
	
	private void colocarEtiquetas() {
		//Etiqueta tipo texto
				JLabel etiqueta = new JLabel(); //Etiqueta de texto
				etiqueta.setText("Ingrese su nombre:");//Establecemos el texto de la etiqueta
				etiqueta.setBounds(30, 10, 200, 30);
				//etiqueta.setHorizontalAlignment(SwingConstants.CENTER); //Alineacion horizontal del texto
				//etiqueta.setForeground(Color.WHITE); //Color de la letra
				//etiqueta.setOpaque(true); //Quitamos el transparente por defecto de la etiqueta
				//etiqueta.setBackground(Color.BLACK); //Cambiamos el color de fondo de la etiqueta
				etiqueta.setFont(new Font("arial",Font.BOLD,20));//Fuente del texto(arial, normal o negrita, tamaño)
				panel.add(etiqueta);
				
				
				//Etiqueta tipo imagen
				/*JLabel etiqueta2 = new JLabel();
				ImageIcon imagen = new ImageIcon("src/main/java/com/tfg/ventanas/grafico.png");
				etiqueta2.setBounds(80, 10, 210, 160);
				etiqueta2.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(etiqueta2.getWidth(), etiqueta2.getHeight(), Image.SCALE_SMOOTH)));
				
				panel.add(etiqueta2);*/
	}
	
	private void colocarBotones() {
		
		//Boton 1, boton de Grafana
		JButton botonGrafica = new JButton();
		botonGrafica.setBounds(740, 40, 180, 40);
		botonGrafica.setText("Gráfica (Grafana)"); //Establecer texto al boton
		//boton1.setEnabled(false); //Establece la interaccion del botón
		//boton1.setMnemonic('a'); //Boton del teclado (a) con el que se activa el boton 
		//boton1.setForeground(Color.BLUE); //Cambiar color de la letra del botón
		botonGrafica.setFont(new Font("arial", 0, 15)); //Fuente de la letra del botón
		panel.add(botonGrafica);
		logger.info("Añadido botón para acceder a Grafana en la interfaz gráfica inicial.");
		
		ActionListener abreGrafana = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(java.awt.Desktop.isDesktopSupported()) {
					java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
					if(desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
						java.net.URI uri;
						try {

							System.out.println("Visualización gráfica mediante Grafana de las medidas almacenadas.");
							areaTexto.append("Visualización gráfica mediante Grafana de las medidas almacenadas.\n");
							ejecucion.append("Visualización gráfica mediante Grafana de las medidas almacenadas.\n");
							logger.info("Visualización gráfica mediante Grafana en navegador de las medidas almacenadas.");
							
							uri = new java.net.URI("http://localhost:3000/d/lh4gCmg7k/temperatura?orgId=1");
							desktop.browse(uri);
						} catch (URISyntaxException | IOException e1) {
							// TODO Auto-generated catch block
							logger.error("Error en la visualización gráfica de las medidas almacenadas.");
							e1.printStackTrace();
						}
					}
				}
			}
		};
		botonGrafica.addActionListener(abreGrafana);
		
		
		//Boton 2, boton de ver medidas de temperatura que hay en la base de datos
		JButton botonVerTemperatura = new JButton();
		botonVerTemperatura.setBounds(740, 100, 180, 40);
		botonVerTemperatura.setText("Medidas Temperatura");
		botonVerTemperatura.setFont(new Font("arial", 0, 15)); //Fuente de la letra del botón
		/*ImageIcon imagen = new ImageIcon("src/main/java/com/tfg/ventanas/grafico.png");
		boton2.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(boton2.getWidth(), boton2.getHeight(), Image.SCALE_SMOOTH)));*/
		//boton2.setBackground(Color.BLUE); //Color de fondo del boton
		
		panel.add(botonVerTemperatura);
		logger.info("Añadido botón para ver en una ventana diferente las medidas guardadas de la temperatura.");
		
		ActionListener muestraTemperatura = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				VentanaMuestraMedidas vMM = new VentanaMuestraMedidas();
				vMM.setVisible(true);
				vMM.areaTexto.append(new App().imprimeListaTemperaturas().toString());
				System.out.println("Medidas de Temperatura mostradas en la interfaz de usuario.");
				areaTexto.append("Medidas de Temperatura mostradas en la interfaz de usuario.\n");
				ejecucion.append("Medidas de Temperatura mostradas en la interfaz de usuario.\n");
				logger.info("Medidas de Temperatura mostradas en la interfaz de usuario.");
			}
		};
		botonVerTemperatura.addActionListener(muestraTemperatura);
		
		//Boton 3, boton para pasar las medidas a pdf
		JButton botonTemperaturaPDF = new JButton();
		botonTemperaturaPDF.setBounds(720, 160, 220, 40);
		botonTemperaturaPDF.setText("Datos Temperatura en PDF");
		botonTemperaturaPDF.setFont(new Font("arial", 0, 15)); //Fuente de la letra del botón
		/*ImageIcon imagen = new ImageIcon("src/main/java/com/tfg/ventanas/grafico.png");
		boton2.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(boton2.getWidth(), boton2.getHeight(), Image.SCALE_SMOOTH)));*/
		//boton2.setBackground(Color.BLUE); //Color de fondo del boton
		
		panel.add(botonTemperaturaPDF);
		logger.info("Añadido botón para guardar en un PDF las medidas de la temepratura que hay almacenadas en la base de datos.");
		
		ActionListener temperaturaAPDF = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {

					System.out.println("Solicitud para crear el fichero pdf con las medidas de las temperaturas.");
					areaTexto.append("Solicitud para crear el fichero pdf con las medidas de las temperaturas.\n");
					ejecucion.append("Solicitud para crear el fichero pdf con las medidas de las temperaturas.\n");
					logger.info("Solicitud para crear el fichero pdf con las medidas de las temperaturas.");
					app.medidasAPDF();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println("Error al crear el fichero pdf con las medidas de las temperaturas.");
					areaTexto.append("Error al crear el fichero pdf con las medidas de las temperaturas.\n");
					ejecucion.append("Error al crear el fichero pdf con las medidas de las temperaturas.\n");
					logger.error("Error al crear el fichero pdf con las medidas de las temperaturas.");
				}
			}
		};
		botonTemperaturaPDF.addActionListener(temperaturaAPDF);
						
		//Boton 4, analizar medidas en tiempo real
		JButton analizarTemperatura = new JButton();
		analizarTemperatura.setBounds(30, 40, 220, 40);
		analizarTemperatura.setText("Analizar Temperatura");
		analizarTemperatura.setFont(new Font("arial", 0, 15)); //Fuente de la letra del botón
		/*ImageIcon imagen = new ImageIcon("src/main/java/com/tfg/ventanas/grafico.png");
		boton2.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(boton2.getWidth(), boton2.getHeight(), Image.SCALE_SMOOTH)));*/
		//boton2.setBackground(Color.BLUE); //Color de fondo del boton
		
		panel.add(analizarTemperatura);
		logger.info("Añadido botón para calcular la tendencia de las medidas de temperatura según la regresión lineal.");
				
		ActionListener iniciarAnalisis = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final SwingWorker worker = new SwingWorker(){

					@Override
					protected Object doInBackground() throws Exception {
						System.out.println("Iniciando el sistema de análisis de datos.");
						areaTexto.append("Iniciando el sistema de análisis de datos.\n");
						ejecucion.append("Iniciando el sistema de análisis de datos.\n");
						logger.info("Iniciando el sistema de análisis de datos.");
						app.iniciar(0);
						return null;
					}	
				};
				worker.execute();
				
			}
		};
		analizarTemperatura.addActionListener(iniciarAnalisis);
		
		//Boton 5, Parar analizador de temperatura
		JButton pararAnalizarTemperatura = new JButton();
		pararAnalizarTemperatura.setBounds(30, 100, 220, 40);
		pararAnalizarTemperatura.setText("Parar Analizador");
		pararAnalizarTemperatura.setFont(new Font("arial", 0, 15)); //Fuente de la letra del botón
		/*ImageIcon imagen = new ImageIcon("src/main/java/com/tfg/ventanas/grafico.png");
		boton2.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(boton2.getWidth(), boton2.getHeight(), Image.SCALE_SMOOTH)));*/
		//boton2.setBackground(Color.BLUE); //Color de fondo del boton
		
		panel.add(pararAnalizarTemperatura);
		logger.info("Añadido botón para pausar el análisis de las medidas de temepratura.");
		
		ActionListener pararAnalisis = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				app.pararAnalizador();
			}
		};
		pararAnalizarTemperatura.addActionListener(pararAnalisis);
		
		//Boton 6, actualizar umbral de temperatura
		JButton botonCambiarUmbral = new JButton();
		botonCambiarUmbral.setBounds(20, 160, 240, 40);
		botonCambiarUmbral.setText("Modificar Temperatura Umbral");
		botonCambiarUmbral.setFont(new Font("arial", 0, 15)); //Fuente de la letra del botón
				
		panel.add(botonCambiarUmbral);
		logger.info("Añadido botón para cambiar el umbral del análisis de temeperatura.");
		
		ActionListener actualizarUmbral = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				AnalizadorTermico aT = null; 
				for(int i =0; i< analizadores.size(); i++) {
					if(analizadores.get(i).getTipo()==0) {
						aT = (AnalizadorTermico) analizadores.get(i);
					}
				}
				
				VentanaCambiarTemperaturaUmbral vMM = new VentanaCambiarTemperaturaUmbral(areaTexto,ejecucion,aT);
				vMM.setVisible(true);
					
				
			}
		};
		botonCambiarUmbral.addActionListener(actualizarUmbral);
		
		//Boton 7, informacion de ejecucion en PDF
		JButton botonInfoAPDF = new JButton();
		botonInfoAPDF.setBounds(30, 220, 220, 40);
		botonInfoAPDF.setText("Exportar información a PDF");
		botonInfoAPDF.setFont(new Font("arial", 0, 15)); //Fuente de la letra del botón
		/*ImageIcon imagen = new ImageIcon("src/main/java/com/tfg/ventanas/grafico.png");
		boton2.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(boton2.getWidth(), boton2.getHeight(), Image.SCALE_SMOOTH)));*/
		//boton2.setBackground(Color.BLUE); //Color de fondo del boton
				
		panel.add(botonInfoAPDF);
		logger.info("Añadido botón para guardar en un PDF la información de la ejecución actual.");
		
		ActionListener infoAPDF = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					System.out.println("Solicitud para generar el archivo PDF con la información de la ejecución actual.");
					areaTexto.append("Solicitud para generar el archivo PDF con la información de la ejecución actual.\n");
					ejecucion.append("Solicitud para generar el archivo PDF con la información de la ejecución actual.\n");
					logger.info("Solicitud para generar el archivo PDF con la información de la ejecución actual.");
					app.informacionEjecucionAPDF();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println("Error al crear el fichero pdf con la información de la ejecución.");
					areaTexto.append("Error al crear el fichero pdf con la información de la ejecución.\n");
					ejecucion.append("Error al crear el fichero pdf con la información de la ejecución.\n");
					logger.error("Error al crear el fichero pdf con la información de la ejecución.");
				}
			}
		};
		botonInfoAPDF.addActionListener(infoAPDF);
		
		//Boton 8, abrir carpeta ficheros
				JButton botonAbrirCarpetaFicheros = new JButton();
				botonAbrirCarpetaFicheros.setBounds(350, 500, 240, 40);
				botonAbrirCarpetaFicheros.setText("Carpeta ficheros generados");
				botonAbrirCarpetaFicheros.setFont(new Font("arial", 0, 15)); //Fuente de la letra del botón
						
				panel.add(botonAbrirCarpetaFicheros);
				logger.info("Añadido botón para abrir la carpeta con los ficheros generados.");
				
				ActionListener abrirCarpeta = new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String folderName="C:\\Users\\mrrtr\\eclipse-workspace\\tfg\\ficheros";//Write your complete path here
						try {
						       Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + folderName);
						    } catch (IOException ex) {
						             logger.error("Error al abrir la carpeta de los ficheros");
						    }
						
					}
				};
				botonAbrirCarpetaFicheros.addActionListener(abrirCarpeta);
	}
	
	private void colocarRadioBotones() {
		JRadioButton radioBoton1 = new JRadioButton("Opción 1", true);
		radioBoton1.setBounds(50, 100, 100, 50);
		panel.add(radioBoton1);
		
		JRadioButton radioBoton2 = new JRadioButton("Opción 2", false);
		radioBoton2.setBounds(50, 150, 100, 50);
		panel.add(radioBoton2);
		
		JRadioButton radioBoton3 = new JRadioButton("Opción 3", false);
		radioBoton3.setBounds(50, 200, 100, 50);
		panel.add(radioBoton3);
		
		ButtonGroup grupoRadioBotones = new ButtonGroup();
		grupoRadioBotones.add(radioBoton1);
		grupoRadioBotones.add(radioBoton2);
		grupoRadioBotones.add(radioBoton3);
	}
	
	private void colocarCajasDeTexto() {
		cajaTexto = new JTextField();  //Caja de texto de una sola linea
		cajaTexto.setBounds(30, 50, 300, 30);
		//cajaTexto.setText("HOLA");
		//System.out.println("Texto en la caja: "+ cajaTexto.getText());
		panel.add(cajaTexto);
		
	}
	
	private void colocarAreasDeTexto() {
		areaTexto = new JTextArea();
		areaTexto.setSize(400,450);
		areaTexto.setLocation(270, 470);
		areaTexto.setLineWrap(true);
		areaTexto.setWrapStyleWord(true);
		//areaTexto.setText("Escriba el texto aquí");
		areaTexto.setEditable(false); //Para permitir editar o no		
		//System.out.println("texto: "+ areaTexto.getText());	
		DefaultCaret caret = (DefaultCaret)areaTexto.getCaret();
		 caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		panel.add(areaTexto);
		
		JScrollPane scrollingArea = new JScrollPane(areaTexto, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollingArea.setBounds(270, 40, 400, 450);
		panel.add(scrollingArea);
		logger.info("Añadida a la ventana inicial el area de texto donde se visualizará la información de la ejecución actual.");
		
		
	}
	
	private void colocarListasDesplegables() {
		String [] paises = {"Perú", "Colombia", "Paraguay", "Ecuador"};
		
		JComboBox listaDesplegable = new JComboBox(paises);
		listaDesplegable.setBounds(20,20,100,30);
		listaDesplegable.addItem("Argentina"); //Añadir objetos a lista desplegable
		listaDesplegable.setSelectedItem("Paraguay"); //Seleccionar primer objeto visto
		panel.add(listaDesplegable);
		
	}
}