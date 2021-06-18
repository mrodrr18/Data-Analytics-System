package com.tfg.ventanas;

import java.awt.Dimension;

import javax.swing.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class VentanaMuestraMedidas extends JFrame{
	
	public JPanel panel;
	public JTextArea areaTexto;
	private static Logger logger = LogManager.getLogger(VentanaMuestraMedidas.class);
	
	public VentanaMuestraMedidas() {
		
		this.setTitle("Data Analytics System");
		this.setSize(1000,600);
		this.setLocationRelativeTo(null); //Posicion de la ventana en el centro de la pantalla
		this.setMinimumSize(new Dimension(800,400));
		this.setResizable(false);
		//this.getContentPane().setBackground(Color.CYAN); //Pone color a la ventana 
		iniciarComponentes();
	}
	
	private void iniciarComponentes() {
		colocarPaneles();

		colocarAreasDeTexto();
		
	}
	
	private void colocarPaneles() {
		panel = new JPanel(); //Creacion del panel
		panel.setLayout(null); //Desactivamos el layout (diseño por defecto)
		
		this.getContentPane().add(panel); //Agregamos el panel a la ventana
		//panel.setBackground(Color.CYAN); //Color de fondo del panel
		logger.info("Creado el panel de la ventana gráfica que muestra las medidas de temperatura.");
	}
	private void colocarAreasDeTexto() {
		areaTexto = new JTextArea();
		areaTexto.setSize(400,40);
		areaTexto.setLocation(270, 470);
		areaTexto.setLineWrap(true);
		areaTexto.setWrapStyleWord(true);
		//areaTexto.setText("Escriba el texto aquí");
		areaTexto.setEditable(false); //Para permitir editar o no		
		//System.out.println("texto: "+ areaTexto.getText());		
		panel.add(areaTexto);
		
		JScrollPane scrollingArea = new JScrollPane(areaTexto, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollingArea.setBounds(270, 40, 400, 470);
		panel.add(scrollingArea);
		logger.info("Añadida a la ventana gráfica el area de texto donde se visualizarán las medidas de temperatura almacenadas.");
		
		
	}
}
