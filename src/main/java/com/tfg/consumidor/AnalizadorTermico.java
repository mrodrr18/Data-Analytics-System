package com.tfg.consumidor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.tfg.ventanas.VentanaInicial;

public class AnalizadorTermico extends Analizador {

	public boolean despierta;
	private ArrayList <Double> nuevosDatos;
	private ArrayList<Double> tiempos;
	private float umbral_Temperatura;
	private VentanaInicial vI;
	private StringBuilder ejecucion;
	private Date ultimaFecha;
	private static Logger logger = LogManager.getLogger(AnalizadorTermico.class);
	
	public AnalizadorTermico(int tipo, ArrayList <Double> nuevosDatos, ArrayList<Double> fechas, Date ultimaFecha) {
		super(tipo, nuevosDatos);
		this.tipo= tipo;
		this.nuevosDatos = nuevosDatos;
		this.tiempos = fechas;
		this.despierta=false;
		this.ultimaFecha= ultimaFecha;
		configurarUmbrales();
		logger.info("Nuevo analizador Térmico");
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public float configurarUmbrales() {
		// TODO Auto-generated method stub

		this.umbral_Temperatura = 30;
		System.out.println("Umbral de temperatura configurado en "+ this.umbral_Temperatura+ " ºC.");
		if(vI !=null) {
			vI.areaTexto.append("Umbral de temperatura configurado en "+ this.umbral_Temperatura+ " ºC.\n");
			ejecucion.append("Umbral de temperatura configurado en "+ this.umbral_Temperatura+ " ºC.\n");
		}
		logger.info("Umbral de temperatura configurado en "+ this.umbral_Temperatura+ " ºC.");
		return this.umbral_Temperatura;
	}

	@Override
	//Devuelve true tambien si ha generado alerta
	public void realizarAnalisis() {
		// TODO Auto-generated method stub
		
		//MEDIA POR ENCIMA DEL UMBRAL
		//analisis_Media();
		
		//5 VALORES SEGUIDOS POR ENCIMA DEL UMBRAL
		//analisis_Valores_Consecutivos();
		System.out.println("Análisis de la tendencia de la temperatura mediante la regresión lineal.");
		vI.areaTexto.append("Análisis de la tendencia de la temperatura mediante la regresión lineal.\n");
		ejecucion.append("Análisis de la tendencia de la temperatura mediante la regresión lineal.\n");
		logger.info("Análisis de la tendencia de la temperatura mediante la regresión lineal.");
		analisis_Regresion_Lineal();
	
		
	}
	
	
	public boolean analisis_Regresion_Lineal() {
		// TODO Auto-generated method stub
		
		LinearRegression linear = new LinearRegression (this.tiempos, this.nuevosDatos );
		logger.info("Análisis de la temperatura por regresión lineal iniciado.");
		//Predice en los siguientes 30 minutos que temperatura habra

		Calendar cal = Calendar.getInstance();
		cal.setTime(this.ultimaFecha);
		
		//Desplegamos la fecha
		Date tempDate = cal.getTime();
		
		//Le cambiamos minutos sumando 30 para predecir
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+30);
		tempDate = cal.getTime();
		
		double resultado = linear.predict((double)tempDate.getTime());
		
		System.out.println("La predicción de temperatura dentro de 30 minutos será de "+ resultado + " según el análisis de regresión lineal.");
		vI.areaTexto.append("La predicción de temperatura dentro de 30 minutos será de "+ resultado + " según el análisis de regresión lineal.\n");
		ejecucion.append("La predicción de temperatura dentro de 30 minutos será de "+ resultado + " según el análisis de regresión lineal.\n");
		logger.info("La predicción de temperatura dentro de 30 minutos será de "+ resultado + " según el análisis de regresión lineal.");
		
		//Como la temperatura ha sido superior a la temperatura umbral llamo al metodo que genera la alerta.
		if(resultado > (double) this.umbral_Temperatura) {
			System.out.println("El resultado de la predicción: "+resultado+"ºC, supera el umbral "+this.umbral_Temperatura+"ºC. Se genera una alerta por correo electrónico.");
			vI.areaTexto.append("El resultado de la predicción: "+resultado+", supera el umbral "+this.umbral_Temperatura+"ºC. Se genera una alerta por correo electrónico.\n");
			ejecucion.append("La predicción de temperatura dentro de 30 minutos será de "+ resultado + " según el análisis de regresión lineal.\n");
			logger.info("El resultado de la predicción: "+resultado+", supera el umbral "+this.umbral_Temperatura+"ºC. Se genera una alerta por correo electrónico.");
			generarAlerta(resultado);
			
			return true;
			}else {
		
			System.out.println("La predicción de temperatura no ha superado el umbral "+this.umbral_Temperatura +"ºC");
			vI.areaTexto.append("La predicción de temperatura no ha superado el umbral "+this.umbral_Temperatura +"ºC.\n");
			ejecucion.append("La predicción de temperatura no ha superado el umbral "+this.umbral_Temperatura +"ºC.\n");
			logger.info("La predicción de temperatura no ha superado el umbral "+this.umbral_Temperatura +"ºC.\n");
			
			return false;
		}
	}
	
