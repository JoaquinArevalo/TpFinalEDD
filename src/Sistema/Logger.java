package Sistema;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private BufferedWriter escritor;
    private boolean disponible;
    private DateTimeFormatter formatoFecha;

    public Logger(String rutaArchivo) {
        this.escritor = null;
        this.disponible = false;
        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        try {
            this.escritor = new BufferedWriter(new FileWriter(rutaArchivo, false));
            this.escritor.write("LOG DEL SISTEMA ESCAPE HOUSE\n" +
                                "Inicio de ejecucion: " + obtenerFechaHoraActual() + "\n" +
                                "============================================================\n");
            this.escritor.flush();
            this.disponible = true;

        } catch (IOException excepcion) {
            System.err.println("No fue posible abrir el archivo de log: " + excepcion.getMessage());
            this.escritor = null;
            this.disponible = false;
        }
    }

    public boolean registrarEstadoInicial(String estadoSistema) {
        return escribirBloque("ESTADO DEL SISTEMA DESPUES DE LA CARGA INICIAL", estadoSistema);
    }
    public boolean registrarAlta(String detalle) { return escribirLinea("ALTA", detalle); }
    public boolean registrarBaja(String detalle) { return escribirLinea("BAJA", detalle); }
    public boolean registrarModificacion(String detalle) { return escribirLinea("MODIFICACION", detalle); }
    public boolean registrarMensaje(String detalle) { return escribirLinea("INFORMACION", detalle); }
    public boolean registrarError(String detalle) { return escribirLinea("ERROR", detalle); }
    public boolean registrarEstadoFinal(String estadoSistema) {
        return escribirBloque("ESTADO FINAL DEL SISTEMA", estadoSistema);
    }

    private boolean escribirLinea(String tipoOperacion, String detalle) {
        boolean exito = false;

        if (this.disponible && this.escritor != null) {
            try {
                this.escritor.write("[" + obtenerFechaHoraActual() + "] [" + tipoOperacion + "] " + detalle + "\n");
                this.escritor.flush();
                exito = true;
            } catch (IOException excepcion) {
                System.err.println("No fue posible escribir en el log: " + excepcion.getMessage());
                this.disponible = false;
            }
        }
        return exito;
    }

    private boolean escribirBloque(String titulo, String contenido) {
        boolean exito = false;

        if (this.disponible && this.escritor != null) {
            try {
                String textoContenido = (contenido != null) ? contenido : "No se recibio informacion del sistema.";
                this.escritor.write("\n============================================================\n" +
                                    "[" + obtenerFechaHoraActual() + "] " + titulo + "\n" +
                                    "============================================================\n" +
                                    textoContenido + "\n");
                this.escritor.flush();
                exito = true;
            } catch (IOException excepcion) {
                System.err.println("No fue posible escribir el estado en el log: " + excepcion.getMessage());
                this.disponible = false;
            }
        }
        return exito;
    }

    private String obtenerFechaHoraActual() {
        return LocalDateTime.now().format(this.formatoFecha);
    }

    public boolean estaDisponible() {
        return this.disponible;
    }

    public boolean cerrar() {
        boolean exito = false;

        if (this.escritor != null) {
            try {
                this.escritor.write("Fin de ejecucion: " + obtenerFechaHoraActual() + "\n");
                this.escritor.flush();
                this.escritor.close(); // Se cierra el archivo
                
                this.escritor = null;
                this.disponible = false;
                exito = true;
            } catch (IOException excepcion) {
                System.err.println("No fue posible cerrar el archivo de log: " + excepcion.getMessage());
                this.disponible = false;
            }
        }
        return exito;
    }
}