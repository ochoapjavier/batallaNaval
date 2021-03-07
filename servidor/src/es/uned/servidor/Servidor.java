package es.uned.servidor;

/**
 * Esta clase levanta el servidor y realiza las operaciones de consulta sobre el mismo
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Scanner;

import es.uned.common.CallbackJugadorInterface;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.ServicioGestorInterface;


public class Servidor {
	
	private ServicioDatosInterface servDatos;
	private ServicioAutenticacionInterface servAutenticacionS;
	private ServicioGestorInterface servGestorS;
	final int puertoEntrada = 5555;
	final int puertoSalida = 5556;
	final String URL_BBDD = "rmi://localhost:" + puertoEntrada + "/BBDD";
	final String URL_Autenticacion = "rmi://localhost:" + puertoSalida + "/Auntenticacion";
	final String URL_Gestor = "rmi://localhost:" + puertoSalida + "/Gestor";
	private HashMap<String, CallbackJugadorInterface> jugadoresCallbacks;
	
	public Servidor() {
		try {
			this.jugadoresCallbacks = new HashMap<String, CallbackJugadorInterface>();
			this.servGestorS = new ServicioGestorImpl(this);
			this.servAutenticacionS = new ServicioAutenticacionImpl(this);
			Registry registroBBDD = arrancarRegistro(puertoEntrada);	
			Registry registroJugador = arrancarRegistro(puertoSalida);
			Naming.rebind(URL_Gestor, servGestorS);
			Naming.rebind(URL_Autenticacion, servAutenticacionS);
			servDatos = (ServicioDatosInterface) Naming.lookup(URL_BBDD);			
		} catch (Exception e) {
			System.out.println("Excepción en el constructor de Servidor: " +e);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		Servidor servidor = new Servidor();
		servidor.menu();
	}
	
	//Imprime el menu inicial
	private void imprimirMenu() {
		System.out.println("\nMENU SERVIDOR\n");
		System.out.println("1.- Información del Servidor.");
		System.out.println("2.- Estado de las partidas que se están jugando en este momento");
		System.out.println("3.- Salir.");
	}
	
	//Gestiona el menu inicial
	private void menu() {
		imprimirMenu();
		Scanner sc = new Scanner(System.in);
		int select = sc.nextInt();
		while (select != 3) {
			switch (select) {
			case 1:
				//Obtener información de la base de datos
				System.out.println("\nINFORMACIÓN DEL SERVIDOR");
				System.out.println(URL_BBDD);
				System.out.println(URL_Gestor);
				System.out.println(URL_Autenticacion);
				System.out.println();
				menu();
				break;
			case 2:
				//Listar jugadores
				System.out.println("LISTA DE ESTADO DE LAS PARTIDAS");
				try {
					System.out.println(this.getServDatos().estadoPartidas());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				menu();
				break;
			case 3:
				//Obtener información de la base de datos
				System.exit(1);
				break;
			default:
				System.out.println("El valor introducido debe ser entre 1 y 3 y ha sido "+select+". Vuelva a intentarlo.");
				menu();
			}
		}
	}
	
	//Arranca el registro
	private Registry arrancarRegistro(int numPuertoRMI) throws RemoteException { 
		Registry registro = null;
		try { 
			registro = LocateRegistry.getRegistry(numPuertoRMI); 
			registro.list(); 
			// Esta llamada lanza una excepción 
			// si el registro no existe 
		} 
		catch (RemoteException e) { 
			// No existe registro válido en el puerto 
			registro = LocateRegistry.createRegistry(numPuertoRMI); 
		}
		return registro;
	}
	
	//Registra el callback
	public void registrarCallback(String nombre, CallbackJugadorInterface objCallbackCliente) throws java.rmi.RemoteException { 
		// almacena el objeto callback en el vector 
		if (!(jugadoresCallbacks.containsKey(nombre))) { 
			jugadoresCallbacks.put(nombre, objCallbackCliente); 
		} 
	} 

	public HashMap<String, CallbackJugadorInterface> getJugadoresCallbacks() {
		return jugadoresCallbacks;
	}

	public void setJugadoresCallbacks(HashMap<String, CallbackJugadorInterface> jugadoresCallbacks) {
		this.jugadoresCallbacks = jugadoresCallbacks;
	}

	public synchronized void eliminarRegistroCallback(String nombre, CallbackJugadorInterface objCallbackCliente) throws java.rmi.RemoteException{ 
		if (jugadoresCallbacks.remove(nombre, objCallbackCliente)){ 
			objCallbackCliente.notificame("Eliminado del servicio de Callbacks");
		}
	} 
	
	public ServicioDatosInterface getServDatos() {
		return servDatos;
	}
	
	public void setServDatos(ServicioDatosInterface servDatos) {
		this.servDatos = servDatos;
	}
	
	public ServicioAutenticacionInterface getServAutenticacionS() {
		return servAutenticacionS;
	}
	
	public void setServAutenticacionS(ServicioAutenticacionInterface servAutenticacionS) {
		this.servAutenticacionS = servAutenticacionS;
	}
	
	public ServicioGestorInterface getServGestorS() {
		return servGestorS;
	}
	public void setServGestorS(ServicioGestorInterface servGestorS) {
		this.servGestorS = servGestorS;
	}
}
