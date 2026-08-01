package Sistema.Lista;

//import java.lang.annotation.Target;

//import org.junit.Test;

public class Lista {
    private Nodo cabecera;
    private int longitud;
    
    public Lista(){
        this.cabecera=null;
        this.longitud=0;
    }
    public boolean insertar(Object elem, int pos){
    boolean exito = false;
    if(pos >= 1 && pos <= this.longitud + 1){
        if(pos == 1){
            this.cabecera = new Nodo(elem, this.cabecera);
        } else {
            Nodo aux = recorrer(pos - 1);
            Nodo nuevo = new Nodo(elem, aux.getEnlace());
            aux.setEnlace(nuevo);
        }
        this.longitud++;
        exito = true;
    }
    return exito;
}
    public boolean eliminar(int pos){
        boolean exito=false;
        if(pos>0 && pos<this.longitud+1){
            if(pos!=1){
                Nodo aux=recorrer(pos-1);
                aux.setEnlace(aux.getEnlace().getEnlace());
            }else{
                this.cabecera=this.cabecera.getEnlace();
            }
            exito=true;
            this.longitud--;
        }
        return exito;
    }
    public Object recuperar(int pos){
        Object elem= null;
        if (pos >= 1 && pos <= this.longitud){
            Nodo aux= recorrer(pos);
            elem=aux.getElem();
        }
        return elem;
    }
    //el mod recorrer queda justo en la posicion
    private Nodo recorrer(int pos){
        int i=1;
        Nodo aux=this.cabecera;
        while(i<pos){
            aux=aux.getEnlace();
            i++;
        }
        return aux;
    }
    public int localizar(Object elem){
        int i=1,j=-1;
        Nodo aux=this.cabecera;
        while(aux != null && !aux.getElem().equals(elem)){
            aux=aux.getEnlace();
            i++;
        }
        return (aux!=null)? i: j;
    }
    public int longitud(){
        return this.longitud;
    }
    public boolean esVacia(){
        return this.cabecera==null;
    }
    public void vaciar(){
        this.cabecera=null;
        this.longitud=0;
    }
    public Lista clone(){
        Lista unLista= new Lista();
        if(this.cabecera!=null){
            unLista.cabecera=new Nodo(this.cabecera.getElem(),null);
            Nodo aux=this.cabecera.getEnlace();
            Nodo recorredor=unLista.cabecera;
            while(aux!=null){
                Nodo otroNodo=new Nodo(aux.getElem(), null);
                recorredor.setEnlace(otroNodo);
                recorredor=otroNodo;
                aux=aux.getEnlace();
            }
            unLista.longitud=this.longitud;
        }
        return unLista;
    }
    @Override
    public String toString() {
        String resultado = "[";
        Nodo auxiliar = this.cabecera;

        while (auxiliar != null) {
            if (auxiliar.getElem() == null) {
                resultado += "null";
            } else {
                resultado += auxiliar.getElem().toString();
            }
            auxiliar = auxiliar.getEnlace();
            if (auxiliar != null) {
                resultado += ", ";
            }
        }
        resultado += "]";
        return resultado;
    }

    //propios del tipo ej4

    public void invertir(){
        if(this.cabecera!=null && this.cabecera.getEnlace()!=null){
            Nodo aux1=null;
            Nodo aux2=this.cabecera;
            Nodo aux3=null;
            while(aux2!=null){
                aux3=aux2.getEnlace();
                aux2.setEnlace(aux1);
                aux1=aux2;
                aux2=aux3;

            }
            this.cabecera=aux1;
        }
    }
    public void eliminarApariciones(Object elem){
        // eliminar desde la cabecera
        while(this.cabecera != null && this.cabecera.getElem().equals(elem)){
            this.cabecera = this.cabecera.getEnlace();
            this.longitud--;
        }

        Nodo aux2 = this.cabecera;
        Nodo aux = (this.cabecera != null) ? this.cabecera.getEnlace() : null;

        while(aux != null){
            if(aux.getElem().equals(elem)){
                aux2.setEnlace(aux.getEnlace());
                this.longitud--;
                aux = aux2.getEnlace();
            } else {
                aux2 = aux;
                aux = aux.getEnlace();
            }
        }
    }

    //EJERCICIOS ADICIONALES
    public Lista intercalar(Lista l2){
        Lista listaFinal=new Lista();
        Nodo aux1=this.cabecera;  
        Nodo aux2=l2.cabecera;
        if(this.cabecera==null && l2.cabecera!=null){
            listaFinal.cabecera=new Nodo(aux2.getElem(), listaFinal.cabecera);
            aux2=aux2.getEnlace();
        }else if(this.cabecera!=null){
            listaFinal.cabecera=new Nodo(aux1.getElem(), listaFinal.cabecera);
            aux1=aux1.getEnlace();
        }
        Nodo list=listaFinal.cabecera;
        while(aux1!=null || aux2!=null){
            if(aux2!=null){
                Nodo papa= new Nodo(aux2.getElem(), null);
                list.setEnlace(papa);
                aux2=aux2.getEnlace();
                list=papa;
            }
            if(aux1!=null){
                Nodo mama=new Nodo(aux1.getElem(), null);
                list.setEnlace(mama);
                aux1=aux1.getEnlace();
                list=mama;
            }
        }
        return listaFinal;
    }
    public int contarIterativo(Object elem){
        Nodo aux=null;
        if(this.cabecera!=null){
            aux=this.cabecera;
        }
        int i=0;
        while(aux!=null){
            if(aux.getElem().equals(elem)){
                i++;
            }
            aux=aux.getEnlace();
        }
        return i;
    }
    private int recursivo(Object elem, Nodo aux){
        int cont=0;
        if(aux!=null){
            if(aux.getElem().equals(elem)){
                cont++;
            }
            cont+=recursivo(elem, aux.getEnlace());
        }
        return cont;
    }
    public int contarRecursivo(Object elem){
        Nodo aux=this.cabecera;
        int cont=recursivo(elem, aux);
        return cont;
    }
    public boolean esCapicua(){
        boolean valor=true;



        
        return valor;
    }
}
