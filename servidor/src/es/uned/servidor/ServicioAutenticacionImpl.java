package es.uned.servidor;

/**
 * Esta clase implemente la interfaz ServicioAuntenticacionInterface.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.CallbackJugadorInterface;
import es.uned.common.ServicioAutenticacionInterface;


public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface {
	
	private Servidor servidor;
	
	public ServicioAutenticacionImpl(Servidor servidor) throws RemoteException { 
		super(); 
		this.servidor = servidor;
	}

	//Registra un nuevo usuario con un nombre no existente en el sistema
	public boolean registrar(String nombre, String password, CallbackJugadorInterface objCallbackCliente) throws RemoteException {
		if (servidor.getServDatos().registrarJugador(nombre, password)) {
			objCallbackCliente.imprimirMensaje("Usuario con nombre "+nombre+" registrado correctamente.");
			return true;
		} else {
			objCallbackCliente.imprimirMensaje("Error al registrar. El nombre "+nombre+" ya está en uso. Pruebe otro diferente");
			return false;
		}
	}
	
	//Realiza el login de un jugador previamente registrado
	public boolean login(String nombre, String password, CallbackJugadorInterface objCallbackCliente) throws RemoteException {
		boolean login = false;
		switch (servidor.getServDatos().login(nombre, password)) {
		case "ok":
			objCallbackCliente.imprimirMensaje("\nAuntenticación correcta. Bienvenido "+nombre);
			servidor.registrarCallback(nombre, objCallbackCliente);
			login = true;
			break;
		case "errorPass":
			objCallbackCliente.imprimirMensaje("\nError en la auntenticación. La contraseña introducida es incorrecta");
			login = false;
			break;
		case "errorUser":
			objCallbackCliente.imprimirMensaje("\nError en la auntenticación. El usuario introducido no está registrado");
			login = false;
			break;
		case "errorConectado":
			objCallbackCliente.imprimirMensaje("\nError en la auntenticación. El usuario introducido ya está conectado");
			login = false;
			break;
		default:
			throw new IllegalArgumentException("Valor inesperado: " + servidor.getServDatos().login(nombre, password));
		}
		return login;	
	}
	
	//Realiza el logout para cerrar sesión
	public void logout(String nombre, CallbackJugadorInterface objCallbackCliente) throws RemoteException{
		servidor.getServDatos().logout(nombre);
		servidor.eliminarRegistroCallback(nombre, objCallbackCliente);
		objCallbackCliente.imprimirMensaje("Cerrando sesión. Vuelve pronto "+nombre);
	}	

}
