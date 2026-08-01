package Sistema;
import Sistema.Grafo.Grafo;
import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Lista.Lista;
import java.util.HashMap;
public class EscapeHouse {
    private Grafo mapa; // Mapa de la casa
    private DiccionarioAVL casona; // Habitaciones
    private HashMap<String, Equipo> equipos; // Equipos
    private HashMap<String, HashMap<Integer, Lista>> desafiosResueltos; // Desafios resueltos por equipo
    

    //Consultas sobre habitaciones
    public String mostrarHabitacion(Integer habOrigen) {
        String resultado = "";
        // Verifico en el Diccionario si la habitación existe
        if (casona.contiene(habOrigen)) {
            resultado = casona.obtenerInformacion(habOrigen).toString();
        } else {
            resultado = "Error: La habitación " + habOrigen + " no existe en el mapa.";
        }
        return resultado;
    }


    public String sinPasarPor(Integer habOrigen, Integer habDestino, int cantidadPuntaje, Integer habProhibida) {
        String resultado = "";
        // Verifico en el Diccionario si las habitaciones existen
        if(casona.contiene(habOrigen) && casona.contiene(habDestino) && casona.contiene(habProhibida)) {
            resultado+= "Caminos desde la habitación " + habOrigen + " hasta la habitación " + habDestino +
                        " sin pasar por la habitación " + habProhibida + " y con puntaje máximo de " + cantidadPuntaje + ":\n";
            resultado= mapa.caminosSinPasarPor(habOrigen, habDestino, cantidadPuntaje, habProhibida).toString();
        } else {
            resultado = "Error: Al menos una de las habitaciones ingresadas no existe en el mapa.";
        }
        return resultado;
    }


    public String habitacionesContiguas(Integer habOrigen) {
        String resultado = "";
        // Verifico en el Diccionario si la habitación existe
        if (casona.contiene(habOrigen)) {
            resultado = mapa.obtenerAdyacentes(habOrigen).toString();
        } else {
            resultado = "Error: La habitación " + habOrigen + " no existe en el mapa.";
        }
        return resultado;
    }
    
    public boolean esPosibleLlegar(Integer habOrigen, Integer habDestino, int cantidadPuntaje) {
        boolean resultado = false;
        // Verifico en el Diccionario si las habitaciones existen
        if (casona.contiene(habOrigen) && casona.contiene(habDestino)) {
            resultado = mapa.esPosibleLlegar(habOrigen, habDestino, cantidadPuntaje);
        }
        return resultado;
    }

    public String consultarMinimoPuntaje(Integer habOrigen, Integer habDestino) {
        String resultado = "";
        // Verifico en el Diccionario (Casona) si las habitaciones existen
        if (casona.contiene(habOrigen) && casona.contiene(habDestino)) {
            Lista caminoIdeal = new Lista();
            int[] puntajeMinimo = new int[1];

            boolean hayRuta = mapa.caminoMenorCosto(habOrigen, habDestino, caminoIdeal, puntajeMinimo);
            if (hayRuta) {
                resultado = "Mínimo puntaje para pasar de " + habOrigen + " a " + habDestino + ": " + puntajeMinimo[0] +
                            "\nCamino : " + caminoIdeal.toString();
            } else {
                resultado = "Atención: No hay puertas que conecten la habitación " + habOrigen + 
                            " con la " + habDestino + ".";
            }
            
        } else {
            resultado = "Error: Al menos una de las habitaciones ingresadas no existe en el mapa.";
        }
        
        return resultado;
    }
//Consultas sobre desafios
    public String mostrarDesafio(int numeroDesafio, int numeroHabitacion){
            String res = "";
            Habitacion hab = (Habitacion) casona.obtenerInformacion(numeroHabitacion);
            Desafio desafio = null;
            if(hab != null){
                desafio = hab.buscarDesafio(numeroDesafio);
                if(desafio != null){
                    res += "Desafío encontrado: " + desafio.toString() + "\n";
                } else {
                    res += "El desafío no existe en la habitación especificada.\n";
                }
            }else{
                res += "La habitación no existe.\n";
            }
            return res;
        }

    public String mostrarDesafiosResueltos(String nomEquipo) {
        String resultado = "";  
        HashMap<Integer, Lista> desafiosHechos = desafiosResueltos.get(nomEquipo);
        if (desafiosHechos != null) {
            if (!desafiosHechos.isEmpty()) {
                resultado += "El equipo " + nomEquipo + " resolvió:\n";
                resultado += "Habitación\tDesafíos\n";
                for (Integer codHabitacion : desafiosHechos.keySet()) {
                    resultado += "(" + codHabitacion + ")\t\t";
                    Lista desafios = desafiosHechos.get(codHabitacion).clone();
                    while (!desafios.esVacia()) {
                        resultado += desafios.recuperar(1).toString();
                        desafios.eliminar(1);

                        if (!desafios.esVacia()) {
                            resultado += " | "; 
                        }
                    }
                    resultado += "\n";
                }    
            } else {
                resultado = "El equipo '" + nomEquipo + "' todavía no resolvió ningún desafío.";
            }
        } else {
            resultado = "Error: El equipo '" + nomEquipo + "' no está registrado en el sistema.";
        }
        
        return resultado; 
    }
    public String mostrarDesafiosTipo(int codHab, String tipoDesafio, int a, int b) {
        // 1. Inicializamos el String vacío
        String resultado = ""; 
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codHab);
        
        if (hab != null) {
            Lista desafios = hab.getDesafiosRango(a, b);
            if (!desafios.esVacia()) {
                resultado += "Desafíos del tipo '" + tipoDesafio + "' en la habitación " + codHab + " con puntaje entre " + a + " y " + b + ":\n";
                boolean encontroAlguno = false; 
                while (!desafios.esVacia()) {
                    Desafio desafio = (Desafio) desafios.recuperar(1);
                    if (desafio.getTipo().equalsIgnoreCase(tipoDesafio)) {
                        resultado += desafio.toString() + "\n";
                        encontroAlguno = true;
                    }
                    desafios.eliminar(1);
                }
                if (!encontroAlguno) {
                    resultado = "No se encontraron desafíos del tipo '" + tipoDesafio + "' en el rango [" + a + ", " + b + "].";
                }
                
            } else {
                resultado = "No hay ningún tipo de desafío en el rango [" + a + ", " + b + "] en la habitación " + codHab + ".";
            }
        } else {
            resultado = "Error: La habitación " + codHab + " no existe.";
        }
        return resultado;
    }
    public boolean verificarDesafioResuelto(String nomEquipo, int codHabitacion, Desafio desafio) {
        boolean resuelto = false;
        HashMap<Integer,Lista> desafiosHechos = desafiosResueltos.get(nomEquipo);
        if(desafiosHechos!= null){
            Lista desafios = desafiosHechos.get(codHabitacion);
            if(desafios!= null){
                if(desafios.localizar(desafio) > 0){
                    resuelto = true;
                }
            }
        }
        return resuelto;
    }       
}
