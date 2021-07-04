package com.tfg.ventanas;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.tfg.consumidor.AnalizadorTermico;
import com.tfg.productor.App;

public class VentanaCambiarTemperaturaUmbral extends JFrame{

	private JPanel panel;
	private JTextArea areaTexto;
	private JTextArea areaTextoInicial;
	private JFormattedTextField cajaNumero;
	private StringBuilder ejecucion;
	private static Logger logger = LogManager.getLogger(VentanaCambiarTemperaturaUmbral.class);
	AnalizadorTermico analizadorTermico;
	
	public VentanaCambiarTemperaturaUmbral(JTextArea areaTextoInicial, StringBuilder ejecucion, AnalizadorTermico analizadorTermico) {
		
		this.setTitle("Data Analytics System");
		this.setSize(400,200);
		this.setLocationRelativeTo(null); //Posicion de la ventana en el centro de la pantalla
		this.setMinimumSize(new Dimension(200,100));
		this.setResizable(false);
		this.areaTextoInicial = areaTextoInicial;
		this.ejecucion=ejecucion;
		this.analizadorTermico = analizadorTermico;
		//this.getContentPane().setBackground(Color.CYAN); //Pone color a la ventana 
		iniciarComponentes();
	}
	
	private void iniciarComponentes() {
		colocarPaneles();
		colocarAreasDeTexto();
		colocarEtiquetas();
		colocarCajasDeNumero();
		colocarBotones();
		
	}
	
	private void colocarPaneles() {
		panel = new JPanel(); //Creacion del panel
		panel.setLayout(null); //Desactivamos el layout (diseño por defecto)
		
		this.getContentPane().add(panel); //Agregamos el panel a la ventana
		//panel.setBackground(Color.CYAN); //Color de fondo del panel
		logger.info("Creado el panel de la ventana gráfica que cambia el umbral de temperatura.");
	}
	
	private void colocarEtiquetas() {
		//Etiqueta tipo texto
				JLabel etiqueta = new JLabel(); //Etiqueta de texto
				etiqueta.setText("Umbral:");//Establecemos el texto de la etiqueta
				etiqueta.setSize(100,20);
				etiqueta.setOpaque(false);
				etiqueta.setLocation(5, 60);
				//etiqueta.setHorizontalAlignment(SwingConstants.CENTER); //Alineacion horizontal del texto
				//etiqueta.setForeground(Color.WHITE); //Color de la letra
				//etiqueta.setOpaque(true); //Quitamos el transparente por defecto de la etiqueta
				//etiqueta.setBackground(Color.BLACK); //Cambiamos el color de fondo de la etiqueta
				etiqueta.setFont(new Font("arial",Font.BOLD,12));//Fuente del texto(arial, normal o negrita, tamaño)
				panel.add(etiqueta);
				logger.info("Añadida etiqueta relacionada con el cuadro de texto donde se introduce el umbral deseado.");

	}
	
	private void colocarAreasDeTexto() {
		areaTexto = new JTextArea();
		areaTexto.setSize(395,50);
		areaTexto.setOpaque(false);
		areaTexto.setLocation(5, 5);
		areaTexto.setLineWrap(true);
		areaTexto.setWrapStyleWord(true);
		areaTexto.setText("Indique la temperatura en grados centígrados que desee establecer como umbral de alerta.\nMínimo -30 ºC, Máximo 100 ºC\n");
		areaTexto.setEditable(false); //Para permitir editar o no		
		//System.out.println("texto: "+ areaTexto.getText());		
		panel.add(areaTexto);
		
		
		logger.info("Añadida a la ventana gráfica el area de texto con información sobre cómo cambiar el umbral.");
		
		
	}
	
	private void colocarCajasDeNumero() {
		try
		{
			DecimalFormat nf = new DecimalFormat("#.00");
	        NumberFormatter formatter = new NumberFormatter(nf);
	        formatter.setValueClass(Double.class);
	        formatter.setMinimum(new Double(-30.00));
	        formatter.setMaximum(new Double(100.1));
	        formatter.setAllowsInvalid(false);
		    cajaNumero = new JFormattedTextField(formatter);
		   cajaNumero.setBounds(130, 60, 50, 30);
			panel.add(cajaNumero);
			logger.info("Añadido el cuadro de texto donde se cambia el umbral de temperatura.");

		}
		catch (Exception e)
		{
			System.out.println("Error en el umbral de temperatura.");
			logger.error("Error en el umbral de temperatura.");
		}
	}
	
	private void colocarBotones() {
		//Boton 1, alctualizar umbral de temperatura
				JButton botonActualizarUmbral = new JButton();
				botonActualizarUmbral.setBounds(100, 90, 200, 20);
				botonActualizarUmbral.setText("Actualizar umbral");
				botonActualizarUmbral.setFont(new Font("arial", 0, 15)); 
						
				panel.add(botonActualizarUmbral);
				logger.info("Añadido botón para actualizar el umbral.");
				
				ActionListener cambioDeUmbral = new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						System.out.println("Umbral cambiado a: "+ cajaNumero.getText() +"ºC.");
						areaTextoInicial.append("Umbral cambiado a: "+ cajaNumero.getText() +"ºC.\n");
						ejecucion.append("Umbral cambiado a: "+ cajaNumero.getText() +"ºC.\n");
						logger.info("Umbral cambiado a: "+ cajaNumero.getText() +"ºC.");
					
						analizadorTermico.setUmbral_Temperatura(Float.valueOf(cajaNumero.getText().replaceAll(",", ".")));
						setVisible(false);
						dispose();
					}
				};
				botonActualizarUmbral.addActionListener(cambioDeUmbral);
	}
}

