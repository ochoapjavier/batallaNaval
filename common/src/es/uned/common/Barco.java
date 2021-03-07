package es.uned.common;

/**
 * Esta clase contiene un barco y realiza las operaciones sobre él tocado, hundido...
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Barco implements Serializable{

	private String barco [];

	
	public Barco( int longitud) {
		barco = new String [longitud];
	}
	
	//Calcula las coordenadas y añade al barco en cada posición el String correspondiente.
	//Ejemplo de A1V, añade en el barco A1, B1, C1
	public void calcularCoordenadas(String coordenada, int longitud) {
		char letra = coordenada.charAt(0);								//'A'
		int numero = obtenerColumna(coordenada);						// 1
		char orientacion = coordenada.charAt(coordenada.length()-1);	//'V'

		
		String posicion = null;
		
		if (orientacion == 'V') {
			char letra2 = ' ';
			
			for (int i = 0; i < longitud; i++) {
				letra2 = (char) (letra + i);
				posicion = String.valueOf(letra2)+numero;
				this.getBarco()[i] = posicion;
			}
		}
		
		if (orientacion == 'H') {
			int numero2 = 0;
			
			for (int i = 0; i < longitud; i++) {
				numero2 = numero + i;
				posicion = String.valueOf(letra)+numero2;
				this.getBarco()[i] = posicion;
			}
		}	
	}
	
	//Devuelve si esa coordenada ha tocado al barco y si es así lo pinta TOCADO
	public boolean tocado(String coordenada) {
		boolean tocado = false;
		
		for (int i = 0; i < this.barco.length; i++) {
			if(this.barco[i].equals(coordenada)) {
				this.barco[i] ="TOCADO";
				tocado = true;
			}
		}
		return tocado;
	}
	
	//Devuelve si el barco está hundido (si todas sus coordenadas están tocadas)
	public boolean hundido(){
		boolean hundido = false;
		for (int i = 0; i < this.barco.length; i++) {
			if(this.barco[i].equals("TOCADO")) {
				hundido = true;
			} else {
				hundido = false;
				break;
			}
		}
		return hundido;
	}
	
	//Obtiene la columna de la coordenada.
	private int obtenerColumna(String coordenada) {
		String regexp = "\\d+";
		String columnaS ="";
		Matcher m = Pattern.compile(regexp).matcher(coordenada);
		while (m.find()) {
			columnaS = columnaS.concat(m.group());
		}
		return Integer.parseInt(columnaS);	
	}

	public String[] getBarco() {
		return barco;
	}

	public void setBarco(String[] barco) {
		this.barco = barco;
	}
}
