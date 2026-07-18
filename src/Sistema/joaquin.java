package Sistema;
import Sistema.Grafo.Grafo;
import Sistema.Desafio;
import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Lista.Lista;
import java.util.HashMap;

public class joaquin {
    private Grafo mapa; // Mapa de la casa
    private DiccionarioAVL casona; // Habitaciones
    private HashMap<String, Equipo> equipos; // Equipos
    private HashMap<String, HashMap<Integer, Lista>> desafiosResueltos; // Desafios resueltos por equipo

    public void mostrarDesafio(int numeroDesafio, int numeroHabitacion){
        Habitacion hab = (Habitacion) casona.obtenerInformacion(numeroHabitacion);
        Desafio desafio = null;
        if(hab != null){
            desafio = hab.buscarDesafio(numeroDesafio);
            if(desafio != null){
                System.out.println("Desafío encontrado: " + desafio.toString());
            } else {
                System.out.println("El desafío no existe en la habitación especificada.");
            }
        }else{
            System.out.println("La habitación no existe.");
        }
    }
}
