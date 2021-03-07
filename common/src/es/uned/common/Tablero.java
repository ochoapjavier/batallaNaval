package es.uned.common;

/**
 * Esta clase almacena la disposición de los barcos, y gestiona la colocación y los disparos en el mismo
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tablero implements Serializable{
	static final int NUMFILAS = 10;
	static final int NUMCOLUMNAS = 10;
	private int tablero [][];
	private int barcosColocados;
	private ArrayList<Barco> barcos;
	
	public Tablero() {
		this.tablero = new int[NUMFILAS][NUMCOLUMNAS];
		this.barcos = new ArrayList<Barco>();
		this.barcosColocados = 0;
	}
	
	//Inicia el tablero con las filas y colomnas de las constantes (en este caso 10x10)
	public void iniciarTablero() {
		for (int i = 0; i < NUMFILAS; i++) {
			for (int j = 0; j < NUMCOLUMNAS; j++) {
				this.tablero[i][j] = 0;
			}
		}
	}
	
	//Devuelve true si la colocación del barco en la coordenada tiene éxito y false si no
	public boolean colocarBarco(String coordenada) {
		int longitudBarco = 3;
		int fila = obtenerFila(coordenada);
		int columna = obtenerColumna(coordenada);
		char orientacion = coordenada.charAt(coordenada.length()-1);
		boolean colocado = false;
		if (fila < 0 || fila > 9 || columna < 0 || columna > 9) {
			return false;
		}
		try {
			if (orientacion == 'V') {
				for (int i = 0; i < longitudBarco; i++) {
					//Solo si la posición tiene valor 0 (libre y válida)
					if (this.tablero[fila+i][columna] == 0) {						
							this.tablero[fila+i][columna] = 1;
							colocado = true;
					}else {
						colocado = false;
						break;
					}	
				}
			} else if (orientacion == 'H') {
				for (int i = 0; i < longitudBarco; i++) {
					//Solo si la posición tiene valor 0 (libre y válida)
					if (this.tablero[fila][columna+i] == 0) {
							this.tablero[fila][columna+i] = 1;
							colocado = true;
					}else {
						colocado = false;
						break;
					
					}	
				}
			}
		}
		catch (Exception e) {
			colocado = false;
		}
		if(colocado) {
			Barco barco = new Barco (longitudBarco);
			barco.calcularCoordenadas(coordenada, longitudBarco);
			barcos.add(barco);
		}
		setBarcosColocados(getBarcosColocados()+1);
		return colocado;	
	}
	
	public void reiniciarBarcos() {
		this.getBarcos().clear();
		this.setBarcosColocados(0);
	}
	
	//Devuelve -1 si hay error, 0 si es agua, 1 si es tocado y 2 si es repetido 
	public int disparo(String coordenada) {
		int fila = obtenerFila(coordenada);
		int columna = obtenerColumna(coordenada);
		//Valor fuera de la tabla
		if (fila < 0 || fila > 9 || columna < 0 || columna > 9) {
			return -1;
		}
		switch (this.tablero[fila][columna]) {
		case 0:
			return 0;
		case 1:
			//Si es tocado, pinta con 2 esa coordenada, si vuelve a ser disparada será repetida (devolverá 2).
			this.tablero[fila][columna] = 2;
			return 1;
		case 2:
			return 2;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this.tablero[fila][columna]);
		}
	}
	
	//Devuelve la fila a partir de la coordenada
	private int obtenerFila(String coordenada) {
		char letraChar = coordenada.charAt(0);
		int fila = -1;
		int k = 0;
		for (char i = 'A'; i < 'K'; i++) {
			if (letraChar == i) {
				fila = k;
			}
			k++;
		}
		return fila;
	}
	
	//Obtiene la columna a partir de la coordenada
	private int obtenerColumna(String coordenada) {
		String regexp = "\\d+";
		String columnaS ="";
		Matcher m = Pattern.compile(regexp).matcher(coordenada);
		while (m.find()) {
			columnaS = columnaS.concat(m.group());
		}
		return (Integer.parseInt(columnaS)-1);	
	}
	
	public ArrayList<Barco> getBarcos() {
		return barcos;
	}
	public void setBarcos(ArrayList<Barco> barcos) {
		this.barcos = barcos;
	}
	
	public int[][] getTablero() {
		return tablero;
	}
	public void setTablero(int[][] tablero) {
		this.tablero = tablero;
	}
	public int getBarcosColocados() {
		return barcosColocados;
	}
	public void setBarcosColocados(int barcosColocados) {
		this.barcosColocados = barcosColocados;
	}
	
}
