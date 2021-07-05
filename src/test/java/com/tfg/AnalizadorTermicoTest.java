package com.tfg;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tfg.consumidor.AnalizadorTermico;
import com.tfg.consumidor.LinearRegression;
import com.tfg.productor.App;
import com.tfg.ventanas.VentanaInicial;

public class AnalizadorTermicoTest {

	AnalizadorTermico analizadorTermico;
	ArrayList <Double> nuevosDatos  = new ArrayList<Double> ();
	ArrayList<Double> tiempos = new ArrayList<Double> ();
	Date ultimaFecha;
	
	@Before
	public void setUp() throws InterruptedException  {
		
		analizadorTermico = new AnalizadorTermico(0,this.nuevosDatos, this.tiempos, this.ultimaFecha);
		analizadorTermico.setvI(new VentanaInicial());
		analizadorTermico.setEjecucion(new StringBuilder());
	}
	
	/**
	 * Comprueba que el umbral está en 30ºC al iniciar el análisis
	 */
	@Test
	public void umbralPorDefectoTest() {
		Assert.assertEquals(30.00, analizadorTermico.getUmbral_Temperatura(), 0.001);
	}
	
	/**
	 * El análisis de la tendencia supera el umbral
	 */
	@Test
	public void compruebaSuperaUmbralTest() {
		double fecha = Double.valueOf("1.62539038271E12");
		this.tiempos.add(Double.valueOf("1.62539038271E12"));
		this.nuevosDatos.add(40.8);
		this.ultimaFecha = new Date(tiempos.get(tiempos.size()-1).longValue());
		analizadorTermico.setUltimaFecha(ultimaFecha);
		
		assertTrue(analizadorTermico.analisis_Regresion_Lineal());
	}	
	/**
	 * El análisis de la tendencia no supera el umbral
	 */
	@Test
	public void compruebaNoSuperaUmbralTest() {
		double fecha = Double.valueOf("1.62539038271E12");
		this.tiempos.add(Double.valueOf("1.62539038271E12"));
		this.nuevosDatos.add(20.8);
		this.ultimaFecha = new Date(tiempos.get(tiempos.size()-1).longValue());
		analizadorTermico.setUltimaFecha(ultimaFecha);
		
		assertFalse(analizadorTermico.analisis_Regresion_Lineal());
	}
	/**
	 * Alerta generada correctamente
	 */
	@Test
	public void generaAlertaTest() {
		assertTrue(analizadorTermico.generarAlerta(20.0));
	}
}
