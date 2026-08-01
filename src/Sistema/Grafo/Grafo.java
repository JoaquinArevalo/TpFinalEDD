package Sistema.Grafo;

import java.util.Objects;

import Sistema.Lista.Lista;
import Sistema.Lista.Cola;

public class Grafo {
    private NodoVert inicio;

    public Grafo() {
        this.inicio = null;
    }

    // ─────────────────────────────────────────────
    // insertarVertice
    // ─────────────────────────────────────────────

    public boolean insertarVertice(Object elem) {
        boolean valor = false;
        if (ubicaVert(elem) == null) {
            this.inicio = new NodoVert(elem, this.inicio, null);
            valor = true;
        }
        return valor;
    }

    // ─────────────────────────────────────────────
    // helpers para localizar vértices 
    // ─────────────────────────────────────────────

    private NodoVert ubicaVert(Object elem) {
        NodoVert aux = this.inicio;
        while (aux != null && !aux.getElem().equals(elem)) {
            aux = aux.getSigVertice();
        }
        return aux;
    }

    private NodoVert ubicaVertAnterior(Object elem) {
        NodoVert aux = this.inicio;
        while (aux.getSigVertice() != null
                && !aux.getSigVertice().getElem().equals(elem)) {
            aux = aux.getSigVertice();
        }
        return aux;
    }

    private NodoVert[] ubicarVertOrigDestino(Object origen, Object destino) {
        NodoVert[] arr = new NodoVert[2];
        NodoVert aux = this.inicio;
        while (aux != null && (arr[0] == null || arr[1] == null)) {
            if (aux.getElem().equals(origen))  arr[0] = aux;
            if (aux.getElem().equals(destino)) arr[1] = aux;
            aux = aux.getSigVertice();
        }
        return arr;
    }

    // ─────────────────────────────────────────────
    // eliminarVertice
    // ─────────────────────────────────────────────

    public boolean eliminarVertice(Object elem) {
        boolean valor = false;
        if (this.inicio != null) {
            if (this.inicio.getElem().equals(elem)) {
                eliminarTodosApuntadores(this.inicio);
                this.inicio = this.inicio.getSigVertice();
                valor = true;
            } else {
                NodoVert vertAnterior = ubicaVertAnterior(elem);
                if (vertAnterior != null) {
                    NodoVert vert = vertAnterior.getSigVertice();
                    if (vert != null) {
                        eliminarTodosApuntadores(vert);
                        vertAnterior.setSigVertice(vert.getSigVertice());
                        valor = true;
                    }
                }
            }
        }
        return valor;
    }

    private void eliminarTodosApuntadores(NodoVert vert) {
        NodoAdy aux = vert.getPrimerAdy();
        while (aux != null) {
            quitarArcoDirecto(aux.getVertice(), vert);
            aux = aux.getSigAdyacente();
        }
    }

    // ─────────────────────────────────────────────
    // insertarArco (Grafo NO Dirigido)
    // ─────────────────────────────────────────────

    public boolean insertarArco(Object origen, Object destino, int valorEtiqueta) {
        boolean valor = false;
        if (this.inicio != null) {
            NodoVert[] arr = ubicarVertOrigDestino(origen, destino);
            if (arr[0] != null && arr[1] != null && !existeArcoDirecto(arr[0], arr[1])) {
                if (arr[0] != arr[1]) { 
                    arr[0].setPrimerAdy(new NodoAdy(arr[1], arr[0].getPrimerAdy(), valorEtiqueta));
                    arr[1].setPrimerAdy(new NodoAdy(arr[0], arr[1].getPrimerAdy(), valorEtiqueta));
                    valor = true;
                }
            }
        }
        return valor;
    }

    // ─────────────────────────────────────────────
    // eliminarArco
    // ─────────────────────────────────────────────

    public boolean eliminarArco(Object origen, Object destino) {
        boolean valor = false;
        if (this.inicio != null) {
            NodoVert[] arr = ubicarVertOrigDestino(origen, destino);
            if (arr[0] != null && arr[1] != null && existeArcoDirecto(arr[0], arr[1])) {
                quitarArcoDirecto(arr[0], arr[1]);
                quitarArcoDirecto(arr[1], arr[0]);
                valor = true;
            }
        }
        return valor;
    }

