package Sistema;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Grafo.Grafo;

public class goyo {
    /** 
    C:\Users\Usuario\Desktop\TPo\TpFinalEDD\casona.txt (goyo)
    **/
    public static void cargarDesdeArchivo(String rutaArchivo, Grafo grafo, DiccionarioAVL habitacionesAVL, HashMap<String, Equipo> equiposHash) {
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
                    case "HABITACION":
                        int codHab = Integer.parseInt(datos[1]);
                        String nomHab = datos[2];
                        int planta = Integer.parseInt(datos[3]);
                        double m2 = Double.parseDouble(datos[4]);
                        boolean salida = Boolean.parseBoolean(datos[5]);
                        
                        Habitacion hab = new Habitacion(codHab, nomHab, planta, m2, salida);
                        
                        habitacionesAVL.insertar(codHab, hab);
                        grafo.insertarVertice(codHab);
                        break;

                    case "CONEXION":
                        String origen = datos[1];
                        String destino = datos[2];
                        int puntajeMin = Integer.parseInt(datos[3]);
                        
                        grafo.insertarArco(origen, destino, puntajeMin);
                        grafo.insertarArco(destino, origen, puntajeMin);
                        break;

                    case "DESAFIO":
                        int codHabDesafio = Integer.parseInt(datos[1]);
                        int puntajeDesafio = Integer.parseInt(datos[2]);
                        String nomDesafio = datos[3];
                        String tipoDesafio = datos[4];
                        
                        Desafio des = new Desafio(codHabDesafio, puntajeDesafio, nomDesafio, tipoDesafio);
                        
                        Habitacion habDestino = (Habitacion) habitacionesAVL.obtenerInformacion(codHabDesafio);
                        if (habDestino != null) {
                            // La clave del desafío en el AVL es su puntaje
                            habDestino.getDesafios().insertar(puntajeDesafio, des);
                        }
                        break;

                    case "EQUIPO":
                        // EQUIPO;nombre;puntajeExigido;habActual
                        String nomEquipo = datos[1];
                        int puntajeExigido = Integer.parseInt(datos[2]);
                        int puntajeAcumulado = Integer.parseInt(datos[3]);
                        int codigoHabitacion = Integer.parseInt(datos[4]);
                        int puntajeHabitacion = Integer.parseInt(datos[5]); 
                        
                        Equipo eq = new Equipo(nomEquipo, puntajeExigido, puntajeAcumulado, codigoHabitacion, puntajeHabitacion);
                        equiposHash.put(nomEquipo, eq);
                        break;

                    default:
                        System.out.println("No existe el tipo: " + tipo);
                        break;
                }
            }
            System.out.println("Carga inicial completada con éxito.");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error en el formato de los números del archivo: " + e.getMessage());
        }
    }
}