	public boolean generarAlerta(double resultado) {
		System.out.println("La predicción de temperatura supera el umbral, se generará una alerta por correo electrónico.");
		vI.areaTexto.append("La predicción de temperatura supera el umbral, se generará una alerta por correo electrónico.\n");
		ejecucion.append("La predicción de temperatura supera el umbral, se generará una alerta por correo electrónico.\n");
		logger.info("Para generar la alerta se crea un objeto Alerta y se llama al método send.");
		Alerta email = new Alerta();
		return email.send("Analizador Térmico", resultado);
	}

	@Override
	public void run() {
		
		System.out.println("INICIADO EL ANALIZADOR DE INERCIA TÉRMICA");
		vI.areaTexto.append("Iniciado el analizador de inercia térmica.\n");
		ejecucion.append("Iniciado el analizador de inercia térmica.\n");
		logger.info("Iniciado el hilo del analizador de inercia térmica.");
		
			while(true) {
				//Espera a que notifique con datos nuevos
				while(this.despierta==false)
			        synchronized(this) {
			          try {

						System.out.println("Analizador Térmico esperando a que haya nuevos datos");
						vI.areaTexto.append("Analizador Térmico esperando a que haya nuevos datos.\n");
						ejecucion.append("Analizador Térmico esperando a que haya nuevos datos.\n");
						logger.info("Analizador Térmico esperando a que haya nuevos datos.");
						wait();
			          } catch(InterruptedException e) {
			        	  logger.error("Error hilo analizador térmico en espera.");
			            throw new RuntimeException(e);
			          }
			        }
				//Despierta al hilo porque hay datos nuevos, analizamos la tendencia
				System.out.println("Analizador Térmico recibe notificación de nuevos datos.");
				vI.areaTexto.append("Analizador Térmico recibe notificación de nuevos datos.\n");
				ejecucion.append("Analizador Térmico recibe notificación de nuevos datos.\n");
				logger.info("Analizador Térmico recibe notificación de nuevos datos.");
				//configurarUmbrales();
				
				realizarAnalisis();
				
				try {
					System.out.println("Análisis finalizado. Espere 15 segundos para comprobar si hay nuevos datos del sensor.");
					vI.areaTexto.append("Análisis finalizado. Espere 15 segundos para comprobar si hay nuevos datos del sensor.\n");
					ejecucion.append("Análisis finalizado. Espere 15 segundos para comprobar si hay nuevos datos del sensor.\n");
					logger.info("Análisis finalizado. Espere 15 segundos para comprobar si hay nuevos datos del sensor.");
					Thread.sleep(15000);
					despierta=false;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.error("Error al esperar los 15 segundos al finalizar la ejecución del hilo");
					e.printStackTrace();
				}
				
				
			}	
		
		
		
		
	}

	/**
	 * @return the nuevosDatos
	 */
	public ArrayList<Double> getNuevosDatos() {
		return nuevosDatos;
	}

	/**
	 * @param nuevosDatos the nuevosDatos to set
	 */
	public void setNuevosDatos(ArrayList<Double> nuevosDatos) {
		this.nuevosDatos = nuevosDatos;
	}

	/**
	 * @return the tiempos
	 */
	public ArrayList<Double> getTiempos() {
		return tiempos;
	}

	/**
	 * @param tiempos the tiempos to set
	 */
	public void setTiempos(ArrayList<Double> tiempos) {
		this.tiempos = tiempos;
	}

	/**
	 * @return the umbral_Temperatura
	 */
	public float getUmbral_Temperatura() {
		return umbral_Temperatura;
	}

	/**
	 * @param umbral_Temperatura the umbral_Temperatura to set
	 */
	public void setUmbral_Temperatura(Float umbral_Temperatura) {
		this.umbral_Temperatura = umbral_Temperatura;
	}

	/**
	 * @return the vI
	 */
	public VentanaInicial getvI() {
		return vI;
	}

	/**
	 * @param vI the vI to set
	 */
	public void setvI(VentanaInicial vI) {
		this.vI = vI;
	}

	/**
	 * @return the ejecucion
	 */
	public StringBuilder getEjecucion() {
		return ejecucion;
	}

	/**
	 * @param ejecucion the ejecucion to set
	 */
	public void setEjecucion(StringBuilder ejecucion) {
		this.ejecucion = ejecucion;
	}

	/**
	 * @return the ultimaFecha
	 */
	public Date getUltimaFecha() {
		return ultimaFecha;
	}

	/**
	 * @param ultimaFecha the ultimaFecha to set
	 */
	public void setUltimaFecha(Date ultimaFecha) {
		this.ultimaFecha = ultimaFecha;
	};

}