    private void quitarArcoDirecto(NodoVert origen, NodoVert destino) {
        NodoAdy aux = origen.getPrimerAdy();
        if (aux != null) { 
            if (aux.getVertice() == destino) {
                origen.setPrimerAdy(aux.getSigAdyacente());
            } else {
                NodoAdy prev = aux;
                aux = aux.getSigAdyacente();
                while (aux != null && aux.getVertice() != destino) {
                    prev = aux;
                    aux = aux.getSigAdyacente();
                }
                if (aux != null) {
                    prev.setSigAdyacente(aux.getSigAdyacente());
                }
            }
        }
    }

    // ─────────────────────────────────────────────
    // consultas básicas
    // ─────────────────────────────────────────────

    private boolean existeArcoDirecto(NodoVert origen, NodoVert destino) {
        NodoAdy aux = origen.getPrimerAdy();
        while (aux != null && aux.getVertice() != destino) {
            aux = aux.getSigAdyacente();
        }
        return aux != null;
    }

    public boolean existeVertice(Object vertice) {
        return ubicaVert(vertice) != null;
    }

    public boolean existeArco(Object origen, Object destino) {
        boolean exito = false;
        NodoVert[] arr = ubicarVertOrigDestino(origen, destino);
        if (!(arr[0] == null || arr[1] == null)){
            exito = existeArcoDirecto(arr[0], arr[1]);
        }
        return exito;
    }

    public boolean esVacio() {
        return this.inicio == null;
    }

    // ─────────────────────────────────────────────
    // Métodos de Solución a los Problemas del TP
    // ─────────────────────────────────────────────

// 1. obtenerAdyacentes
    public Lista obtenerAdyacentes(Object vertice) {
        Lista resultado = new Lista();
        NodoVert vert = ubicaVert(vertice);
        
        if (vert != null) {
            NodoAdy ady = vert.getPrimerAdy();
            while (ady != null) {
                String info = ady.getVertice().getElem() + " (Costo: " + ady.getEtiqueta() + ")";
                resultado.insertar(info, resultado.longitud() + 1);
                ady = ady.getSigAdyacente();
            }
        }
        return resultado; 
    }

   // 2. esPosibleLlegar
    public boolean esPosibleLlegar(Object origen, Object destino, int limiteCosto) {
        boolean posible = false;
        NodoVert[] arr = ubicarVertOrigDestino(origen, destino);
        
        if (arr[0] != null && arr[1] != null) {
            posible = esPosibleLlegarAux(arr[0], arr[1], limiteCosto, 0, new Lista());
        }
        return posible; 
    }

    private boolean esPosibleLlegarAux(NodoVert vert, NodoVert destino, int limiteCosto, int costoAcumulado, Lista caminoActual) {
        boolean encontrado = false;
        
        if (vert == destino) {
            encontrado = (costoAcumulado <= limiteCosto);
        } else if (costoAcumulado < limiteCosto) {
            caminoActual.insertar(vert.getElem(), caminoActual.longitud() + 1);
            NodoAdy ady = vert.getPrimerAdy();
            
            while (ady != null && !encontrado) {
                NodoVert sig = ady.getVertice();
                // Verifico si ya lo visitamos buscando su elemento en la lista
                if (caminoActual.localizar(sig.getElem()) < 0) {
                    encontrado = esPosibleLlegarAux(sig, destino, limiteCosto, costoAcumulado + (int)ady.getEtiqueta(), caminoActual);
                }
                ady = ady.getSigAdyacente();
            }
    
            caminoActual.eliminar(caminoActual.longitud());
        }
        return encontrado;
    }
// 3. caminoMenorCosto
    public boolean caminoMenorCosto(Object origen, Object destino, Lista mejorCamino, int[] minCosto) {
        boolean exito = false;
        NodoVert[] arr = ubicarVertOrigDestino(origen, destino);
        
        if (arr[0] != null && arr[1] != null) {
            minCosto[0] = Integer.MAX_VALUE; // Inicializo al máximo posible
            caminoMenorCostoAux(arr[0], arr[1], new Lista(), mejorCamino, 0, minCosto);
            
            // Si el costo cambió, es porque se encontro al menos un camino
            if (minCosto[0] != Integer.MAX_VALUE) {
                exito = true;
            }
        }
        return exito; 
    }

