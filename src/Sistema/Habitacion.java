package Sistema;

import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Lista.Lista;
public class Habitacion {
    private int codigo;
    private String nombre;
    private int planta;
    private double metrosCuadrados;
    private boolean tieneSalida;
    private DiccionarioAVL desafios;

    public Habitacion(int codigo, String nombre, int planta, double metrosCuadrados, boolean tieneSalida) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.planta = planta;
        this.metrosCuadrados = metrosCuadrados;
        this.tieneSalida = tieneSalida;
        this.desafios = new DiccionarioAVL();
    }
    
    public Desafio buscarDesafio(int numeroDesafio) {
        return (Desafio) desafios.obtenerInformacion(numeroDesafio);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPlanta() {
        return planta;
    }

    public void setPlanta(int planta) {
        this.planta = planta;
    }

    public double getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(double metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public boolean isTieneSalida() {
        return tieneSalida;
    }

    public void setTieneSalida(boolean tieneSalida) {
        this.tieneSalida = tieneSalida;
    }

    public DiccionarioAVL getDesafios() {
        return desafios;
    }

    public void setDesafios(DiccionarioAVL desafios) {
        this.desafios = desafios;
    }
    public Lista getDesafiosRango(int min, int max) {
        return desafios.listarRango(min, max);
    }
    
    @Override
    public String toString() {
        return "Habitacion{" +
                "codigo=" + this.codigo +
                ", nombre='" + this.nombre + '\'' +
                ", planta=" + this.planta +
                ", metrosCuadrados=" + this.metrosCuadrados +
                ", tieneSalida=" + this.tieneSalida +
                '}';
    }
}