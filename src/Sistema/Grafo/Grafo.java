package Sistema.Grafo;

import java.util.Objects;

import Sistema.Lista.Lista;

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

    // SOLUCIONADO EL BUG: Ahora el bucle sí se ejecuta correctamente
    private NodoVert[] ubicarVertOrigDestino(Object origen, Object destino) {
        NodoVert[] arr = new NodoVert[2];
        NodoVert aux = this.inicio;
        while (aux != null && (arr[0] == null || arr[1] == null)) {
            if (Objects.equals(aux.getElem(), origen))  arr[0] = aux;
            if (Objects.equals(aux.getElem(), destino)) arr[1] = aux;
            aux = aux.getSigVertice();
        }
        return arr;
    }

    // ─────────────────────────────────────────────
    // eliminarVertice
    // ─────────────────────────────────────────────

    public boolean eliminarVertice(Object elem) {
        boolean valor = false;
        if(this.inicio != null){
            if (this.inicio.getElem().equals(elem)) {
                eliminarTodosApuntadores(this.inicio);
                this.inicio = this.inicio.getSigVertice();
                valor = true;
            } else {
                NodoVert vertAnterior = ubicaVertAnterior(elem);
                if(vertAnterior != null){
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
        if (aux != null){ 
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
        NodoVert[] arr = ubicarVertOrigDestino(origen, destino);
        if (arr[0] == null || arr[1] == null) return false;
        return existeArcoDirecto(arr[0], arr[1]);
    }

    public boolean esVacio() {
        return this.inicio == null;
    }

    // ─────────────────────────────────────────────
    // Métodos de Solución a los Problemas del TP
    // ─────────────────────────────────────────────

    // 1. habitacionesContiguas
    public Lista habitacionesContiguas(Object codigoHab) {
        Lista resultado = new Lista();
        NodoVert vert = ubicaVert(codigoHab);
        if (vert != null) {
            NodoAdy ady = vert.getPrimerAdy();
            while (ady != null) {
                String info = "Habitación contigua: " + ady.getVertice().getElem() 
                            + " | Puntaje requerido: " + ady.getEtiqueta();
                resultado.insertar(info, 1);
                ady = ady.getSigAdyacente();
            }
        }
        return resultado;
    }

    // 2. esPosibleLlegar
    public boolean esPosibleLlegar(Object hab1, Object hab2, int k) {
        NodoVert[] arr = ubicarVertOrigDestino(hab1, hab2);
        boolean posible = false;
        if (arr[0] != null && arr[1] != null) {
            posible = esPosibleLlegarAux(arr[0], arr[1], k, 0, new Lista());
        }
        return posible;
    }

    private boolean esPosibleLlegarAux(NodoVert vert, NodoVert destino, int k, int puntosAcumulados, Lista visitados) {
        boolean encontrado = false;
        if (vert == destino) {
            encontrado = (puntosAcumulados <= k);
        } else if (puntosAcumulados < k) { // Poda por eficiencia si ya nos pasamos
            visitados.insertar(vert, visitados.longitud() + 1);
            NodoAdy ady = vert.getPrimerAdy();
            while (ady != null && !encontrado) {
                NodoVert sig = ady.getVertice();
                if (visitados.localizar(sig) < 0) {
                    encontrado = esPosibleLlegarAux(sig, destino, k, puntosAcumulados + ady.getEtiqueta(), visitados);
                }
                ady = ady.getSigAdyacente();
            }
            visitados.eliminar(visitados.longitud());
        }
        return encontrado;
    }

    // 3. minimoPuntaje 
    public Lista minimoPuntaje(Object hab1, Object hab2) {
        NodoVert[] arr = ubicarVertOrigDestino(hab1, hab2);
        Lista mejorCamino = new Lista();
        if (arr[0] != null && arr[1] != null) {
            int[] minPuntaje = {Integer.MAX_VALUE};
            minimoPuntajeAux(arr[0], arr[1], new Lista(), mejorCamino, new Lista(), 0, minPuntaje);
            if (minPuntaje[0] == Integer.MAX_VALUE) {
                System.out.println("No hay caminos disponibles entre las habitaciones.");
            } else {
                System.out.println("Mínimo puntaje absoluto a acumular: " + minPuntaje[0]);
            }
        }
        return mejorCamino;
    }

    private void minimoPuntajeAux(NodoVert vert, NodoVert destino, Lista caminoActual, Lista mejorCamino, 
                                  Lista visitados, int puntajeActual, int[] minPuntaje) {
        visitados.insertar(vert,  1);
        caminoActual.insertar(vert.getElem(), caminoActual.longitud() + 1);

        if (vert == destino) {
            if (puntajeActual < minPuntaje[0]) {
                minPuntaje[0] = puntajeActual;
                copiarLista(caminoActual, mejorCamino);
            }
        } else if (puntajeActual < minPuntaje[0]) { // Poda crítica de rendimiento
            NodoAdy ady = vert.getPrimerAdy();
            while (ady != null) {
                NodoVert sig = ady.getVertice();
                if (visitados.localizar(sig) < 0) {
                    minimoPuntajeAux(sig, destino, caminoActual, mejorCamino, visitados, puntajeActual + ady.getEtiqueta(), minPuntaje);
                }
                ady = ady.getSigAdyacente();
            }
        }
        visitados.eliminar(visitados.longitud());
        caminoActual.eliminar(caminoActual.longitud());
    }

    // 4. sinPasarPor
    public Lista sinPasarPor(Object hab1, Object hab2, Object hab3, int P) {
        NodoVert[] arr = ubicarVertOrigDestino(hab1, hab2);
        NodoVert evitar = ubicaVert(hab3);
        Lista caminosValidos = new Lista();

        if (arr[0] != null && arr[1] != null && evitar != null) {
            System.out.println("Caminos de " + hab1 + " a " + hab2 + " sin pisar " + hab3 + " (Máx " + P + " pts):");
            sinPasarPorAux(arr[0], arr[1], evitar, P, 0, new Lista(), new Lista(), caminosValidos);
        }
        return caminosValidos;
    }

   private void sinPasarPorAux(NodoVert vert, NodoVert destino, NodoVert evitar, int maxPuntos, 
                                int puntosAcumulados, Lista caminoActual, Lista visitados, Lista todosLosCaminos) {
        
        // Solo procesamos la rama si NO tocamos el nodo prohibido y NO excedemos el puntaje máximo
        if (vert != evitar && puntosAcumulados <= maxPuntos) {
            
            visitados.insertar(vert, 1);
            caminoActual.insertar(vert.getElem(), caminoActual.longitud() + 1);

            if (vert == destino) {
                // Se clona el camino actual exitoso para guardarlo en la lista contenedora
                Lista caminoEncontrado = new Lista();
                copiarLista(caminoActual, caminoEncontrado);
                todosLosCaminos.insertar(caminoEncontrado,  1);
                
                // Mostrar por pantalla directo como pide el enunciado
                System.out.println("-> Ruta válida: " + caminoActual.toString() + " | Puntos totales requeridos: " + puntosAcumulados);
            } else {
                NodoAdy ady = vert.getPrimerAdy();
                while (ady != null) {
                    NodoVert sig = ady.getVertice();
                    if (visitados.localizar(sig) < 0) {
                        sinPasarPorAux(sig, destino, evitar, maxPuntos, puntosAcumulados + ady.getEtiqueta(), caminoActual, visitados, todosLosCaminos);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
            // Paso de Backtracking
            visitados.eliminar(visitados.longitud());
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
            NodoAdy adyOrig   = auxOrig.getPrimerAdy();
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
        String s = "";
        NodoVert auxVert = this.inicio;
        while (auxVert != null) {
            s += auxVert.getElem().toString() + " -> ";
            NodoAdy auxAdy = auxVert.getPrimerAdy();
            if (auxAdy == null) {
                s += "sin adyacentes";
            } else {
                while (auxAdy != null) {
                    s += "(" + auxAdy.getVertice().getElem().toString()
                            + ", " + auxAdy.getEtiqueta() + ")";
                    auxAdy = auxAdy.getSigAdyacente();
                    if (auxAdy != null) s += " - ";
                }
            }
            s += "\n";
            auxVert = auxVert.getSigVertice();
        }
        return s;
    }
}