    private void caminoMenorCostoAux(NodoVert vert, NodoVert destino, Lista caminoActual, Lista mejorCamino, int costoActual, int[] minCosto) {
        caminoActual.insertar(vert.getElem(), caminoActual.longitud() + 1);

        if (vert == destino) {
            if (costoActual < minCosto[0]) {
                minCosto[0] = costoActual;
                copiarLista(caminoActual, mejorCamino);
            }
        } else if (costoActual < minCosto[0]) {
            NodoAdy ady = vert.getPrimerAdy();
            while (ady != null) {
                NodoVert sig = ady.getVertice();
                if (caminoActual.localizar(sig.getElem()) < 0) {
                    caminoMenorCostoAux(sig, destino, caminoActual, mejorCamino, costoActual + (int)ady.getEtiqueta(), minCosto);
                }
                ady = ady.getSigAdyacente();
            }
        }
        caminoActual.eliminar(caminoActual.longitud());
    }

    // 4. sinPasarPor
    public Lista caminosSinPasarPor(Object origen, Object destino, Object evitar, int limiteCosto) {
        Lista todosLosCaminos = new Lista();
        NodoVert[] arr = ubicarVertOrigDestino(origen, destino);
        NodoVert vertEvitar = ubicaVert(evitar);

        if (arr[0] != null && arr[1] != null && vertEvitar != null) {
            caminosSinPasarPorAux(arr[0], arr[1], vertEvitar, limiteCosto, 0, new Lista(), todosLosCaminos);
        }
        return todosLosCaminos;
    }

