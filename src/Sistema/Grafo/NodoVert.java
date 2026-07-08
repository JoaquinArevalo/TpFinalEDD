package Sistema.Grafo;

public class NodoVert {
    private Object elem;
    private NodoVert sigVertice;
    private NodoAdy primerAdy;

    public NodoVert(Object unElem, NodoVert unSig, NodoAdy unAdy){
        this.elem=unElem;
        this.sigVertice=unSig;
        this.primerAdy=unAdy;
    } 
    public Object getElem(){
        return this.elem;
    }
    public NodoVert getSigVertice(){
        return this.sigVertice;
    }
    public NodoAdy getPrimerAdy(){
        return this.primerAdy;
    }
    public void setElem(Object unElem){
        this.elem=unElem;
    }
    public void setSigVertice(NodoVert unVert){
        this.sigVertice=unVert;
    }
    public void setPrimerAdy(NodoAdy otroADy){
        this.primerAdy=otroADy;
    }
}
