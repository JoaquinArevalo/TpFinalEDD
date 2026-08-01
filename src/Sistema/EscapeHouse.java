package Sistema;
import Sistema.Grafo.Grafo;
import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Lista.Lista;
import java.util.HashMap;
import java.util.Scanner;
public class EscapeHouse {
    private Grafo mapa; // Mapa de la casa
    private DiccionarioAVL casona; // Habitaciones
    private HashMap<String, Equipo> equipos; // Equipos
    private HashMap<String, HashMap<Integer, Lista>> desafiosResueltos; // Desafios resueltos por equipo
    
    public EscapeHouse() {
    this.mapa = new Grafo();
    this.casona = new DiccionarioAVL();
    this.equipos = new HashMap<>();
    this.desafiosResueltos = new HashMap<>();
    }

    // Método para mostrar el estado completo del sistema
    public String mostrarSistema() {
        String resultado = "\n============================================================\n" +
                        "               ESTADO COMPLETO ESCAPE HOUSE\n" +
                        "============================================================\n" +
                        mostrarAVLHabitaciones() +
                        mostrarAVLDesafios() +
                        mostrarGrafo() +
                        mostrarEquipos() +
                        mostrarDesafiosResueltos() +
                        "============================================================\n";
        return resultado;
    }

    private String mostrarAVLHabitaciones() {
        String resultado = "\n1. AVL DE HABITACIONES\n" +
                        "------------------------------------------------------------\n" +
                        this.casona.toString() + "\n";
        return resultado;
    }

    private String mostrarAVLDesafios() {
        String resultado = "\n2. AVL DE DESAFIOS POR HABITACION\n" +
                        "------------------------------------------------------------\n";
        Lista habitaciones = this.casona.listarInorden();

        if (habitaciones.esVacia()) {
            resultado += "No hay habitaciones cargadas.\n";
        } else {
            // Se cambió el 'while' por un 'for' para ahorrar líneas y hacerlo más limpio
            for (int pos = 1; pos <= habitaciones.longitud(); pos++) {
                Habitacion hab = (Habitacion) habitaciones.recuperar(pos);
                resultado += "\nHabitacion " + hab.getCodigo() + " - " + hab.getNombre() + "\n" +
                            hab.getDesafios().toString() + "\n";
            }
        }

        return resultado;
    }

    private String mostrarGrafo() {
        String resultado = "\n3. GRAFO DE HABITACIONES Y PUERTAS\n" +
                        "------------------------------------------------------------\n" +
                        this.mapa.toString();
        return resultado;
    }

    private String mostrarEquipos() {
        String resultado = "\n4. HASH DE EQUIPOS\n" +
                        "------------------------------------------------------------\n";

        if (this.equipos.isEmpty()) {
            resultado += "No hay equipos registrados.\n";
        } else {
            for (String nombreEquipo : this.equipos.keySet()) {
                Equipo equipo = this.equipos.get(nombreEquipo);
                resultado += "CLAVE HASH: " + nombreEquipo + "\n" +
                            "VALOR: " + equipo.toString() + "\n";
            }
        }

        return resultado;
    }

    private String mostrarDesafiosResueltos() {
        String resultado = "\n5. MAPEO DE DESAFIOS RESUELTOS\n" +
                        "------------------------------------------------------------\n";

        if (this.desafiosResueltos.isEmpty()) {
            resultado += "No hay equipos registrados en el historial.\n";
        } else {
            for (String nombreEquipo : this.desafiosResueltos.keySet()) {
                HashMap<Integer, Lista> historial = this.desafiosResueltos.get(nombreEquipo);
                resultado += "EQUIPO: " + nombreEquipo + "\n";
                resultado += (historial.isEmpty()) ? "    Sin desafios resueltos.\n" : mostrarHistorialEquipo(historial);
            }
        }

        return resultado;
    }

    private String mostrarHistorialEquipo(HashMap<Integer, Lista> historial) {
        String resultado = "";

        for (Integer codigoHabitacion : historial.keySet()) {
            Lista desafios = historial.get(codigoHabitacion);
            resultado += "    HABITACION: " + codigoHabitacion + "\n" +
                        "    DESAFIOS: " + desafios.toString() + "\n";
        }

        return resultado;
    }







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
            resultado += "Caminos desde la habitación " + habOrigen + " hasta la habitación " + habDestino +
                        " sin pasar por la habitación " + habProhibida + " y con puntaje máximo de " + cantidadPuntaje + ":\n";
            resultado+= mapa.caminosSinPasarPor(habOrigen, habDestino, habProhibida, cantidadPuntaje).toString();
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


    private static void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("          ESCAPE HOUSE 2.0");
        System.out.println("========================================");
        System.out.println("1. Carga inicial");
        System.out.println("2. ABM");
        System.out.println("3. Consultas sobre habitaciones");
        System.out.println("4. Consultas sobre desafios");
        System.out.println("5. Consultas sobre equipos");
        System.out.println("6. Consulta general");
        System.out.println("0. Salir");
    }

    public static void main(String[] args) {
    String RUTA_LOG = "";
    String RUTA_CARGA = "";
    Scanner teclado = new Scanner(System.in);
    EscapeHouse sistema = new EscapeHouse();
    Logger logger = new Logger(RUTA_LOG);

    boolean cargaExitosa = false;
    int opcion = -1;

    try {
        System.out.println("Realizando carga inicial...");

        // cargaExitosa = sistema.cargarDesdeArchivo(RUTA_CARGA);

        if (cargaExitosa) {
            System.out.println("Carga inicial completada.");

            logger.registrarEstadoInicial(
                    sistema.mostrarSistema()
            );

            do {
                mostrarMenuPrincipal();
                opcion = teclado.nextInt();

                switch (opcion) {
                    case 1:
                        System.out.println(
                                "La carga inicial ya fue realizada automaticamente."
                        );
                        break;

                    case 2:
                        // menuABM(sistema, logger, teclado);
                        break;

                    case 3:
                        // menuConsultasHabitaciones(sistema, teclado);
                        break;

                    case 4:
                        // menuConsultasDesafios(sistema, teclado);
                        break;

                    case 5:
                        // menuConsultasEquipos(sistema, teclado);
                        break;

                    case 6:
                        System.out.println(
                                sistema.mostrarSistema()
                        );
                        break;

                    case 0:
                        System.out.println(
                                "Finalizando Escape House..."
                        );
                        break;

                    default:
                        System.out.println(
                                "Opcion incorrecta."
                        );
                        break;
                }

            } while (opcion != 0);

        } else {
            System.out.println(
                    "La carga inicial no pudo completarse."
            );

            logger.registrarError(
                    "No se pudo completar la carga inicial desde: "
                            + RUTA_CARGA
            );
        }

    } catch (Exception excepcion) {
        System.out.println(
                "Se produjo un error durante la ejecucion: "
                        + excepcion.getMessage()
        );

        logger.registrarError(
                "Error general: " + excepcion.getMessage()
        );

    } finally {
        logger.registrarEstadoFinal(
                sistema.mostrarSistema()
        );

        logger.cerrar();
        teclado.close();
    }
}
}
