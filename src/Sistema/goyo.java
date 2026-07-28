package Sistema;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Grafo.Grafo;
import Sistema.Lista.Lista;

public class goyo {
    /** 
    /home/goyo/Escritorio/RepositoriosGit/facultad/EDD/TPO/TpFinalEDD (goyo)
    **/
    public static void cargarDesdeArchivo(String rutaArchivo, Grafo grafo, DiccionarioAVL habitacionesAVL, HashMap<String, Equipo> equiposHash, HashMap<String, Lista> desafiosResueltos) {
        /**bufferedReader, utiliza buffer(memoria temporal para gestionar el flujo de datos)
        FileReader lee el archivo de teto y almacena los datos en el bufferReader
        **/
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            
            while ((linea = br.readLine()) != null) {
                // saltear lineas vacias o comentarios
                if (linea.trim().isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                //separa el texto por los punto y coma
                String[] datos = linea.split(";");
                String tipo = datos[0].toUpperCase();

                switch (tipo) {
                    case "H":
                        //leer Habitacion
                        int codHab = Integer.parseInt(datos[1]);
                        String nomHab = datos[2];
                        int planta = Integer.parseInt(datos[3]);
                        double m2 = Double.parseDouble(datos[4]);
                        boolean salida = Boolean.parseBoolean(datos[5]);
                        
                        Habitacion hab = new Habitacion(codHab, nomHab, planta, m2, salida);
                        
                        habitacionesAVL.insertar(codHab, hab);
                        grafo.insertarVertice(codHab);
                        break;

                    case "P":
                        //leer Puertas
                        int origen = Integer.parseInt(datos[1]);
                        int destino = Integer.parseInt(datos[2]);
                        int puntajeMin = Integer.parseInt(datos[3]);
                        
                        grafo.insertarArco(origen, destino, puntajeMin);
                        grafo.insertarArco(destino, origen, puntajeMin);
                        break;

                    case "D":
                        //leer desafios
                        int puntajeDesafio = Integer.parseInt(datos[1]);                        
                        int codHabDesafio = Integer.parseInt(datos[2]);
                        String nomDesafio = datos[3];
                        String tipoDesafio = datos[4];
                        
                        Desafio des = new Desafio(codHabDesafio, puntajeDesafio, nomDesafio, tipoDesafio);
                        
                        Habitacion habDestino = (Habitacion) habitacionesAVL.obtenerInformacion(codHabDesafio);
                        if (habDestino != null) {
                            // La clave del desafío en el AVL es su puntaje
                            habDestino.getDesafios().insertar(puntajeDesafio, des);
                        }
                        break;

                    case "E":
                        // EQUIPO;nombre;puntajeExigido;habActual
                        String nomEquipo = datos[1];
                        int puntajeExigido = Integer.parseInt(datos[2]);
                        int puntajeAcumulado = Integer.parseInt(datos[3]);
                        int codigoHabitacion = Integer.parseInt(datos[4]);
                        int puntajeHabitacion = Integer.parseInt(datos[5]); 
                        
                        Equipo eq = new Equipo(nomEquipo, puntajeExigido, puntajeAcumulado, codigoHabitacion, puntajeHabitacion);
                        equiposHash.put(nomEquipo, eq);
                        if (!desafiosResueltos.containsKey(nomEquipo)){
                            desafiosResueltos.put(nomEquipo,new Lista());
                        }
                        Lista listaDesafiosEquipo = desafiosResueltos.get(nomEquipo);
                        for (int i = 6; i < datos.length; i++) {
                            String item = datos[i].trim(); // Ej: "(1,20)"
                            if (!item.isEmpty()) {
                                // Eliminamos los paréntesis '(' y ')'
                                String limpio = item.replace("(", "").replace(")", ""); // Queda "1,20"
                                
                                // Separamos por la coma
                                String[] partes = limpio.split(","); // partes[0] = "1", partes[1] = "20"
                                
                                int codigoHab = Integer.parseInt(partes[0].trim());
                                int puntaje = Integer.parseInt(partes[1].trim());
                                
                                Habitacion habBusqueda = (Habitacion) habitacionesAVL.obtenerInformacion(codigoHab);
                                if(habBusqueda!=null){
                                    Desafio desBuscado = (Desafio) habBusqueda.getDesafios().obtenerInformacion(puntaje);
                                    if(desBuscado!=null){
                                        listaDesafiosEquipo.insertar(desBuscado, listaDesafiosEquipo.longitud()+1);
                                    }else{
                                        System.out.println("no se ha encontrado un desafio que otorge: "+ puntaje +" en la Habitacion con el codigo: "+ codigoHab);
                                    }
                                }else{
                                    System.out.println("no se ha encontrado la habitacion con el codigo "+ codigoHab + "para el equipo "+ nomEquipo);
                                }
                            }
                        }
                        break;

                    default:
                        System.out.println("No existe el tipo: " + tipo);
                        break;
                }
            }
            System.out.println("Carga inicial completada con éxito.");

        } catch (IOException e) {//error al leer/escribir un archivo
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {//error al pasar de text a numero(parsear)
            System.err.println("Error en el formato de los números del archivo: " + e.getMessage());
        }
    }
}
