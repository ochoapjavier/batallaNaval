package es.uned.common;

/**
 * Esta clase contiene la partida y realiza las operaciones necesarias para su desarrollo.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.io.Serializable;
import java.util.ArrayList;


public class Partida implements Serializable{
	private ArrayList<Player> jugadores;
	private int idPartida;
	private int numJugadores;

	public Partida() {
		jugadores = new ArrayList<Player>();
		numJugadores = 0;
	}
	
	//Devuelve true si agrega un jugador a la partida de forma exitosa y false si no
	public boolean agregarJugador(String nombre) {
		if (this.getNumJugadores() < 2) {
			Player jugador = new Player();
			jugador.setNombre(nombre);
			jugadores.add(jugador);
			numJugadores ++;
			return true;
		} else {
			return false;
		}	
	}
	
	//Devuelve true si coloca el barco de forma exitosa y false si no
	public boolean colocarBarcos(String coordenadas1, String coordenadas2, String nombreJugador) {
		boolean colocados = false;
		for (int i = 0; i < this.jugadores.size(); i++) {
			if (this.jugadores.get(i).getNombre().equals(nombreJugador)){
				//Si el primero es válido
				if (this.jugadores.get(i).getTablero().colocarBarco(coordenadas1)) {
					//Si el segundo es válido
					if (this.jugadores.get(i).getTablero().colocarBarco(coordenadas2)) {
						colocados = true;
						return colocados;
					} else {
						colocados = false;
						this.jugadores.get(i).getTablero().iniciarTablero();
						this.jugadores.get(i).getTablero().reiniciarBarcos();
						return colocados;
					}
				} else {		
					//Si hay error al colocar el barco devuelve falso y reinicia tablero
					colocados = false;
					this.jugadores.get(i).getTablero().iniciarTablero();
					return colocados;
				}
			}
		}
		return colocados;
	}
	
	//Devuelve el resultado del disparo
	public String disparar(String coordenadas, String nombreJugador) {
		String resultadoDisparo = "";
		Player atacante = null;
		Player defensor = null;
		
		for (int i = 0; i < this.jugadores.size(); i++) {
			//Si es el otro jugador
			if (this.jugadores.get(i).getNombre().equals(nombreJugador)){		
				//Realiza un disparo en su tablero
				atacante = this.jugadores.get(i);
			} else {
				defensor = this.jugadores.get(i);
			}
		}
		
		//Funcionalidad añadida ME_RINDO
		if (coordenadas.equals("ME_RINDO")) {
			defensor.setPuntuacion(16);
			defensor.setGanador(true);
			resultadoDisparo = "RENDIDO";
			return resultadoDisparo;
		}
		
		if (defensor.getTablero().disparo(coordenadas) == 1) {
			for (int i = 0; i < defensor.getTablero().getBarcos().size(); i++) {
				if (defensor.getTablero().getBarcos().get(i).tocado(coordenadas)) {
					if(defensor.getTablero().getBarcos().get(i).hundido()) {
						resultadoDisparo ="HUNDIDO";
						atacante.sumarPuntuacion(1);
						//Si el que realiza el disparo es ganador
						if (atacante.isGanador()) {
							resultadoDisparo = "GANADOR";
							return resultadoDisparo;
						} else {
							return resultadoDisparo;
						}
						
					} else {
						resultadoDisparo ="TOCADO";
						atacante.sumarPuntuacion(1);
						return resultadoDisparo;
					}
				}
			}
		
		} 
		
		if (defensor.getTablero().disparo(coordenadas) == 2) {
			resultadoDisparo = "REPETIDO";
			return resultadoDisparo;
		}
		
		if (defensor.getTablero().disparo(coordenadas) == 0) {
			resultadoDisparo = "AGUA";
			return resultadoDisparo;
		}
		
		if (defensor.getTablero().disparo(coordenadas) == -1) {
			resultadoDisparo = "ERROR";
			return resultadoDisparo;
		}
		return resultadoDisparo;
	}
	
	public ArrayList<Player> getJugadores() {
		return jugadores;
	}

	public void setJugadores(ArrayList<Player> jugadores) {
		this.jugadores = jugadores;
	}
	
	public int getIdPartida() {
		return idPartida;
	}

	public void setIdPartida(int idPartida) {
		this.idPartida = idPartida;
	}
	
	public int getNumJugadores() {
		return numJugadores;
	}

	public void setNumJugadores(int numJugadores) {
		this.numJugadores = numJugadores;
	}

}
