package Sistema;

public class Desafio {
    private int codigoHabitacion;
    private int puntaje;
    private String nombre;
    private String tipo;

    public Desafio(int codigoHabitacion, int puntaje, String nombre, String tipo) {
        this.codigoHabitacion = codigoHabitacion;
        this.puntaje = puntaje;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public int getCodigoHabitacion() {
        return codigoHabitacion;
    }

    public void setCodigoHabitacion(int codigoHabitacion) {
        this.codigoHabitacion = codigoHabitacion;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
@Override
public boolean equals(Object obj) {
    boolean sonIguales = false;
    if (this == obj) {
        sonIguales = true;
    }
    else if (obj != null && this.getClass() == obj.getClass()) {

        Desafio otroDesafio = (Desafio) obj;
        sonIguales = (this.codigoHabitacion == otroDesafio.codigoHabitacion &&
                this.puntaje == otroDesafio.puntaje);
    }
    return sonIguales;
}
    public String toString() {
        return "Desafio{" +
                "codigoHabitacion=" + codigoHabitacion +
                ", puntaje=" + puntaje +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}