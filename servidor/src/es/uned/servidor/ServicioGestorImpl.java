package es.uned.servidor;

/**
 * Esta clase implemente la interfaz ServicioGestorInterface.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.CallbackJugadorInterface;
import es.uned.common.Partida;
import es.uned.common.ServicioGestorInterface;


public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface {
	
	private Servidor servidor;
	
	public ServicioGestorImpl(Servidor servidor) throws RemoteException {
		super();
		this.servidor = servidor;
	}

	//Crea una partida nueva
	public void crearPartida(String nombre, CallbackJugadorInterface objCallback) throws RemoteException {
		Partida partida = new Partida();
		partida.agregarJugador(nombre);
		int idPartida = servidor.getServDatos().addPartida(partida);
		objCallback.imprimirMensaje("A la espera de contrincante");
		objCallback.notificame(String.valueOf(idPartida));
		
	}
	
	//Se une a una partida ya existente introduciendo el ID de la partida
	public void unirsePartida(int idPartida, String nombre, CallbackJugadorInterface objCallbackCliente) throws RemoteException {
		switch (servidor.getServDatos().unirsePartida(idPartida, nombre)) {
			case "ok":
				objCallbackCliente.notificame("Colocar barcos");
				CallbackJugadorInterface objCallbackRival = getCallbackRival(idPartida, nombre);
				objCallbackRival.notificame("Colocar barcos");
				break;
			case "noPartidas":
				objCallbackCliente.imprimirMensaje("Error al unirse a la partida. No hay partidas disponibles");
				objCallbackCliente.notificame("Error al unirse");
				break;
			case "llena":
				objCallbackCliente.imprimirMensaje("Error al unirse a la partida. Partida llena");
				objCallbackCliente.notificame("Error al unirse");
				break;
			case "idError":
				objCallbackCliente.imprimirMensaje("Error al unirse a la partida, id incorrecto");
				objCallbackCliente.notificame("Error al unirse");
				break;
			
			default:
				throw new IllegalArgumentException("Unexpected value: " + servidor.getServDatos().unirsePartida(idPartida, nombre));
		}
	}

	//Lista las partidas disponibles para unirse
	public void listarPartidas(CallbackJugadorInterface objCallbackCliente) throws RemoteException {
		objCallbackCliente.imprimirMensaje(servidor.getServDatos().listarPartidas());
	}
	
	//Devuelve el Callback del jugador rival
	private CallbackJugadorInterface getCallbackRival(int idPartida, String nombre) throws RemoteException{
		CallbackJugadorInterface objCallbackRival = null;
		Partida partida = servidor.getServDatos().getPartida(idPartida);
		String nombreRival = null;
		
		for (int i = 0; i < partida.getNumJugadores(); i++) {
			if (!(partida.getJugadores().get(i).getNombre().equals(nombre))) {
				nombreRival = partida.getJugadores().get(i).getNombre();
			}
		}
		
		if (servidor.getJugadoresCallbacks().containsKey(nombreRival)) {
			objCallbackRival = servidor.getJugadoresCallbacks().get(nombreRival);
		}
		return objCallbackRival;
	}
	
	//Coloca los barcos en las coordenadas indicadas
	public boolean colocarBarcos(String coordenadas1, String coordenadas2, String nombre, int idPartida, CallbackJugadorInterface objCallback) throws RemoteException{
		boolean colocado = false;
		//Si colocar el barco 1 devuelve true
		if (servidor.getServDatos().colocarBarcos(coordenadas1, coordenadas2, nombre, idPartida)) {
			objCallback.imprimirMensaje("Esperando que el rival coloque sus barcos...");
			CallbackJugadorInterface objCallbackRival = getCallbackRival(idPartida, nombre);
			objCallbackRival.notificame("Comienza el juego");
			colocado = true;
			} else {
			objCallback.imprimirMensaje("Error en las coordenadas de los barcos. Vuelva a colocar sus barcos");
			colocado = false;
		}
		return colocado;
	}

	//Realiza un disparo
	public void disparar(String coordenadasDisparo, String nombre, int idPartida, CallbackJugadorInterface objCallback) throws RemoteException {
		
		String resultadoDisparo = servidor.getServDatos().disparar(coordenadasDisparo, nombre, idPartida);
		CallbackJugadorInterface objCallbackRival = getCallbackRival(idPartida, nombre);
		
		switch (resultadoDisparo) {
		case "RENDIDO":
			objCallback.notificame("Perdedor");
			objCallbackRival.imprimirMensaje("Tu rival se ha rendido.");
			objCallbackRival.notificame("Ganador");
			break;
		case "GANADOR":
			objCallback.notificame("Ganador");
			objCallbackRival.notificame("Perdedor");
			break;
		case "REPETIDO":
			objCallback.imprimirMensaje("\nDisparo REALIZADO a "+coordenadasDisparo+" REPETIDO. Revise sus disparos anteriores antes de realizar un nuevo disparo");
			objCallback.imprimirMensaje("Turno del rival...");
			objCallbackRival.imprimirMensaje("\nDisparo RECIBIDO en "+coordenadasDisparo+" REPETIDO. Su rival ha malgastado un turno\n");
			objCallback.notificame("Disparo realizado");
			objCallbackRival.notificame("Disparo recibido");
			break;
		case "TOCADO":
			objCallback.imprimirMensaje("\nDisparo REALIZADO a "+coordenadasDisparo+" "+resultadoDisparo);
			objCallback.imprimirMensaje("Turno del rival...");
			objCallbackRival.imprimirMensaje("\nDisparo RECIBIDO en "+coordenadasDisparo+" "+resultadoDisparo);
			objCallback.notificame("Disparo realizado");
			objCallbackRival.notificame("Disparo recibido");
			break;
		case "AGUA":
			objCallback.imprimirMensaje("\nDisparo REALIZADO a "+coordenadasDisparo+" "+resultadoDisparo);
			objCallback.imprimirMensaje("Turno del rival...");
			objCallbackRival.imprimirMensaje("\nDisparo RECIBIDO en "+coordenadasDisparo+" "+resultadoDisparo);
			objCallback.notificame("Disparo realizado");
			objCallbackRival.notificame("Disparo recibido");
			break;
		case "HUNDIDO":
			objCallback.imprimirMensaje("\nDisparo REALIZADO a "+coordenadasDisparo+" "+resultadoDisparo);
			objCallback.imprimirMensaje("Turno del rival...");
			objCallbackRival.imprimirMensaje("\nDisparo RECIBIDO en "+coordenadasDisparo+" "+resultadoDisparo);
			objCallback.notificame("Disparo realizado");
			objCallbackRival.notificame("Disparo recibido");
			break;		
		default:
			objCallback.imprimirMensaje("\nDisparo REALIZADO a "+coordenadasDisparo+" erróneo. Valor no permitido. Recuerde que la fila debe ser A-J y la columna 1-10");
			objCallback.imprimirMensaje("Turno del rival...");
			objCallbackRival.imprimirMensaje("\nDisparo RECIBIDO en "+coordenadasDisparo+" erróneo. Su rival ha malgastado un turno\n");
			objCallback.notificame("Disparo realizado");
			objCallbackRival.notificame("Disparo recibido");
			break;
		}
	}
	
	//Consulta la puntuación histórica del jugador
	public void consultarPuntuacion(String nombre, CallbackJugadorInterface objCallback) throws RemoteException {
		String puntuaciones = servidor.getServDatos().consultarPuntuacion(nombre);
		if (puntuaciones.equals("")) {
			objCallback.imprimirMensaje("No hay puntuaciones para "+nombre);
		}
		objCallback.imprimirMensaje(puntuaciones);
	}
	
}
