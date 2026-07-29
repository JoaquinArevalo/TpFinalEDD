package Sistema;

public class Equipo {
    private String nombre;
    private int puntajeExigido;
    private int puntajeAcumulado;
    private int codigoHabitacion;
    private int puntajeHabitacion;

    public Equipo(String nombre, int puntajeExigido, int puntajeAcumulado, int codHabitacion, int puntajeHabitacion) {
        this.nombre = nombre;
        this.puntajeExigido = puntajeExigido;
        this.puntajeAcumulado = puntajeAcumulado;
        this.codigoHabitacion = codHabitacion;
        this.puntajeHabitacion = puntajeHabitacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntajeExigido() {
        return puntajeExigido;
    }

    public void setPuntajeExigido(int puntajeExigido) {
        this.puntajeExigido = puntajeExigido;
    }

    public int getPuntajeAcumulado() {
        return puntajeAcumulado;
    }

    public void setPuntajeAcumulado(int puntajeAcumulado) {
        this.puntajeAcumulado = puntajeAcumulado;
    }

    public int getCodigoHabitacion() {
        return codigoHabitacion;
    }

    public void setCodigoHabitacion(int codigoHabitacion) {
        this.codigoHabitacion = codigoHabitacion;
    }

    public int getPuntajeHabitacion() {
        return puntajeHabitacion;
    }

    public void setPuntajeHabitacion(int puntajeHabitacion) {
        this.puntajeHabitacion = puntajeHabitacion;
    }
}