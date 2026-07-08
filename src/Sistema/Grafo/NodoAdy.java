package Sistema.Grafo;

public class NodoAdy {
    private NodoVert vertice;
    private NodoAdy sigAdyacente;
    private int etiqueta;
    public NodoAdy(NodoVert vert, NodoAdy unAdy, int unaEtiqueta){
        this.vertice=vert;
        this.sigAdyacente=unAdy;
        this.etiqueta=unaEtiqueta;
    }
    public NodoVert getVertice(){
        return this.vertice;
    }
    public NodoAdy getSigAdyacente(){
        return this.sigAdyacente;
    }
    public int getEtiqueta(){
        return this.etiqueta;
    }
    public void setVertice(NodoVert unVertice){
        this.vertice=unVertice;
    }
    public void setSigAdyacente(NodoAdy unAdy){
        this.sigAdyacente=unAdy;
    }
    public void setEtiqueta(int unaEtiqueta){
        this.etiqueta=unaEtiqueta;
    }
}
