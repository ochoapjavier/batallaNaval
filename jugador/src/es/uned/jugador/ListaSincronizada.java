package es.uned.jugador;

/**
 * Esta clase almacena y consume eventos para que haya comunicación mediante eventos 
 * entre el Servidor y el Jugador, a través del CallbackJugador
 * @author Javier Ochoa Pérez
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.List;

public class ListaSincronizada {
   private List <String> lista;
   
   protected ListaSincronizada () {
	   lista= new ArrayList<String>();
   }
   
   public synchronized int numDatosPendientes() {
	   return (lista.size());
   }
   
   public synchronized void addEvento(String dato) {
      lista.add(dato);
      notifyAll();
   }
   
   public synchronized String getEvento() throws InterruptedException {
      if (lista.size()==0)
         wait();
      String dato = lista.get(0);
      lista.remove(0);
      return dato;
   } 
}
