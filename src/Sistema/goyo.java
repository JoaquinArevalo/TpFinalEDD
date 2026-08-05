package Sistema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Grafo.Grafo;
import Sistema.Lista.Lista;

public class goyo {

    public static boolean cargarDesdeArchivo(String rutaArchivo, Grafo grafo, DiccionarioAVL habitacionesAVL, HashMap<String, Equipo> equiposHash, HashMap<String, HashMap<Integer, Lista>> desafiosResueltos) {
        boolean cargaExitosa = false;
        
        // Listas temporales
        Lista lineasHabitaciones = new Lista();
        Lista lineasPuertas = new Lista();
        Lista lineasDesafios = new Lista();
        Lista lineasEquipos = new Lista();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
             FileWriter logWriter = new FileWriter("log_escape_house.txt", true)) {
            
            String linea;
            
            //Lectura y clasificacion
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                
                char tipo = linea.toUpperCase().charAt(0);
                switch (tipo) {
                    //se insertan en la posicion 1 para que no recorran de mas
                    case 'H': lineasHabitaciones.insertar(linea, 1); break;
                    case 'P': lineasPuertas.insertar(linea, 1); break;
                    case 'D': lineasDesafios.insertar(linea, 1); break;
                    case 'E': lineasEquipos.insertar(linea, 1); break;
                    default: System.out.println("Línea ignorada por tipo desconocido: " + linea); break;
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
                    
                    Habitacion hab = new Habitacion(codHab, nomHab, planta, m2, salida);
                    habitacionesAVL.insertar(codHab, hab);
                    grafo.insertarVertice(codHab);
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
                    
                    grafo.insertarArco(origen, destino, puntajeMin);
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
                    
                    Desafio des = new Desafio(codHabDesafio, puntajeDesafio, nomDesafio, tipoDesafio);
                    Habitacion habDestino = (Habitacion) habitacionesAVL.obtenerInformacion(codHabDesafio);
                    
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
                    
                    Equipo eq = new Equipo(nomEquipo, puntajeExigido, puntajeAcumulado, codigoHabitacion, puntajeHabitacion);
                    equiposHash.put(nomEquipo, eq);
                    
                    if (!desafiosResueltos.containsKey(nomEquipo)){
                        desafiosResueltos.put(nomEquipo, new HashMap<Integer, Lista>());
                    }

                    // Obtenemos el historial de habitaciones de este equipo
                    HashMap<Integer, Lista> historialHabitaciones = desafiosResueltos.get(nomEquipo);
                    
                    for (int j = 6; j < datos.length; j++) {
                        String item = datos[j].trim();
                        if (item.isEmpty()) continue;
                        
                        // Si tiene formato agrupado ej: (1:20,50)(2:30)
                        if (item.contains(":")) {
                            // Usamos StringTokenizer con los delimitadores: paréntesis de apertura, de cierre y espacio
                            StringTokenizer st = new StringTokenizer(item, "() ");
                            
                            while (st.hasMoreTokens()) {
                                // nextToken() nos dará directamente "1:20,50", "2:30", etc.
                                String g = st.nextToken(); 
                                String[] partesGrupo = g.split(":");
                                
                                if (partesGrupo.length == 2) {
                                    int codHab = Integer.parseInt(partesGrupo[0].trim());
                                    String[] puntajes = partesGrupo[1].split(",");
                                    
                                    Habitacion hab = (Habitacion) habitacionesAVL.obtenerInformacion(codHab);
                                    if (hab != null) {
                                        if (!historialHabitaciones.containsKey(codHab)) {
                                            historialHabitaciones.put(codHab, new Lista());
                                        }
                                        // Recuperamos la lista específica de esa habitación
                                        Lista listaDesafiosHabitacion = historialHabitaciones.get(codHab);
                                        
                                        for (String pts : puntajes) {
                                            int p = Integer.parseInt(pts.trim());
                                            Desafio d = (Desafio) hab.getDesafios().obtenerInformacion(p);
                                            if (d != null) {
                                                // Insertamos en la lista de la habitación correspondiente
                                                listaDesafiosHabitacion.insertar(d, listaDesafiosHabitacion.longitud() + 1);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Formato separado ej: (1,20)
                            String limpio = item.replace("(", "").replace(")", "").replace("$", "");
                            String[] partes = limpio.split(",");
                            if(partes.length == 2) {
                                int codHab = Integer.parseInt(partes[0].trim());
                                int puntaje = Integer.parseInt(partes[1].trim());
                                
                                Habitacion hab = (Habitacion) habitacionesAVL.obtenerInformacion(codHab);
                                if (hab != null) {

                                    if (!historialHabitaciones.containsKey(codHab)) {
                                        historialHabitaciones.put(codHab, new Lista());
                                    }
                                    Lista listaDesafiosHabitacion = historialHabitaciones.get(codHab);

                                    Desafio d = (Desafio) hab.getDesafios().obtenerInformacion(puntaje);
                                    if (d != null) {
                                        listaDesafiosHabitacion.insertar(d, listaDesafiosHabitacion.longitud() + 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // ETAPA 3: Registro en archivo de Log
            logWriter.write("Carga Inicial del Sistema\n");
            logWriter.write("Total Habitaciones cargadas: " + lineasHabitaciones.longitud() + "\n");
            logWriter.write("Total Puertas cargadas: " + lineasPuertas.longitud() + "\n");
            logWriter.write("Total Desafios cargados: " + lineasDesafios.longitud() + "\n");
            logWriter.write("Total Equipos cargados: " + lineasEquipos.longitud() + "\n");            
            cargaExitosa = true;

        } catch (IOException e) {
            System.err.println("Error de entrada/salida al procesar archivos: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error de formato numérico en los datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado durante la carga: " + e.getMessage());
        }

        return cargaExitosa;
    }
}