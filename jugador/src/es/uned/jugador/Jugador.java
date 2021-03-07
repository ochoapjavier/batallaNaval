package es.uned.jugador;

/**
 * Esta clase actúa como cliente, comunicando al Jugador con el Servidor, para acceder a la aplicación y utilizarla.
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

import es.uned.common.CallbackJugadorInterface;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioGestorInterface;



public class Jugador {
	final static int puerto = 5556;
	private ServicioAutenticacionInterface servAutenticacionJ;
	private ServicioGestorInterface servGestorJ;
	final String URL_Autenticacion = "rmi://localhost:" + puerto + "/Auntenticacion";
	final String URL_Gestor = "rmi://localhost:" + puerto + "/Gestor";
	private String nombre;
	public static ListaSincronizada ListaEventos;
	private String evento;
	
	
	public Jugador() {
		try {		
			servAutenticacionJ = (ServicioAutenticacionInterface) Naming.lookup(URL_Autenticacion);			
		} catch (Exception e) {
			System.out.println("Excepción en el constructor de Jugador: " +e);
			System.exit(0);
		}
	}
	

	public static void main(String[] args) throws RemoteException {
		Jugador jugador = new Jugador();	
		ListaEventos = new ListaSincronizada();
		CallbackJugadorInterface objCallback = new CallbackJugadorImpl(ListaEventos); 
		jugador.menuInicial(objCallback);
	}
	
	//Gestiona el menu incial
	private void menuInicial(CallbackJugadorInterface objCallback) {
		Scanner teclado = new Scanner (System.in);
		int select = 0;
		System.out.println("\nMENU BIENVENIDA\n");
		System.out.println("1.- Registrar un nuevo jugador.");
		System.out.println("2.- Hacer login.");
		System.out.println("3.- Salir.");	
		try {
			select = teclado.nextInt();
		} catch (Exception e) {
			System.out.println("ERROR: Valor introducido con formato erroneo. Recuerde que debe ser 1, 2 o 3. Vuelve a intentarlo");
			menuInicial(objCallback);
		}
		
		if (select < 1 || select > 3) {
			System.out.println("ERROR: El valor introducido ha sido "+select+". Vuelva a intentarlo");
			menuInicial(objCallback);
		}
		switch (select) {
		case 1:
			registrarConsola(objCallback);
			menuInicial(objCallback);
			menuInterfazJugador(objCallback);
			break;
		case 2:
			loginConsola(objCallback);
			menuInterfazJugador(objCallback);
			break;
		case 3:
			System.out.println("Cerrando el programa");
			//Hacer logout
			System.exit(1);
			break;
		default:	
			System.out.println("El valor introducido debe ser entre 1 y 3, y ha sido " + select);
			System.exit(0);
		}
	}
	
	//Hace el registro por la consola
	private void registrarConsola(CallbackJugadorInterface objCallback) {
		String password;
		Scanner teclado = new Scanner(System.in);
		
		System.out.println("\nREGISTRO DE JUGADOR\n");
		System.out.println("Introduzca su nombre");
		this.setNombre(teclado.next());
		System.out.println("Por favor " + this.getNombre() + " introduzca su contraseña");
		password = teclado.next();
		try {
			this.servAutenticacionJ.registrar(this.getNombre(), password, objCallback);
		} catch (RemoteException e) {
			System.out.println("Fallo al conectar para registrar el usuario");
		}
	}
	
	//Hace el login por la consola
	private void loginConsola(CallbackJugadorInterface objCallback) {
		String password;
		Scanner teclado = new Scanner (System.in);
		
		System.out.println("\nLOGIN\n");
		System.out.println("Introduzca su nombre");
		this.setNombre(teclado.next());
		System.out.println("Por favor " + this.getNombre() + " introduzca su contraseña");
		password = teclado.next();
		try {	
			if (this.servAutenticacionJ.login(nombre, password, objCallback)) {
				this.servGestorJ = (ServicioGestorInterface) Naming.lookup(URL_Gestor);
				menuInterfazJugador(objCallback);			
			} else {
				menuInicial(objCallback);
			}
		} catch (Exception e) {
			System.out.println("Fallo al conectar para realizar el login. Excepción" +e);
		}
	}

	//Gestiona el menú de interfaz del jugador
	private void menuInterfazJugador(CallbackJugadorInterface objCallback) {
		Scanner sc = new Scanner(System.in);
		System.out.println("\nINTERFAZ TEXTO JUGADOR\n");
		System.out.println("1.- Información del jugador (consultar puntuación histórica).");
		System.out.println("2.- Iniciar una partida.");
		System.out.println("3.- Listar partidas iniciadas a la espera de contrincante.");
		System.out.println("4.- Unirse a una partida ya iniciada.");
		System.out.println("5.- Salir \"Logout\".");	
		int select = sc.nextInt();
		switch (select) {
		case 1:
			System.out.println("INFORMACIÓN DEL JUGADOR");
			try {
				this.servGestorJ.consultarPuntuacion(this.getNombre(), objCallback);
			} catch (RemoteException e) {
				System.out.println("Error al consultar las puntuaciones");
			}
			menuInterfazJugador(objCallback);
			break;
		case 2:
			System.out.println("INICIAR PARTIDA");
			try {
				this.servGestorJ.crearPartida(this.getNombre(), objCallback);
				int idPartida = Integer.parseInt(ListaEventos.getEvento());    
				setEvento(ListaEventos.getEvento());
				jugarPartida(true, this.getNombre(), idPartida, objCallback);
			} catch (RemoteException | InterruptedException e) {
				System.out.println("Error al crear la partida"+e);
			}
			break;
		case 3:
			System.out.println("LISTAR PARTIDAS");
			try {
				this.servGestorJ.listarPartidas(objCallback);
			} catch (RemoteException e) {
				System.out.println("Error al listar las partidas"+e);
			}
			menuInterfazJugador(objCallback);
			break;
		case 4:
			System.out.println("UNIRSE A UNA PARTIDA INICIADA");
			System.out.println("Introduzca el ID de la partida");
			int idPartida = sc.nextInt();
			try {
				this.servGestorJ.unirsePartida(idPartida, nombre, objCallback);
				setEvento(ListaEventos.getEvento());
				
				//Si hay error al unirse a una partida vuelve a imprimir el menú
				if (this.getEvento().equals("Error al unirse")) {
					menuInterfazJugador(objCallback);
				}
				
				jugarPartida(false, this.getNombre(), idPartida, objCallback);
				
			} catch (RemoteException | InterruptedException e) {
				System.out.println("Error al unirse a la partida"+e);
			}
			break;
		case 5:
			System.out.println("Realizando el logout...");
			try {
				this.servAutenticacionJ.logout(this.getNombre(), objCallback);
			} catch (RemoteException e) {
				System.out.println("Excepción " +e+ " producida al realizar el Logout");
			}
			System.exit(1);
			break;
		default:
			System.out.println("El valor introducido debe ser entre 1 y 3, y ha sido " + select);
			menuInterfazJugador(objCallback);
		}	
	}
	
	//Juega la partida comenzando por colocar los barcos, disparar hasta que haya ganador
	private void jugarPartida(Boolean creador, String nombre, int idPartida, CallbackJugadorInterface objCallback) throws RemoteException, InterruptedException {
		Scanner teclado = new Scanner(System.in);
		String formatoBarcos = "[a-jA-J]+\\d{1,2}+[vhVH]";
		String formatoDisparo = "[a-jA-J]{1}([1-9]|10)|ME_RINDO";
		while (true) {
			switch (this.getEvento()) {
			case "Colocar barcos":
				//Formato de entrada de coordenadas
				System.out.println("Fase de colocación de barcos");
				System.out.print("Introduzca coordenadas del barco 1 (Ejemplo A1V): ");
				String coordenadas1 = teclado.next().toUpperCase();
				while (!coordenadas1.matches(formatoBarcos)) {
					System.out.print("Error en el formato de las coordenadas del barco 1, vuelva a intentarlo (Ejemplo A1V): ");
					coordenadas1 = teclado.next().toUpperCase();
				}
				System.out.print("Introduzca coordenadas del barco 2 (Ejemplo A1V): ");
				String coordenadas2 = teclado.next().toUpperCase();
				while (!coordenadas2.matches(formatoBarcos)) {
					System.out.print("Error en el formato de las coordenadas del barco 2, vuelva a intentarlo (Ejemplo A1V): ");
					coordenadas2 = teclado.next().toUpperCase();
				}
				if (this.servGestorJ.colocarBarcos(coordenadas1, coordenadas2, nombre, idPartida, objCallback)) {
					setEvento(ListaEventos.getEvento());
				} 
				break;
			case "Comienza el juego":
				System.out.println("Partida iniciada");
				if (creador) {
					System.out.print("Introduzca coordenadas de disparo [YX] (Ejemplo A1):");
					String coordenadasDisparo = teclado.next().toUpperCase();
					while (!coordenadasDisparo.matches(formatoDisparo)) {
						System.out.print("Error en el formato de las coordenadas del disparo, vuelva a intentarlo (Ejemplo A1): ");
						coordenadasDisparo = teclado.next().toUpperCase();
					}
					//Verificador error incorrecto
					this.servGestorJ.disparar(coordenadasDisparo, this.getNombre(), idPartida, objCallback);
					setEvento(ListaEventos.getEvento());
				} else {
					setEvento(ListaEventos.getEvento());
				}
				break;
			case "Disparo recibido":	//Realizar turno
				System.out.print("Introduzca coordenadas de tiro [YX] (Ejemplo A1):");
				String coordenadasDisparo = teclado.next().toUpperCase();
				while (!coordenadasDisparo.matches(formatoDisparo)) {
					System.out.print("Error en el formato de las coordenadas del disparo, vuelva a intentarlo (Ejemplo A1): ");
					coordenadasDisparo = teclado.next().toUpperCase();
				}
				this.servGestorJ.disparar(coordenadasDisparo, this.getNombre(), idPartida, objCallback);
				setEvento(ListaEventos.getEvento());
				break;
			case "Disparo realizado":	//Esperar turno
				setEvento(ListaEventos.getEvento());
				break;
			case "Ganador":
				System.out.println("Enhorabuena "+this.getNombre()+", eres el GANADOR de la partida");
				setEvento("Finalizada");
				break;
			case "Perdedor":
				System.out.println("Lo sentimos "+this.getNombre()+", has perdido la partida");
				setEvento("Finalizada");
				break;
			case "Finalizada":
				menuInterfazJugador(objCallback);
				break;
			case "Error":
				this.servAutenticacionJ.logout(nombre, objCallback);
				System.exit(0);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: ");
			}
		}
		
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}
}
