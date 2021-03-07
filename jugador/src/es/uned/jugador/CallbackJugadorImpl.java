package es.uned.jugador;

/**
 * Esta clase implementa la interfaz CallbackJugadorInterface.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.CallbackJugadorInterface;


public class CallbackJugadorImpl extends UnicastRemoteObject implements CallbackJugadorInterface{
	
	private ListaSincronizada listaSincronizada;
	
	public CallbackJugadorImpl(ListaSincronizada listaEventos)  throws RemoteException {
		super();
		listaSincronizada = listaEventos;
	}
	
	//Notifica en la lista sincronizada un evento
	public void notificame(String mensaje) throws RemoteException { 
		listaSincronizada.addEvento(mensaje); 
	}
	
	//Imprime un mensaje
	public void imprimirMensaje(String mensaje) throws RemoteException {
		System.out.println(mensaje);
	}

}
