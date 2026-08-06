package Sistema;

import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Grafo.Grafo;
import Sistema.Lista.Lista;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class EscapeHouse {
    /*goyo path: /home/goyo/Escritorio/RepositoriosGit/facultad/EDD/TPO/TpFinalEDD/src/casona.txt */
    //log: /home/goyo/Escritorio/RepositoriosGit/facultad/EDD/TPO/TpFinalEDD/src/log_escapeHouse.txt.


    private static final String RUTA_CARGA = "/home/goyo/Escritorio/RepositoriosGit/facultad/EDD/TPO/TpFinalEDD/src/casona.txt";
    private static final String RUTA_LOG = "/home/goyo/Escritorio/RepositoriosGit/facultad/EDD/TPO/TpFinalEDD/src/log_escapeHouse.txt";

    private Grafo mapa;
    private DiccionarioAVL casona;
    private HashMap<String, Equipo> equipos;
    private HashMap<String, HashMap<Integer, Lista>> desafiosResueltos;

    public EscapeHouse() {
        this.mapa = new Grafo();
        this.casona = new DiccionarioAVL();
        this.equipos = new HashMap<>();
        this.desafiosResueltos = new HashMap<>();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EscapeHouse sistema = new EscapeHouse();
        Logger logger = new Logger(RUTA_LOG);

        boolean cargaExitosa;
        int opcion = -1;

        System.out.println("Realizando carga inicial...");

        cargaExitosa = cargarDesdeArchivo(
                RUTA_CARGA,
                sistema.mapa,
                sistema.casona,
                sistema.equipos,
                sistema.desafiosResueltos
        );

        if (cargaExitosa) {
            System.out.println("Carga inicial completada.");
            logger.registrarEstadoInicial(sistema.mostrarSistema());

            do {
                mostrarMenuPrincipal();
                opcion = leerEntero(sc, "Opcion: ");

                switch (opcion) {
                    case 1:
                        System.out.println("La carga inicial ya fue realizada automaticamente.");
                        break;
                    case 2:
                        menuABM(sistema, logger, sc);
                        break;
                    case 3:
                        menuConsultasHabitaciones(sistema, sc);
                        break;
                    case 4:
                        menuConsultasDesafios(sistema, sc);
                        break;
                    case 5:
                        menuConsultasEquipos(sistema, sc);
                        break;
                    case 6:
                        System.out.println(sistema.mostrarSistema());
                        break;
                    case 0:
                        System.out.println("Finalizando Escape House...");
                        break;
                    default:
                        System.out.println("Opcion incorrecta.");
                        break;
                }
            } while (opcion != 0);
        } else {
            System.out.println("La carga inicial no pudo completarse.");
            logger.registrarError("No se pudo completar la carga inicial desde: " + RUTA_CARGA);
        }

        logger.registrarEstadoFinal(sistema.mostrarSistema());
        logger.cerrar();
        sc.close();
        return;
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
        return;
    }

    private static void menuABM(EscapeHouse sistema, Logger logger, Scanner sc) {
        int opcion = -1;

        do {
            System.out.println();
            System.out.println("=============== ABM ===============");
            System.out.println("1. Alta de habitacion");
            System.out.println("2. Baja de habitacion");
            System.out.println("3. Modificar habitacion");
            System.out.println("4. Alta de desafio");
            System.out.println("5. Baja de desafio");
            System.out.println("6. Modificar desafio");
            System.out.println("7. Alta de equipo");
            System.out.println("8. Baja de equipo");
            System.out.println("9. Modificar equipo");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Opcion: ");

            switch (opcion) {
                case 1: {
                    int codigo = leerEntero(sc, "Codigo: ");
                    String nombre = leerTexto(sc, "Nombre: ");
                    int planta = leerEntero(sc, "Planta: ");
                    double metros = leerDouble(sc, "Metros cuadrados: ");
                    boolean exito = sistema.altaHabitacion(codigo, nombre, planta, metros, false);

                    if (exito) {
                        System.out.println("Habitacion creada correctamente.");
                        logger.registrarAlta("Habitacion " + codigo + " - " + nombre);
                    } else {
                        System.out.println("No fue posible crear la habitacion.");
                    }
                    break;
                }
                case 2: {
                    int codigo = leerEntero(sc, "Codigo de habitacion: ");
                    boolean exito = sistema.bajaHabitacion(codigo);

                    if (exito) {
                        System.out.println("Habitacion eliminada correctamente.");
                        logger.registrarBaja("Habitacion " + codigo);
                    } else {
                        System.out.println("No fue posible eliminar la habitacion.");
                    }
                    break;
                }
                case 3: {
                    int codigo = leerEntero(sc, "Codigo de habitacion: ");
                    String nombre = leerTexto(sc, "Nuevo nombre: ");
                    int planta = leerEntero(sc, "Nueva planta: ");
                    double metros = leerDouble(sc, "Nuevos metros cuadrados: ");
                    boolean exito = sistema.modificarHabitacion(codigo, nombre, planta, metros);

                    if (exito) {
                        System.out.println("Habitacion modificada correctamente.");
                        logger.registrarModificacion("Habitacion " + codigo);
                    } else {
                        System.out.println("No fue posible modificar la habitacion.");
                    }
                    break;
                }
                case 4: {
                    int codHabitacion = leerEntero(sc, "Codigo de habitacion: ");
                    int puntaje = leerEntero(sc, "Puntaje: ");
                    String nombre = leerTexto(sc, "Nombre del desafio: ");
                    String tipo = leerTexto(sc, "Tipo: ");
                    boolean exito = sistema.altaDesafio(codHabitacion, puntaje, nombre, tipo);

                    if (exito) {
                        System.out.println("Desafio creado correctamente.");
                        logger.registrarAlta("Desafio " + puntaje + " de la habitacion " + codHabitacion);
                    } else {
                        System.out.println("No fue posible crear el desafio.");
                    }
                    break;
                }
                case 5: {
                    int codHabitacion = leerEntero(sc, "Codigo de habitacion: ");
                    int puntaje = leerEntero(sc, "Puntaje: ");
                    boolean exito = sistema.bajaDesafio(codHabitacion, puntaje);

                    if (exito) {
                        System.out.println("Desafio eliminado correctamente.");
                        logger.registrarBaja("Desafio " + puntaje + " de la habitacion " + codHabitacion);
                    } else {
                        System.out.println("No fue posible eliminar el desafio.");
                    }
                    break;
                }
                case 6: {
                    int codHabitacion = leerEntero(sc, "Codigo de habitacion: ");
                    int puntaje = leerEntero(sc, "Puntaje original: ");
                    String nombre = leerTexto(sc, "Nuevo nombre: ");
                    String tipo = leerTexto(sc, "Nuevo tipo: ");
                    boolean exito = sistema.modificarDesafio(codHabitacion, puntaje, nombre, tipo);

                    if (exito) {
                        System.out.println("Desafio modificado correctamente.");
                        logger.registrarModificacion("Desafio " + puntaje + " de la habitacion " + codHabitacion);
                    } else {
                        System.out.println("No fue posible modificar el desafio.");
                    }
                    break;
                }
                case 7: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    int puntajeExigido = leerEntero(sc, "Puntaje exigido: ");
                    int puntajeAcum = leerEntero(sc, "Puntaje acumulado: ");
                    int codHab = leerEntero(sc, "Habitacion actual: ");
                    int puntajeHab = leerEntero(sc, "Puntaje en la habitacion: ");
                    boolean exito = sistema.altaEquipo(
                            nombre,
                            puntajeExigido,
                            puntajeAcum,
                            codHab,
                            puntajeHab
                    );

                    if (exito) {
                        System.out.println("Equipo creado correctamente.");
                        logger.registrarAlta("Equipo " + nombre);
                    } else {
                        System.out.println("No fue posible crear el equipo.");
                    }
                    break;
                }
                case 8: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    boolean exito = sistema.bajaEquipo(nombre);

                    if (exito) {
                        System.out.println("Equipo eliminado correctamente.");
                        logger.registrarBaja("Equipo " + nombre);
                    } else {
                        System.out.println("No fue posible eliminar el equipo.");
                    }
                    break;
                }
                case 9: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    int puntajeExigido = leerEntero(sc, "Nuevo puntaje exigido: ");
                    int puntajeAcum = leerEntero(sc, "Nuevo puntaje acumulado: ");
                    int codHab = leerEntero(sc, "Nueva habitacion: ");
                    int puntajeHab = leerEntero(sc, "Nuevo puntaje en habitacion: ");
                    boolean exito = sistema.modificarEquipo(
                            nombre,
                            puntajeExigido,
                            puntajeAcum,
                            codHab,
                            puntajeHab
                    );

                    if (exito) {
                        System.out.println("Equipo modificado correctamente.");
                        logger.registrarModificacion("Equipo " + nombre);
                    } else {
                        System.out.println("No fue posible modificar el equipo.");
                    }
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
                    break;
            }
        } while (opcion != 0);

        return;
    }

    private static void menuConsultasHabitaciones(EscapeHouse sistema, Scanner sc) {
        int opcion = -1;

        do {
            System.out.println();
            System.out.println("===== CONSULTAS HABITACIONES =====");
            System.out.println("1. Mostrar habitacion");
            System.out.println("2. Habitaciones contiguas");
            System.out.println("3. Es posible llegar");
            System.out.println("4. Minimo puntaje");
            System.out.println("5. Caminos sin pasar por una habitacion");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Opcion: ");

            switch (opcion) {
                case 1: {
                    int codigo = leerEntero(sc, "Codigo de habitacion: ");
                    System.out.println(sistema.mostrarHabitacion(codigo));
                    break;
                }
                case 2: {
                    int codigo = leerEntero(sc, "Codigo de habitacion: ");
                    System.out.println(sistema.habitacionesContiguas(codigo));
                    break;
                }
                case 3: {
                    int origen = leerEntero(sc, "Habitacion origen: ");
                    int destino = leerEntero(sc, "Habitacion destino: ");
                    int puntaje = leerEntero(sc, "Puntaje disponible: ");
                    boolean posible = sistema.esPosibleLlegar(origen, destino, puntaje);
                    System.out.println(posible ? "Es posible llegar." : "No es posible llegar.");
                    break;
                }
                case 4: {
                    int origen = leerEntero(sc, "Habitacion origen: ");
                    int destino = leerEntero(sc, "Habitacion destino: ");
                    System.out.println(sistema.consultarMinimoPuntaje(origen, destino));
                    break;
                }
                case 5: {
                    int origen = leerEntero(sc, "Habitacion origen: ");
                    int destino = leerEntero(sc, "Habitacion destino: ");
                    int prohibida = leerEntero(sc, "Habitacion prohibida: ");
                    int puntaje = leerEntero(sc, "Puntaje maximo: ");
                    System.out.println(sistema.sinPasarPor(origen, destino, puntaje, prohibida));
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
                    break;
            }
        } while (opcion != 0);

        return;
    }

    private static void menuConsultasDesafios(EscapeHouse sistema, Scanner sc) {
        int opcion = -1;

        do {
            System.out.println();
            System.out.println("======= CONSULTAS DESAFIOS =======");
            System.out.println("1. Mostrar desafio");
            System.out.println("2. Mostrar desafios resueltos por equipo");
            System.out.println("3. Verificar desafio resuelto");
            System.out.println("4. Mostrar desafios por tipo y rango");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Opcion: ");

            switch (opcion) {
                case 1: {
                    int habitacion = leerEntero(sc, "Codigo de habitacion: ");
                    int puntaje = leerEntero(sc, "Puntaje del desafio: ");
                    System.out.println(sistema.mostrarDesafio(puntaje, habitacion));
                    break;
                }
                case 2: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    System.out.println(sistema.mostrarDesafiosResueltos(nombre));
                    break;
                }
                case 3: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    int habitacion = leerEntero(sc, "Codigo de habitacion: ");
                    int puntaje = leerEntero(sc, "Puntaje del desafio: ");
                    Habitacion hab = (Habitacion) sistema.casona.obtenerInformacion(habitacion);
                    Desafio desafio = null;

                    if (hab != null) {
                        desafio = hab.buscarDesafio(puntaje);
                    }

                    boolean resuelto = sistema.verificarDesafioResuelto(nombre,habitacion,desafio);

                    System.out.println(
                            resuelto
                                    ? "El desafio fue resuelto."
                                    : "El desafio no fue resuelto.");
                    break;
                }
                case 4: {
                    int habitacion = leerEntero(sc, "Codigo de habitacion: ");
                    String tipo = leerTexto(sc, "Tipo de desafio: ");
                    int minimo = leerEntero(sc, "Puntaje minimo: ");
                    int maximo = leerEntero(sc, "Puntaje maximo: ");
                    System.out.println(sistema.mostrarDesafiosTipo(habitacion,tipo,minimo,maximo));
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
                    break;
            }
        } while (opcion != 0);

        return;
    }

    private static void menuConsultasEquipos(EscapeHouse sistema, Scanner sc) {
        int opcion = -1;

        do {
            System.out.println();
            System.out.println("======== CONSULTAS EQUIPOS ========");
            System.out.println("1. Mostrar informacion de equipo");
            System.out.println("2. Posibles desafios");
            System.out.println("3. Jugar desafio");
            System.out.println("4. Cambiar de habitacion");
            System.out.println("5. Puede salir");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Opcion: ");

            switch (opcion) {
                case 1: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    System.out.println(sistema.mostrarInfoEquipo(nombre));
                    break;
                }
                case 2: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    int codigoHabitacion = leerEntero(sc,"Habitacion a la que quiere pasar: ");

                    Equipo equipo = sistema.equipos.get(nombre);
                    Habitacion hab = (Habitacion) sistema.casona.obtenerInformacion(codigoHabitacion);

                    String resultado = sistema.posiblesDesafios(equipo, hab);
                    System.out.println("Resultado de la validación: " + resultado);                    break;
                }
                case 3: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    int puntaje = leerEntero(sc, "Puntaje del desafio: ");
                    Equipo equipo = sistema.equipos.get(nombre);
                    Desafio desafio = null;

                    if (equipo != null) {
                        Habitacion hab = (Habitacion) sistema.casona.obtenerInformacion(
                                equipo.getCodigoHabitacion()
                        );

                        if (hab != null) {
                            desafio = hab.buscarDesafio(puntaje);
                        }
                    }

                    boolean exito = sistema.jugarDesafio(equipo, desafio);
                    System.out.println(
                            exito
                                    ? "Desafio jugado correctamente."
                                    : "No fue posible jugar el desafio."
                    );
                    break;
                }
                case 4: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    int codigoHabitacion = leerEntero(sc, "Habitacion destino: ");
                    Equipo equipo = sistema.equipos.get(nombre);
                    Habitacion hab = (Habitacion) sistema.casona.obtenerInformacion(
                            codigoHabitacion
                    );

                    boolean exito = sistema.cambiarDeHabitacion(equipo, hab);
                    System.out.println(
                            exito
                                    ? "El equipo cambio de habitacion."
                                    : "No fue posible cambiar de habitacion."
                    );
                    break;
                }
                case 5: {
                    String nombre = leerTexto(sc, "Nombre del equipo: ");
                    boolean exito = sistema.puedeSalir(nombre);
                    System.out.println(
                            exito
                                    ? "El equipo puede salir."
                                    : "El equipo no puede salir."
                    );
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
                    break;
            }
        } while (opcion != 0);

        return;
    }

    private static int leerEntero(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        int numero = Integer.parseInt(sc.nextLine());
        return numero;
    }

    private static double leerDouble(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        double numero = Double.parseDouble(sc.nextLine());
        return numero;
    }

    private static String leerTexto(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        String texto = sc.nextLine();
        return texto;
    }

    // ---------------------------------------------------------
    // CARGA INICIAL 
    // ---------------------------------------------------------

    public static boolean cargarDesdeArchivo(
            String rutaArchivo,
            Grafo mapa,
            DiccionarioAVL casona,
            HashMap<String, Equipo> equipos,
            HashMap<String, HashMap<Integer, Lista>> desafiosResueltos) {

        boolean cargaExitosa = false;

        // Listas temporales
        Lista lineasHabitaciones = new Lista();
        Lista lineasPuertas = new Lista();
        Lista lineasDesafios = new Lista();
        Lista lineasEquipos = new Lista();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) { {

            String linea;

            //Lectura y clasificacion
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (!linea.isEmpty() && !linea.startsWith("#")) {
                    char tipo = linea.toUpperCase().charAt(0);

                    switch (tipo) {
                        //se insertan en la posicion 1 para que no recorran de mas
                        case 'H':
                            lineasHabitaciones.insertar(linea, 1);
                            break;
                        case 'P':
                            lineasPuertas.insertar(linea, 1);
                            break;
                        case 'D':
                            lineasDesafios.insertar(linea, 1);
                            break;
                        case 'E':
                            lineasEquipos.insertar(linea, 1);
                            break;
                        default:
                            System.out.println("Linea ignorada por tipo desconocido: " + linea);
                            break;
                    }
                }
            }

            //Guardar Habitaciones
            for (int i = 1; i <= lineasHabitaciones.longitud(); i++) {
                String[] datos = ((String) lineasHabitaciones.recuperar(i)).split(";");

                if (datos.length >= 6) {
                    int codHab = Integer.parseInt(datos[1].trim());
                    String nomHab = datos[2].trim();
                    int planta = Integer.parseInt(datos[3].trim());
                    double m2 = Double.parseDouble(datos[4].trim());
                    boolean salida = Boolean.parseBoolean(datos[5].trim());

                    Habitacion hab = new Habitacion(codHab,nomHab,planta,m2,salida);
                    casona.insertar(codHab, hab);
                    mapa.insertarVertice(codHab);
                }
            }

            //Guardar puertas (arcos)(Requiere tener habitaciones primero)
            // medio trampa que metan justo las puertas con 2 puntos y no digan nada, feo la verdad
            for (int i = 1; i <= lineasPuertas.longitud(); i++) {
                String lineaPuerta = (String) lineasPuertas.recuperar(i);

                //primero reemplaza todos los 2 puntos por punto y coma y despues los separa
                String[] datos = lineaPuerta.replaceFirst(":", ";").split(";");

                if (datos.length >= 4) {
                    int origen = Integer.parseInt(datos[1].trim());
                    int destino = Integer.parseInt(datos[2].trim());
                    int puntajeMin = Integer.parseInt(datos[3].trim());

                    mapa.insertarArco(origen, destino, puntajeMin);
                }
            }

            // Guardar Desafíos (Requiere Habitaciones)
            for (int i = 1; i <= lineasDesafios.longitud(); i++) {
                String[] datos = ((String) lineasDesafios.recuperar(i)).split(";");

                if (datos.length >= 5) {
                    int puntajeDesafio = Integer.parseInt(datos[1].trim());
                    int codHabDesafio = Integer.parseInt(datos[2].trim());
                    String nomDesafio = datos[3].trim();
                    String tipoDesafio = datos[4].trim();

                    Desafio des = new Desafio(codHabDesafio,puntajeDesafio,nomDesafio,tipoDesafio);

                    Habitacion habDestino = (Habitacion) casona.obtenerInformacion(codHabDesafio);

                    if (habDestino != null) {
                        habDestino.getDesafios().insertar(puntajeDesafio, des);
                    }
                }
            }

            // GUardar Equipos y su historial de desafios(Requiere todo lo anterior)
            for (int i = 1; i <= lineasEquipos.longitud(); i++) {
                String[] datos = ((String) lineasEquipos.recuperar(i)).split(";");

                if (datos.length >= 6) {
                    String nomEquipo = datos[1].trim();
                    int puntajeExigido = Integer.parseInt(datos[2].trim());
                    int puntajeAcumulado = Integer.parseInt(datos[3].trim());
                    int codigoHabitacion = Integer.parseInt(datos[4].trim());
                    int puntajeHabitacion = Integer.parseInt(datos[5].trim());

                    Equipo eq = new Equipo(nomEquipo,puntajeExigido,puntajeAcumulado,codigoHabitacion,puntajeHabitacion);

                    equipos.put(nomEquipo, eq);

                    if (!desafiosResueltos.containsKey(nomEquipo)) {
                        desafiosResueltos.put(nomEquipo,new HashMap<Integer, Lista>());
                    }

                    // Obtenemos el historial de habitaciones de este equipo
                    HashMap<Integer, Lista> historialHabitaciones = desafiosResueltos.get(nomEquipo);

                    for (int j = 6; j < datos.length; j++) {
                        String item = datos[j].trim();

                        if (!item.isEmpty()) {
                                // Formato separado ej: (1,20)
                                String limpio = item.replace("(", "").replace(")", "").replace("$", "");

                                String[] partes = limpio.split(",");

                                if (partes.length == 2) {
                                    int codHab = Integer.parseInt(partes[0].trim());
                                    int puntaje = Integer.parseInt(partes[1].trim());

                                    Habitacion hab = (Habitacion) casona.obtenerInformacion(codHab);

                                    if (hab != null) {
                                        if (!historialHabitaciones.containsKey(codHab)) {
                                            historialHabitaciones.put(codHab,new Lista());
                                        }
                                        Lista listaDesafiosHabitacion =historialHabitaciones.get(codHab);
                                        Desafio d = hab.buscarDesafio(puntaje);

                                        if (d != null) {
                                            listaDesafiosHabitacion.insertar(d,listaDesafiosHabitacion.longitud() + 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            cargaExitosa = true;
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al procesar archivos: "+ e.getMessage());
        }

        return cargaExitosa;
    }

    // ---------------------------------------------------------
    // ABM
    // ---------------------------------------------------------
    //ABM HABITACIONES

    public boolean altaHabitacion(int codigo,String nombre,int planta,double metros,boolean tieneSalida) {

        boolean exito = false;
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codigo);

        if (hab == null && !tieneSalida && codigo != 1) {
            exito = true;

            //Crea habitacion , la insera en el Avl y en grafo
            Habitacion habNueva = new Habitacion(codigo,nombre,planta,metros,tieneSalida);

            casona.insertar(codigo, habNueva);
            mapa.insertarVertice(codigo);
        }

        return exito;
    }

    public boolean bajaHabitacion(int codigo) {
        boolean exito = false;
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codigo);

        // Si no es nula, ni es primera puerta , ni es salida
        if (hab != null && codigo != 1 && !hab.isTieneSalida() && hab.getDesafios().esVacio()) {

            // Verificamos si la habitación está ocupada usando for-each
            boolean habitacionOcupada = false;

            for (Equipo eq : equipos.values()) {
                if (eq.getCodigoHabitacion() == codigo) {
                    habitacionOcupada = true;
                }
            }

            if (!habitacionOcupada) {
                exito = true;
                casona.eliminar(codigo);
                mapa.eliminarVertice(codigo);
            }
        }

        return exito;
    }

    public boolean modificarHabitacion(int codigo,String nuevoNombre,int nuevaPlanta,double nuevosMetros) {

        boolean exito = false;
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codigo);

        if (hab != null && !hab.isTieneSalida() && codigo != 1) {
            exito = true;
            hab.setNombre(nuevoNombre);
            hab.setPlanta(nuevaPlanta);
            hab.setMetrosCuadrados(nuevosMetros);
        }

        return exito;
    }

    //ABM DESAFIOS
    public boolean altaDesafio(int codHabitacion,int puntaje,String nombre,String tipo) {

        boolean exito = false;
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codHabitacion);

        //si nignun desafio del avlDesafios de la habitacion tiene el mismo puntaje
        if (hab != null && hab.buscarDesafio(puntaje) == null) {
            exito = true;

            Desafio nuevo = new Desafio(
                    codHabitacion,
                    puntaje,
                    nombre,
                    tipo
            );

            hab.getDesafios().insertar(puntaje, nuevo);
        }

        return exito;
    }

    public boolean bajaDesafio(int codHabitacion, int puntaje) {
        boolean exito = false;
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codHabitacion);

        if (hab != null) {
            // Borramos el desafío de la habitacion
            boolean encontrado = hab.getDesafios().eliminar(puntaje);

            if (encontrado) {
                exito = true;

                // Nos fijamos si el equipo tiene un desafío resuelto por esa cantidad de puntos
                for (HashMap<Integer, Lista> historialEquipo
                        : desafiosResueltos.values()) {

                    Lista listaDesafios = historialEquipo.get(codHabitacion);

                    if (listaDesafios != null) {
                        int posicion = 1;

                        while (posicion <= listaDesafios.longitud()) {
                            Desafio desafio =(Desafio) listaDesafios.recuperar(posicion);

                            //  si es ese codigo de hab tiene ese puntaje entonces si lo eliminamos
                            if (desafio.getPuntaje() == puntaje) {
                                listaDesafios.eliminar(posicion);
                            } else {
                                posicion++;
                            }
                        }

                        if (listaDesafios.esVacia()) {
                            historialEquipo.remove(codHabitacion);
                        }
                    }
                }
            }
        }

        return exito;
    }

    public boolean modificarDesafio(int codHabitacion,int puntajeOriginal,String nuevoNombre,String nuevoTipo) {

        boolean exito = false;
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codHabitacion);

        if (hab != null) {
            //buscamos que exista el desafio con ese puntaje
            Desafio des = hab.buscarDesafio(puntajeOriginal);

            if (des != null) {
                exito = true;
                des.setNombre(nuevoNombre);
                des.setTipo(nuevoTipo);
            }
        }

        return exito;
    }

    //ABM EQUIPOS
    public boolean altaEquipo(String nombre,int puntajeExigido,int puntajeAcum,int codHab,int puntajeHab) {

        boolean exito = false;
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codHab);

        // .get  devuelve si existe ese objeto en la tabla
        if (equipos.get(nombre) == null && hab != null && puntajeExigido >= 0 && puntajeAcum >= 0 && puntajeHab >= 0) {

            exito = true;

            Equipo equipoNuevo = new Equipo(nombre,puntajeExigido,puntajeAcum,codHab,puntajeHab);

            // .put inserta o actualiza  un equipo dentro de la tabla
            equipos.put(nombre, equipoNuevo);

            // Inicializamos la tabla de  desafios resueltos del equipoNuevo en 0
            desafiosResueltos.put(nombre,new HashMap<Integer, Lista>());
        }

        return exito;
    }

    public boolean bajaEquipo(String nombre) {
        boolean exito = false;

        // .get  devuelve si existe ese objeto en la tabla
        if (equipos.get(nombre) != null) {
            exito = true;

            // .remove  elimina el objeto de la tabla
            equipos.remove(nombre);
            desafiosResueltos.remove(nombre); //  borramos su historial de desafios
        }

        return exito;
    }

    public boolean modificarEquipo(String nombre,int puntajeExigido,int puntajeAcum,int codHab,int puntajeHab) {

        Boolean exito = false;
        Equipo equipoActual = equipos.get(nombre);
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codHab);

        if (equipos.get(nombre) != null && hab != null && puntajeExigido >= 0 && puntajeAcum >= 0 && puntajeHab >= 0) {

            exito = true;
            equipoActual.setPuntajeExigido(puntajeExigido);
            equipoActual.setPuntajeAcumulado(puntajeAcum);
            equipoActual.setCodigoHabitacion(codHab);
            equipoActual.setPuntajeHabitacion(puntajeHab);
        }

        return exito;
    }

    // ---------------------------------------------------------
    // CONSULTAS DE HABITACIONES Y DESAFÍOS
    // ---------------------------------------------------------
    //1. mostrar habitacion

    public String mostrarHabitacion(Integer habOrigen) {
        String resultado = "";

        // Lo buscamos en el avl
        if (casona.contiene(habOrigen)) {
            resultado = casona.obtenerInformacion(habOrigen).toString();
        } else {
            resultado = "Error: La habitacion "+ habOrigen+ " no existe en el mapa.";
        }
        return resultado;
    }

    // 4. sinPasarPor
    public String sinPasarPor(Integer habOrigen,Integer habDestino,int cantidadPuntaje,Integer habProhibida) {

        String resultado = "";

        if (casona.contiene(habOrigen)&& casona.contiene(habDestino)&& casona.contiene(habProhibida)) {

            resultado += "Caminos desde la habitacion "+ habOrigen+ " hasta la habitacion "+ habDestino+ " sin pasar por la habitacion "
            + habProhibida + " y con puntaje maximo de " + cantidadPuntaje + ":\n";

            resultado += mapa.caminosSinPasarPor(habOrigen,habDestino,habProhibida,cantidadPuntaje).toString();
        } else {
            resultado = "Error: Al menos una de las habitaciones " + "ingresadas no existe en el mapa.";
        }
        return resultado;
    }

    // 2. habitacionesContiguas
    public String habitacionesContiguas(Integer habOrigen) {
        String resultado = "";

        if (casona.contiene(habOrigen)) {
            resultado = mapa.obtenerAdyacentes(habOrigen).toString();
        } else {
            resultado = "Error: La habitacion "+ habOrigen+ " no existe en el mapa.";
        }

        return resultado;
    }

    // 3. esPosibleLlegar
    public boolean esPosibleLlegar(Integer habOrigen,Integer habDestino,int cantidadPuntaje) {

        boolean resultado = false;

        if (casona.contiene(habOrigen)
                && casona.contiene(habDestino)) {
            resultado = mapa.esPosibleLlegar(habOrigen,habDestino,cantidadPuntaje
            );
        }

        return resultado;
    }

    // 4. minimoPuntaje
    public String consultarMinimoPuntaje(Integer habOrigen,Integer habDestino) {

        String resultado = "";

        if (casona.contiene(habOrigen) && casona.contiene(habDestino)) {

            Lista caminoIdeal = new Lista();
            int[] puntajeMinimo = new int[1];

            boolean hayRuta = mapa.caminoMenorCosto(habOrigen,habDestino,caminoIdeal,puntajeMinimo);
            if (hayRuta) {
                resultado = "Minimo puntaje para pasar de "+ habOrigen+ " a "+ habDestino+ ": "+ puntajeMinimo[0]+ "\nCamino : "+ caminoIdeal.toString();
            } else {
                resultado = "Atencion: No hay puertas que conecten "+ "la habitacion "+ habOrigen+ " con la "+ habDestino+ ".";
            }
        } else {
            resultado = "Error: Al menos una de las habitaciones "+ "ingresadas no existe en el mapa.";
        }

        return resultado;
    }

    public String mostrarDesafio(int numeroDesafio,int numeroHabitacion) {

        String res = "";
        Habitacion hab =(Habitacion) casona.obtenerInformacion(numeroHabitacion);

        Desafio desafio = null;

        if (hab != null) {
            desafio = hab.buscarDesafio(numeroDesafio);

            if (desafio != null) {
                res += "Desafio encontrado: "+ desafio.toString()+ "\n";
            } else {
                res += "El desafio no existe en la "+ "habitacion especificada.\n";
            }
        } else {
            res += "La habitacion no existe.\n";
        }

        return res;
    }

    public String mostrarDesafiosResueltos(String nomEquipo) {
        String resultado = "";
        HashMap<Integer, Lista> desafiosHechos =
                desafiosResueltos.get(nomEquipo);

        if (desafiosHechos != null) {
            if (!desafiosHechos.isEmpty()) {
                resultado += "El equipo "+ nomEquipo+ " resolvio:\n";

                resultado += "Habitacion\tDesafios\n";

                for (Integer codHabitacion : desafiosHechos.keySet()) {
                    resultado += "("+ codHabitacion+ ")\t\t";

                    Lista desafios =desafiosHechos.get(codHabitacion).clone();

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
                resultado = "El equipo '"+ nomEquipo+ "' todavia no resolvio ningun desafio.";
            }
        } else {
            resultado = "Error: El equipo '"+ nomEquipo+ "' no esta registrado en el sistema.";
        }

        return resultado;
    }

    public String mostrarDesafiosTipo(int codHab,String tipoDesafio,int a,int b) {

        String resultado = "";
        Habitacion hab = (Habitacion) casona.obtenerInformacion(codHab);

        if (hab != null) {
            Lista desafios = hab.getDesafiosRango(a, b);

            if (!desafios.esVacia()) {
                resultado += "Desafios del tipo '"+ tipoDesafio+ "' en la habitacion "+ codHab+ " con puntaje entre "+ a+ " y "+ b+ ":\n";

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
                    resultado = "No se encontraron desafios del tipo '"+ tipoDesafio+ "' en el rango ["+ a+ ", "+ b+ "].";
                }
            } else {
                resultado = "No hay ningun tipo de desafio en el rango ["+ a+ ", "+ b+ "] en la habitacion "+ codHab+ ".";
            }
        } else {
            resultado = "Error: La habitacion "+ codHab+ " no existe.";
        }

        return resultado;
    }

    public boolean verificarDesafioResuelto(String nomEquipo,int codHabitacion,Desafio desafio) {

        boolean resuelto = false;
        HashMap<Integer, Lista> desafiosHechos =desafiosResueltos.get(nomEquipo);

        if (desafiosHechos != null) {
            Lista desafios = desafiosHechos.get(codHabitacion);

            if (desafios != null) {
                if (desafios.localizar(desafio) > 0) {
                    resuelto = true;
                }
            }
        }

        return resuelto;
    }

    // ---------------------------------------------------------
    // MÉTODOS DE EQUIPOS
    // ---------------------------------------------------------
    public String mostrarInfoEquipo(String nombre) {
        Equipo equipo = equipos.get(nombre);
        String data = "";

        if (equipo != null) {
            data = equipo.toString();
        } else {
            data = "No existe un equipo con ese nombre.";
        }

        return data;
    }

    //ejercicio 2
    public String posiblesDesafios(Equipo equipo,Habitacion hab) {

        String data = "";

        if (equipo != null) {
            Habitacion habEquipoParado =(Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());

            if (habEquipoParado != null && hab != null) {
                boolean adyacente = mapa.existeArco(habEquipoParado.getCodigo(),hab.getCodigo());

                if (adyacente) {
                    int diferencia = mapa.ObtenerEtiqueta(habEquipoParado.getCodigo(),hab.getCodigo()) - equipo.getPuntajeHabitacion();

                    if (diferencia <= 0) {
                        data = "El equipo puede pasar a la habitacion, "+ "no necesita completar ningun desafio";
                    } else {
                        HashMap<Integer, Lista> des = desafiosResueltos.get(equipo.getNombre());
                        Lista aux = null;
                        if (des != null) {
                            aux = des.get(equipo.getCodigoHabitacion());
                        }
                        Lista l = eliminarDesafiosRealizados(habEquipoParado,aux);
                        Lista filtrados = filtrarDesafiosPorPuntaje(l,diferencia);
                        if (!filtrados.esVacia()) {
                            data = "Los desafios posibles son: "+ filtrados.toString();
                        } else {
                            data = "Error, no existe un desafio "+ "posible para pasar de habitacion";
                        }
                    }
                } else {
                    data = "La habitacion no es adyacente al equipo";
                }
            } else {
                data = "Error una de las habitaciones esta vacia";
            }
        }

        return data;
    }

    private Lista eliminarDesafiosRealizados(Habitacion habActual,Lista desafiosResueltos) {

        Lista disponibles =habActual.getDesafios().listarInorden();

        if (desafiosResueltos != null&& !desafiosResueltos.esVacia()) {

            int i = 1;
            int longitud = desafiosResueltos.longitud();

            while (i <= longitud) {
                Desafio resuelto =(Desafio) desafiosResueltos.recuperar(i);

                int j = 1;
                boolean encontrado = false;

                while (j <= disponibles.longitud()&& !encontrado) { //necesario preguntar la longitud

                    //en cada iteracion.
                    Desafio actual =(Desafio) disponibles.recuperar(j);

                    if (actual.getPuntaje() == resuelto.getPuntaje()) {
                        disponibles.eliminar(j);
                        encontrado = true;
                    } else {
                        j++;
                    }
                }

                i++;
            }
        }

        return disponibles;
    }

    private Lista filtrarDesafiosPorPuntaje(Lista l,int diferencia) {

        //destruye la lista para eficiencia, cuidado
        Lista filtrados = new Lista();

        while (!l.esVacia()) {
            Desafio desafioActual = (Desafio) l.recuperar(1);

            if (desafioActual.getPuntaje() >= diferencia) {
                filtrados.insertar(desafioActual, 1);
            }

            l.eliminar(1);
        }

        return filtrados;
    }

    //ejercicio 3
    //ejercicio considerando que entra por parametro la habitacion
    // public boolean jugarDesafio(Equipo equipo, Habitacion laHabitacion, Desafio elDesafio) {
    //     boolean exito = revisarCondiciones(equipo, laHabitacion, elDesafio);
    //     if (exito) {
    //         equipo.setPuntajeHabitacion(equipo.getPuntajeHabitacion() + elDesafio.getPuntaje());
    //         equipo.setPuntajeAcumulado(equipo.getPuntajeAcumulado() + elDesafio.getPuntaje());
    //         HashMap<Integer, Lista> aux = desafiosResueltos.get(equipo.getNombre());
    //         if (aux == null) {
    //             aux = new HashMap<>();
    //             desafiosResueltos.put(equipo.getNombre(), aux);
    //         }
    //         Lista listaDesafios = aux.get(laHabitacion.getCodigo());
    //         if (listaDesafios == null) {
    //             listaDesafios = new Lista();
    //             aux.put(laHabitacion.getCodigo(), listaDesafios);
    //         }
    //         listaDesafios.insertar(elDesafio, listaDesafios.longitud() + 1);
    //     }
    //     return exito;
    // }
    // private boolean revisarCondiciones(Equipo equipo, Habitacion laHabitacion, Desafio elDesafio) {
    //     boolean exito = validarParametros(equipo, laHabitacion, elDesafio);
    //     if (exito) {
    //         Habitacion hab = (Habitacion) casona.obtenerInformacion(laHabitacion.getCodigo());
    //         exito = hab != null
    //                 && equipo.getCodigoHabitacion() == laHabitacion.getCodigo()
    //                 && hab.getDesafios().contiene(elDesafio.getPuntaje())
    //                 && desafioNoResuelto(equipo, elDesafio);
    //     }
    //     return exito;
    // }
    // private boolean validarParametros(Equipo equipo, Habitacion laHabitacion, Desafio elDesafio) {
    //     return equipo != null
    //             && laHabitacion != null
    //             && elDesafio != null;
    // }

    //ejercicio considerando que no es necesaria la habitacion por parametro
    public boolean jugarDesafio(Equipo equipo,Desafio elDesafio) { //ejercicio pide habitacion pero no es necesaria(?)

        boolean exito = revisarCondiciones(equipo, elDesafio);

        if (exito) {
            equipo.setPuntajeHabitacion(equipo.getPuntajeHabitacion()+ elDesafio.getPuntaje());

            equipo.setPuntajeAcumulado(equipo.getPuntajeAcumulado()+ elDesafio.getPuntaje());

            HashMap<Integer, Lista> aux =desafiosResueltos.get(equipo.getNombre());

            if (aux == null) {
                aux = new HashMap<Integer, Lista>();
                desafiosResueltos.put(equipo.getNombre(), aux);
            }

            Habitacion laHabitacion =(Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());

            Lista listaDesafios = aux.get(laHabitacion.getCodigo());

            if (listaDesafios == null) {
                listaDesafios = new Lista();
                aux.put(laHabitacion.getCodigo(), listaDesafios);
            }

            listaDesafios.insertar(elDesafio,listaDesafios.longitud() + 1);
        }

        return exito;
    }

    private boolean revisarCondiciones(Equipo equipo,Desafio elDesafio) {

        boolean exito = validarParametros(equipo, elDesafio);

        if (exito) {
            Habitacion hab =(Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());
            exito = hab != null   && equipo.getCodigoHabitacion()
            == elDesafio.getCodigoHabitacion()
            && hab.getDesafios().contiene(elDesafio.getPuntaje())
            && desafioNoResuelto(equipo, elDesafio);
        }

        return exito;
    }

    private boolean validarParametros(Equipo equipo,Desafio elDesafio) {

        boolean exito = equipo != null && elDesafio != null;
        return exito;
    }

    private boolean desafioNoResuelto(Equipo equipo,Desafio elDesafio) {

        boolean exito = true;
        HashMap<Integer, Lista> aux =desafiosResueltos.get(equipo.getNombre());

        if (aux != null) {
            Lista lista = aux.get(elDesafio.getCodigoHabitacion());

            if (lista != null) {
                exito = lista.localizar(elDesafio) < 0;
            }
        }

        return exito;
    }

    //ejericio 4
    public boolean cambiarDeHabitacion(Equipo equipo,Habitacion habAPasar) {

        boolean exito = verificarCondicionesCambio(equipo, habAPasar);

        if (exito) {
            equipo.setPuntajeHabitacion(0);
            equipo.setCodigoHabitacion(habAPasar.getCodigo());
        }

        return exito;
    }

    private boolean verificarCondicionesCambio(Equipo equipo,Habitacion habAPasar) {

        boolean exito = false;

        if (equipo != null && habAPasar != null) {
            Habitacion habActual =(Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());

            if (habActual != null) {
                exito = mapa.existeArco(habActual.getCodigo(),habAPasar.getCodigo()) 
                && equipo.getPuntajeHabitacion() >= mapa.ObtenerEtiqueta(habActual.getCodigo(),habAPasar.getCodigo());
            }
        }

        return exito;
    }

    public boolean puedeSalir(String nombreEquipo) {
        boolean exito = false;
        Equipo equipo = equipos.get(nombreEquipo);

        if (equipo != null) {
            Habitacion habActual =(Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());

            if (habActual != null) {
                exito = habActual.isTieneSalida()&& equipo.getPuntajeAcumulado()>= equipo.getPuntajeExigido();
            }
        }

        return exito;
    }

    // ---------------------------------------------------------
    // MOSTRAR SISTEMA
    // ---------------------------------------------------------

    public String mostrarSistema() {
        String resultado =
                "\n============================================================\n"
                        + "               ESTADO COMPLETO ESCAPE HOUSE\n"
                        + "============================================================\n"
                        + mostrarAVLHabitaciones()
                        + mostrarAVLDesafios()
                        + mostrarGrafo()
                        + mostrarEquipos()
                        + mostrarDesafiosResueltos()
                        + "============================================================\n";

        return resultado;
    }

    private String mostrarAVLHabitaciones() {
        String resultado =
                "\n1. AVL DE HABITACIONES\n"
                        + "------------------------------------------------------------\n"
                        + this.casona.toString()
                        + "\n";

        return resultado;
    }

    private String mostrarAVLDesafios() {
        String resultado =
                "\n2. AVL DE DESAFIOS POR HABITACION\n"
                        + "------------------------------------------------------------\n";

        Lista habitaciones = this.casona.listarInorden();

        if (habitaciones.esVacia()) {
            resultado += "No hay habitaciones cargadas.\n";
        } else {
            for (int pos = 1; pos <= habitaciones.longitud(); pos++) {
                Habitacion hab =
                        (Habitacion) habitaciones.recuperar(pos);

                resultado += "\nHabitacion "
                        + hab.getCodigo()
                        + " - "
                        + hab.getNombre()
                        + "\n"
                        + hab.getDesafios().toString()
                        + "\n";
            }
        }

        return resultado;
    }

    private String mostrarGrafo() {
        String resultado =
                "\n3. GRAFO DE HABITACIONES Y PUERTAS\n"
                        + "------------------------------------------------------------\n"
                        + this.mapa.toString();

        return resultado;
    }

    private String mostrarEquipos() {
        String resultado =
                "\n4. HASH DE EQUIPOS\n"
                        + "------------------------------------------------------------\n";

        if (this.equipos.isEmpty()) {
            resultado += "No hay equipos registrados.\n";
        } else {
            for (String nombreEquipo : this.equipos.keySet()) {
                Equipo equipo = this.equipos.get(nombreEquipo);

                resultado += "CLAVE HASH: "
                        + nombreEquipo
                        + "\n"
                        + "VALOR: "
                        + equipo.toString()
                        + "\n";
            }
        }

        return resultado;
    }

    private String mostrarDesafiosResueltos() {
        String resultado =
                "\n5. MAPEO DE DESAFIOS RESUELTOS\n"
                        + "------------------------------------------------------------\n";

        if (this.desafiosResueltos.isEmpty()) {
            resultado += "No hay equipos registrados en el historial.\n";
        } else {
            for (String nombreEquipo : this.desafiosResueltos.keySet()) {
                HashMap<Integer, Lista> historial =
                        this.desafiosResueltos.get(nombreEquipo);

                resultado += "EQUIPO: "
                        + nombreEquipo
                        + "\n";

                resultado += historial.isEmpty()
                        ? "    Sin desafios resueltos.\n"
                        : mostrarHistorialEquipo(historial);
            }
        }

        return resultado;
    }

    private String mostrarHistorialEquipo(
            HashMap<Integer, Lista> historial) {

        String resultado = "";

        for (Integer codigoHabitacion : historial.keySet()) {
            Lista desafios = historial.get(codigoHabitacion);

            resultado += "    HABITACION: "
                    + codigoHabitacion
                    + "\n"
                    + "    DESAFIOS: "
                    + desafios.toString()
                    + "\n";
        }

        return resultado;
    }
}