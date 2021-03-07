package es.uned.common;

/**
 * Esta clase contiene un jugador que formará parte de la partida, guardando la puntuación y determinando cuando gana.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable{

	private String nombre;
	private Tablero tablero;
	private int puntuacion;
	private boolean ganador;
	
	public Player() {
		this.tablero = new Tablero();
		this.puntuacion = 0;
		this.ganador = false;
	}
	
	//Suma la puntuación, actualizandola
	public void sumarPuntuacion(int puntos) {
		this.puntuacion = puntuacion + puntos;
		if (this.puntuacion == 6) {
			setGanador(true);
			sumarPuntuacion(10);
		}
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}

	public boolean isGanador() {
		return ganador;
	}

	public void setGanador(boolean ganador) {
		this.ganador = ganador;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tablero getTablero() {
		return tablero;
	}

	public void setTablero(Tablero tablero) {
		this.tablero = tablero;
	}
	
}
