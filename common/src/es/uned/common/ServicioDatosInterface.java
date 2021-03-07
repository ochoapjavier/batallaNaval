package es.uned.common;

/**
 * Esta interfaz comunida el Servidor con la Base de Datos para consumir los datos de la misma.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.*;
import java.util.HashMap; 

public interface ServicioDatosInterface extends Remote {

	public boolean registrarJugador(String nombre, String password) throws RemoteException;

	public String login(String nombre, String password) throws java.rmi.RemoteException;
	
	public void logout(String nombre) throws java.rmi.RemoteException;
	
	public int addPartida(Partida partida) throws RemoteException;
	
	public String unirsePartida(int idPartida, String nombre) throws RemoteException;
	
	public String listarPartidas() throws java.rmi.RemoteException;
	
	public String estadoPartidas() throws java.rmi.RemoteException;
	
	public Partida getPartida(int idPartida) throws java.rmi.RemoteException;
	
	public boolean colocarBarcos (String coordenada1, String coordenada2, String nombre, int idPartida) throws java.rmi.RemoteException;
	
	public String disparar(String coordenada, String nombre, int idPartida) throws java.rmi.RemoteException;

	public String consultarPuntuacion(String nombre) throws java.rmi.RemoteException;
}

