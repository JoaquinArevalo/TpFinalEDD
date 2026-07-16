package Sistema.Lista;

class Nodo {
    private Object elem;
    private Nodo enlace;

    //constructor
    public Nodo(Object unelem, Nodo unenlace){
        this.elem=unelem;
        this.enlace=unenlace;
    }
    //modificadores
    public void setElem(Object otroelem){
        this.elem=otroelem;
    }
    public void setEnlace(Nodo otroenlace){
        this.enlace=otroenlace;
    }
    //observadores
    public Object getElem(){
        return this.elem;
    }
    public Nodo getEnlace(){
        return this.enlace;
    }
}