package com.tfg;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tfg.consumidor.Analizador;
import com.tfg.productor.App;
import com.tfg.ventanas.VentanaInicial;

public class AppTest {
	
	App app;
	@Before
	public void setUp() throws Exception {
		app = new App();
		app.setvI(new VentanaInicial());
		app.listaAnalizadores();
		
	}
	
	/**
	 * Comprueba que el analizador está en la lista de analizadores
	 */
	@Test
	public void iniciarAnalizadorInExistenteTest() {
		
		assertFalse(app.iniciarAnalizador(1));
		
	}
	
	/**
	 * Comprueba si al presionar el boton de parar analizador, tenía un Análisis iniciado
	 */
	
	@Test
	public void pararAnalizadorSinIniciarTest() {
		
		assertFalse(app.pararAnalizador());
	}	
	
}
