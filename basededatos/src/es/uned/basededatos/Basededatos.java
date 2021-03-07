package es.uned.basededatos;

/**
 * Esta clase es la clase que levanta la Base de datos y realiza las operaciones de consulta en la misma
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import es.uned.common.Partida;

public class Basededatos {
	final static int PUERTO = 5555;
	private HashMap<String, String> jugadores;								//Nombre y pass
	private ArrayList<String> listaConectados;
	private ArrayList<Partida> partidas;
	private static String URLRegistro;
	
	
	public Basededatos() {
		this.jugadores = new HashMap<String, String>();
		this.listaConectados = new ArrayList<String>();
		this.partidas = new ArrayList<Partida>();
	}

	public static void main(String[] args) {
		try {
			Basededatos bbdd = new Basededatos();
			arrancarRegistro(PUERTO);
			ServicioDatosImpl servDatos = new ServicioDatosImpl(bbdd);
			String URLRegistro = "rmi://localhost:" + PUERTO + "/BBDD";
			setURLRegistro(URLRegistro);
			Naming.rebind(URLRegistro, servDatos); 
			bbdd.menu();
			
			
		} catch (Exception e) {
			System.out.println("Excepción en el constructor de Base de Datos: " +e);
			System.exit(0);
		}	 
	}
	
	//Imprime menú de inicio
	private static void imprimirMenu() {
		System.out.println("\nMENU BASE DE DATOS\n");
		System.out.println("1.- Información de la Base de Datos.");
		System.out.println("2.- Listar jugadores registrados (Sus puntuaciones).");
		System.out.println("3.- Salir.");
	}
	
	//Gestiona el menu, realizando la acción pertinente
	private void menu() {
		imprimirMenu();
		Scanner sc = new Scanner(System.in);
		int select = sc.nextInt();
		
		switch (select) {
		case 1:
			//Obtener información de la base de datos
			System.out.println();
			System.out.println(getURLRegistro());
			menu();
			break;
		case 2:
			//Listar jugadores
			System.out.println("LISTA DE JUGADORES");
			listarPuntuaciones();
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
	
	//Lista las puntuaciones guardadas
	private void listarPuntuaciones() {		
		List<String> keys = new ArrayList<String>(jugadores.keySet());
		
		for (int i = 0; i < keys.size(); i++) {
			for (int j = 0; j < partidas.size(); j++) {
				if (partidas.get(j).getNumJugadores() == 2 && (partidas.get(j).getJugadores().get(0).getPuntuacion() == 16 || partidas.get(j).getJugadores().get(1).getPuntuacion() == 16)) {
					if(partidas.get(j).getJugadores().get(0).getNombre().equals(keys.get(i))) {
						System.out.println("Puntuación de "+keys.get(i)+" de partida con id "+partidas.get(j).getIdPartida()+" es "+partidas.get(j).getJugadores().get(0).getPuntuacion());
					}
					if(partidas.get(j).getJugadores().get(1).getNombre().equals(keys.get(i))) {
						System.out.println("Puntuación de "+keys.get(i)+" de partida con id "+partidas.get(j).getIdPartida()+" es "+partidas.get(j).getJugadores().get(1).getPuntuacion());
					}
				}
			}
		}
	}
	
	//Arranca el registo
	private static void arrancarRegistro(int numPuertoRMI) throws RemoteException { 
		try { 
			Registry registro = LocateRegistry.getRegistry(numPuertoRMI); 
			registro.list(); 
			// Esta llamada lanza una excepción 
			// si el registro no existe 
		} 
		catch (RemoteException e) { 
			// No existe registro válido en el puerto 
			Registry registro = LocateRegistry.createRegistry(numPuertoRMI); 
		} 
	}

	public HashMap<String, String> getJugadores() {
		return jugadores;
	}
	
	public void setJugadores(HashMap<String, String> jugadores) {
		this.jugadores = jugadores;
	}
	
	public String getURLRegistro() {
		return URLRegistro;
	}

	public static void setURLRegistro(String uRLRegistro) {
		URLRegistro = uRLRegistro;
	}

	public ArrayList<Partida> getPartidas() {
		return partidas;
	}

	public void setPartidas(ArrayList<Partida> partidas) {
		this.partidas = partidas;
	}
	
	public ArrayList<String> getListaConectados() {
		return listaConectados;
	}

	public void setListaConectados(ArrayList<String> listaConectados) {
		this.listaConectados = listaConectados;
	}
}
