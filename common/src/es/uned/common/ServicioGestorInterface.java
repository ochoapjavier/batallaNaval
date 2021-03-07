package es.uned.common;

/**
 * Esta clase permite al servidor comunicarse con la Base de Datos.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.Remote;

public interface ServicioGestorInterface extends Remote{
	
public void crearPartida(String nombre, CallbackJugadorInterface objCallback) throws java.rmi.RemoteException;

public void unirsePartida(int idPartida, String nombre, CallbackJugadorInterface objCallbackCliente) throws java.rmi.RemoteException;

public void listarPartidas(CallbackJugadorInterface objCallbackCliente) throws java.rmi.RemoteException;

public boolean colocarBarcos(String coordenadas1, String coordenadas2, String nombre, int idPartida, CallbackJugadorInterface objCallback) throws java.rmi.RemoteException;

public void disparar(String coordenadasDisparo, String nombre, int idPartida, CallbackJugadorInterface objCallback) throws java.rmi.RemoteException;

public void consultarPuntuacion(String nombre, CallbackJugadorInterface objCallback) throws java.rmi.RemoteException;
	
}