    private void caminosSinPasarPorAux(NodoVert vert, NodoVert destino, NodoVert evitar, int limiteCosto,
                                    int costoAcumulado, Lista caminoActual, Lista todosLosCaminos) {
        if (vert != evitar && costoAcumulado <= limiteCosto) {
            caminoActual.insertar(vert.getElem(), caminoActual.longitud() + 1);

            if (vert == destino) {
                Lista caminoValido = new Lista();
                copiarLista(caminoActual, caminoValido);
                todosLosCaminos.insertar(caminoValido, todosLosCaminos.longitud() + 1);
            } else {
                NodoAdy ady = vert.getPrimerAdy();
                while (ady != null) {
                    NodoVert sig = ady.getVertice();

                    if (caminoActual.localizar(sig.getElem()) < 0) {
                        caminosSinPasarPorAux(sig, destino, evitar, limiteCosto, costoAcumulado + (int) ady.getEtiqueta(),caminoActual, todosLosCaminos);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
            caminoActual.eliminar(caminoActual.longitud());
        }
    }

    public boolean existeCamino(Object origen, Object destino) {
        NodoVert[] arr = ubicarVertOrigDestino(origen, destino);
        return (arr[0] == null || arr[1] == null) ? false : existeCaminoAux(arr[0], arr[1], new Lista());
    }

    private boolean existeCaminoAux(NodoVert vert, NodoVert destino, Lista visitados) {
        boolean valor = false;
        if (vert != null) {
            if (vert == destino) {
                valor = true;
            } else {
                visitados.insertar(vert, 1);
                NodoAdy aux = vert.getPrimerAdy();
                while (aux != null && !valor) {
                    NodoVert sig = aux.getVertice();
                    if (visitados.localizar(sig) < 0) {
                        valor = existeCaminoAux(sig, destino, visitados);
                    }
                    aux = aux.getSigAdyacente();
                }
            }
        }
        return valor;
    }

    private void copiarLista(Lista origen, Lista destino) {
        while (destino.longitud() > 0) destino.eliminar(destino.longitud());
        for (int i = 1; i <= origen.longitud(); i++) {
            destino.insertar(origen.recuperar(i), i);
        }
    }

    public Lista listarEnProfundidad() {
        Lista l = new Lista();
        Lista visitados = new Lista();
        NodoVert aux = this.inicio;
        while (aux != null) {
            listarEnProfundidadAux(aux, l, visitados);
            aux = aux.getSigVertice();
        }
        return l;
    }

    private void listarEnProfundidadAux(NodoVert vert, Lista l, Lista visitados) {
        if (vert != null && visitados.localizar(vert) < 0) {
            l.insertar(vert.getElem(), l.longitud() + 1);
            visitados.insertar(vert, visitados.longitud() + 1);
            NodoAdy aux = vert.getPrimerAdy();
            while (aux != null) {
                listarEnProfundidadAux(aux.getVertice(), l, visitados);
                aux = aux.getSigAdyacente();
            }
        }
    }

    public Lista listarEnAnchura() {
        Lista l = new Lista();
        Lista visitados = new Lista();
        Cola c = new Cola();
        NodoVert aux = this.inicio;

        while (aux != null) {
            if (visitados.localizar(aux) < 0) {
                c.poner(aux);
                visitados.insertar(aux, visitados.longitud() + 1);
                while (!c.esVacia()) {
                    NodoVert vert = (NodoVert) c.obtenerFrente();
                    c.sacar();
                    l.insertar(vert.getElem(), l.longitud() + 1);
                    NodoAdy ady = vert.getPrimerAdy();
                    while (ady != null) {
                        NodoVert sig = ady.getVertice();
                        if (visitados.localizar(sig) < 0) {
                            c.poner(sig);
                            visitados.insertar(sig, visitados.longitud() + 1);
                        }
                        ady = ady.getSigAdyacente();
                    }
                }
            }
            aux = aux.getSigVertice();
        }
        return l;
    }

    public Grafo clone() {
        Grafo clon = new Grafo();
        if (this.inicio == null) return clon;

        NodoVert auxOrig = this.inicio;
        NodoVert ultClon = null;
        while (auxOrig != null) {
            NodoVert nuevo = new NodoVert(auxOrig.getElem(), null, null);
            if (clon.inicio == null) clon.inicio = nuevo;
            else                     ultClon.setSigVertice(nuevo);
            ultClon = nuevo;
            auxOrig = auxOrig.getSigVertice();
        }

        auxOrig = this.inicio;
        NodoVert auxClon = clon.inicio;
        while (auxOrig != null) {
            NodoAdy adyOrig    = auxOrig.getPrimerAdy();
            NodoAdy ultAdyClon = null;
            while (adyOrig != null) {
                NodoVert destinoClon = clon.ubicaVert(adyOrig.getVertice().getElem());
                NodoAdy nuevoAdy = new NodoAdy(destinoClon, null, adyOrig.getEtiqueta());
                if (auxClon.getPrimerAdy() == null) auxClon.setPrimerAdy(nuevoAdy);
                else                                ultAdyClon.setSigAdyacente(nuevoAdy);
                ultAdyClon = nuevoAdy;
                adyOrig = adyOrig.getSigAdyacente();
            }
            auxOrig = auxOrig.getSigVertice();
            auxClon = auxClon.getSigVertice();
        }
        return clon;
    }

    @Override
public String toString() {
    if (this.inicio == null) {
        return "El grafo de la casa está vacío.";
    }
    
    String resultado = "";
    NodoVert aux = this.inicio;
    
    // Recorro la lista de vértices
    while (aux != null) {
        resultado += "Habitación: " + aux.getElem().toString() + " -> Puertas hacia: ";
        NodoAdy ady = aux.getPrimerAdy();
        
        if (ady == null) {
            resultado += "Ninguna (Sin salida)";
        } else {
            // Recorro la lista de adyacentes de este vértice
            while (ady != null) {
                resultado += "[" + ady.getVertice().getElem().toString() + " (Exige: " + ady.getEtiqueta() + " pts)] ";
                ady = ady.getSigAdyacente();
            }
        }
        resultado += "\n";
        aux = aux.getSigVertice(); // Pasamos al siguiente vértice del grafo
    }
    return resultado;
}
    public int ObtenerEtiqueta(Object hab, Object habABuscar){
        NodoVert[] arr = ubicarVertOrigDestino(hab, habABuscar);
        int i=-1;
        NodoAdy puertas=arr[0].getPrimerAdy();//y si arr[0] es null? no se puede hacer getPrimerAdy de null
        if( arr[0] != null && arr[1] != null){
            while(i == -1 && puertas!=null){//las puertas no pueden ser negativas
                if(puertas.getVertice()==arr[1]){
                    i=puertas.getEtiqueta();
                }
                puertas=puertas.getSigAdyacente();
            }
        }
        return i;
    }
}