/*
************ Autores ***********
-FAI-5556	Parra, Joaquin
-FAI-5731	Benavides, Ethan
-FAI-5762	Larronde, Gregorio
-FAI-5771	Arevalo, Joaquin 
*/
package Sistema.Lista;

public class Cola {
    private Nodo frente;
    private Nodo fin;

    public Cola() {
        this.frente = null;
        this.fin = null;
    }

    public boolean poner(Object elem) {
        Nodo nuevo = new Nodo(elem, null);
        if (this.frente == null) {//si esta vacio la cola
            this.frente = nuevo;//el frente apunta al nodo nuevo, frente->[Nuevo]
        } else {
            this.fin.setEnlace(nuevo);//si no estaba vacio el viejo fin apunta al ultimo agregado(que mas adelante puede ser el frente tambien)
                                    //frente ->[A]->[Nuevo]
                                    //          ^ 
                                    //         fin
        }
        this.fin = nuevo;//frente->[A]->[nuevo]<- fin
        return true;
    }
    public boolean sacar(){
        boolean exito=false;
        if(this.frente!=null){//que no sea vacio
            this.frente=this.frente.getEnlace();//frente->[A]->[B]->[C]
                                                //frente->[B]->[C](con el garbage desaparece A)      
            exito=true;
            if(this.frente==null){//si no hay mas elementos fin se pone null tmb
                this.fin=null;
            }
        }
        return exito;
    }
    public Object obtenerFrente(){
        return (this.frente != null) ? this.frente.getElem() : null;
    }
    public boolean esVacia(){
        return this.frente==null;
    }
    public void vaciar(){
        this.frente=null;
        this.fin=null;
    }
    public Cola clone(){
        Cola clon= new Cola();
        if(this.frente!=null){
            Nodo nodoActual=this.frente.getEnlace();
            //siguiente nodo de la cola
            Nodo nodoClon= new Nodo(this.frente.getElem(),null);
            //1er nodo (del frente) copiado
            //nodoClon ->[A']
            clon.frente=nodoClon;
            // clon.frente → [A']
            //                ^
            //             nodoClon

            while(nodoActual!=null){//mientras el siguiente no sea vacio
                Nodo nodoNuevo= new Nodo(nodoActual.getElem(), null);
                // clon.frente → [A'] [nodoNuevo]
                //                ^
                //             nodoClon
                
                nodoClon.setEnlace(nodoNuevo);
                // clon.frente → [A'] ->[nodoNuevo]
                //                ^
                //             nodoClon
                nodoClon=nodoNuevo;
                // clon.frente → [A'] ->[nodoNuevo]
                //                           ^
                //                        nodoClon
                nodoActual=nodoActual.getEnlace();
            }
            clon.fin=nodoClon;
        }
        return clon;
    }
    public String toString(){
        String texto="[]";
        if(this.frente!=null){
            Nodo aux=this.frente;
            texto="[";
            while(aux!=null){
                texto += (aux.getElem() != null ? aux.getElem().toString() : "null");
                aux=aux.getEnlace();
                if(aux!=null){
                    texto+=",";
                }
            }
            texto+="]";
        }
        return texto;
    }
}