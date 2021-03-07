package es.uned.common;

/**
 * Esta interfaz realiza la comunicación con el Jugador.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
public interface CallbackJugadorInterface extends java.rmi.Remote{
	
	public void notificame(String mensaje) throws java.rmi.RemoteException;
	
	public void imprimirMensaje(String mensaje) throws java.rmi.RemoteException;
}
