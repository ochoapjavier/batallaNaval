package es.uned.common;

/**
 * Esta interfaz comunica al Jugador con el Servidor para las operaciones de auntenticación.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioAutenticacionInterface extends Remote{
	
	public boolean registrar(String nombre, String password, CallbackJugadorInterface objCallbackCliente) throws java.rmi.RemoteException;
	
	public boolean login(String nombre, String password, CallbackJugadorInterface objCallbackCliente) throws java.rmi.RemoteException;

	public void logout(String nombre, CallbackJugadorInterface objCallbackCliente) throws java.rmi.RemoteException;
	
		

	
}
