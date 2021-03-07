package es.uned.basededatos;

/**
 * Esta clase implementa la interfaz ServicioDatosInterface.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.Partida;
import es.uned.common.ServicioDatosInterface;

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface{
	
	private Basededatos bbdd;
	
	public ServicioDatosImpl(Basededatos bbdd) throws RemoteException{
		super();
		this.bbdd = bbdd;
	}

	//Registra un jugador en la BBDD
	public boolean registrarJugador(String nombre, String password) throws RemoteException {
		boolean registrado;
		if (!(this.bbdd.getJugadores().containsKey(nombre))) {
			this.bbdd.getJugadores().put(nombre, password);
			registrado = true;
		} else {
			registrado = false;
		}
		return registrado;
	}

	//Devuelve el String resultado de loguear un jugador si las credenciales son correctas
	public String login(String nombre, String password) throws RemoteException {	
		String login = null;
		if (this.bbdd.getJugadores().containsKey(nombre)) {
			//Si la contraseña coincide
			if (this.bbdd.getJugadores().get(nombre).equals(password)) {
				//Si ya está conectado
				if (this.bbdd.getListaConectados().contains(nombre)) {
					login = "errorConectado";
				} else {
					login = "ok";
					this.bbdd.getListaConectados().add(nombre);	
				}	
			} else {
				login = "errorPass";
			}
		} else {
			login = "errorUser";
		}
		return login;
	}

	//Realiza el logout eliminando el jugador de la lista de conectados
	public void logout(String nombre) throws RemoteException {
		for (int i = 0; i < this.bbdd.getListaConectados().size(); i++) {
			if (this.bbdd.getListaConectados().get(i).equals(nombre)) {
				this.bbdd.getListaConectados().remove(i);
			}
		}	
	}

	//Añade partida al array de partidas de la base de datos y devulve el id de la partida creada
	public int addPartida(Partida partida) throws RemoteException {
		bbdd.getPartidas().add(partida);
		partida.setIdPartida(bbdd.getPartidas().size());
		return partida.getIdPartida();
	}
	
	//Devuelve el String resultado de unir al jugador con el nombre a la partida con el idPartida
	public String unirsePartida(int idPartida, String nombre) throws RemoteException{
		String unido = null;
		
		//Si no hay ninguna partida
		if (bbdd.getPartidas().size() == 0) {
			unido = "noPartidas";
		}
		
		for (int i = 0; i < bbdd.getPartidas().size(); i++) {
			//Si coincide el idPartida pasado con alguno existente
			if (bbdd.getPartidas().get(i).getIdPartida() == idPartida) {
				//Si se añade el jugador con éxito
				if (bbdd.getPartidas().get(i).agregarJugador(nombre)) {
					unido = "ok";
					break;
				} else {
					unido = "llena";
				}
			} else {
				unido = "idError";
			}
		}
		return unido;
	}

	//Devuelve un String con las partidas disponibles para unirse
	public String listarPartidas() throws RemoteException {
		String partidas = "";
		for (int i = 0; i < bbdd.getPartidas().size(); i++) {
			//Si tiene un jugador
			if (bbdd.getPartidas().get(i).getNumJugadores() == 1) {
				partidas = partidas.concat("\nPartida a la espera de contrincante con id: "+ bbdd.getPartidas().get(i).getIdPartida()+". Jugador propietario: "+bbdd.getPartidas().get(i).getJugadores().get(0).getNombre());
			}
		}	
		return partidas;
	}
	
	//Devuelve un String con las partidas y su estado
	public String estadoPartidas() throws RemoteException {
		String partidas = "";
		for (int i = 0; i < bbdd.getPartidas().size(); i++) {
			if (bbdd.getPartidas().get(i).getNumJugadores() == 2 && (bbdd.getPartidas().get(i).getJugadores().get(0).getPuntuacion() < 16 && bbdd.getPartidas().get(i).getJugadores().get(1).getPuntuacion() < 16)) {
				partidas = partidas.concat("\nPartida en juego con ID: "+ bbdd.getPartidas().get(i).getIdPartida()+". Jugador 1: "+bbdd.getPartidas().get(i).getJugadores().get(0).getNombre()+" con puntuación "+bbdd.getPartidas().get(i).getJugadores().get(0).getPuntuacion()+". Jugador 2: "+bbdd.getPartidas().get(i).getJugadores().get(1).getNombre()+" con puntuación "+bbdd.getPartidas().get(i).getJugadores().get(1).getPuntuacion());
			}
		}	
		return partidas;
	}
	
	//Devuelve la partida a partide de su id
	public Partida getPartida(int idPartida) {
		Partida partida = new Partida();
		for (int i = 0; i < this.bbdd.getPartidas().size(); i++) {
			if (this.bbdd.getPartidas().get(i).getIdPartida() == idPartida) {
				partida = this.bbdd.getPartidas().get(i);
			}
		}
		return partida;
	}
		
	//Devuelve true si la acción colocar barco ha tenido éxito a partir de los parámetros y false si no.
	public boolean colocarBarcos(String coordenada1, String coordenada2, String nombre, int idPartida) {
		boolean colocado = false;
		for (int i = 0; i < this.bbdd.getPartidas().size(); i++) {
			if (this.bbdd.getPartidas().get(i).getIdPartida() == idPartida) {				
				if (this.bbdd.getPartidas().get(i).colocarBarcos(coordenada1, coordenada2, nombre)) {
					colocado = true;
				} else {
					colocado = false;
				}
			}
		}
		return colocado;
	}
	
	//Realiza el disparo y devuelve el resultado del mismo
	public String disparar(String coordenada, String nombre, int idPartida) {
		String resultadoDisparo = "";
		
		for (int i = 0; i < this.bbdd.getPartidas().size(); i++) {
			if (this.bbdd.getPartidas().get(i).getIdPartida() == idPartida) {				
				resultadoDisparo = this.bbdd.getPartidas().get(i).disparar(coordenada, nombre);
			}
		}
		return resultadoDisparo;
		
	}
	
	//Devuelve un String con la puntuación para ese nombre
	public String consultarPuntuacion(String nombre) {
		int puntuacion = 0;
		String puntuacionToString = "";
		String puntuacionPartida = null;
		for (int i = 0; i < this.bbdd.getPartidas().size(); i++) {
			for (int j = 0; j < this.bbdd.getPartidas().get(i).getJugadores().size(); j++) {
				if (this.bbdd.getPartidas().get(i).getJugadores().get(j).getNombre().equals(nombre)) {
					puntuacion = this.bbdd.getPartidas().get(i).getJugadores().get(j).getPuntuacion();
					puntuacionPartida = "Puntuación de "+nombre+" de la partida con id "+this.bbdd.getPartidas().get(i).getIdPartida()+": "+String.valueOf(puntuacion)+"\n";
					puntuacionToString = puntuacionToString.concat(puntuacionPartida);
				}
			}
		}
		return puntuacionToString;
	}
}
