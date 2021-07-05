package com.tfg.consumidor;

import java.util.ArrayList;

public abstract class Analizador extends Thread{
	public int tipo;
	private ArrayList <Double> nuevosDatos;
	
	
	public Analizador(int tipo, ArrayList <Double> nuevosDatos) {
		
		this.tipo = tipo;
		this.nuevosDatos = nuevosDatos;
	}
	public abstract void realizarAnalisis();
	
	public abstract float configurarUmbrales();
	
	private void generarAlerta() {};
	
	public abstract void run();
	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	private void hola() {
		
	}
	//********************************************** PARA TESTCrear variable de estado boolean y guardo si ha enviado alerta
}
