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

public String mostrarDesafiosResueltos(String nomEquipo) {
    String resultado = "";  
    HashMap<Integer, Lista> desafiosHechos = desafiosResueltos.get(nomEquipo);

    if (desafiosHechos != null) {
        if (!desafiosHechos.isEmpty()) {
            resultado += "El equipo " + nomEquipo + " resolvió:\n";
            resultado += "Habitación\tPuntaje Desafíos\n";
            // Itero sobre cada codigo en el hashmap
            for (Integer codHabitacion : desafiosHechos.keySet()) {
                resultado += "(" + codHabitacion + ")\t\t(";
                Lista puntajes = desafiosHechos.get(codHabitacion).clone();
                while (!puntajes.esVacia()) {
                    resultado += puntajes.recuperar(1);
                    puntajes.eliminar(1);
                    if (!puntajes.esVacia()) {
                        resultado += ", ";
                    }
                }
                resultado += ")\n";
            }    
        } else {
            resultado = "El equipo '" + nomEquipo + "' todavía no resolvió ningún desafío.";
        }
    } else {
        resultado = "Error: El equipo '" + nomEquipo + "' no está registrado en el sistema.";
    }
    return resultado; 
}
}
