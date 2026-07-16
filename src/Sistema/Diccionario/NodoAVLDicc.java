package Sistema.Diccionario;

public class NodoAVLDicc {
    private Comparable clave;
    private Object dato;
    private int altura;
    private NodoAVLDicc izquierdo;
    private NodoAVLDicc derecho;

    public NodoAVLDicc(Comparable clave, Object dato) {
        this.clave = clave;
        this.dato = dato;
        this.altura = 0; // Un nodo hoja arranca siempre con altura 0
        this.izquierdo = null;
        this.derecho = null;
    }

    public Comparable getClave() { return clave; }
    public Object getDato() { return dato; }
    public void setDato(Object dato) { this.dato = dato; }
    public int getAltura() { return altura; }
    public NodoAVLDicc getIzquierdo() { return izquierdo; }
    public void setIzquierdo(NodoAVLDicc izquierdo) { this.izquierdo = izquierdo; }
    public NodoAVLDicc getDerecho() { return derecho; }
    public void setDerecho(NodoAVLDicc derecho) { this.derecho = derecho; }
    public void setClave(Comparable clave) { this.clave = clave; }

    public void recalcularAltura() {
        int altIzq = (this.izquierdo != null) ? this.izquierdo.getAltura() : -1;
        int altDer = (this.derecho != null) ? this.derecho.getAltura() : -1;
        this.altura = Math.max(altIzq, altDer) + 1;
    }
}
