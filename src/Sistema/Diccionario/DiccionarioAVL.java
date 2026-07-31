package Sistema.Diccionario;
import Sistema.Lista.Lista;
public class DiccionarioAVL {
    private NodoAVLDicc raiz;

    public DiccionarioAVL() {
        this.raiz = null;
    }

    //CONSULTA DE EXISTENCIA (Verifica si la clave ya está cargada) ---
    public boolean contiene(Comparable claveBuscada) {
        return obtenerInformacionAux(this.raiz, claveBuscada) != null;
    }

    //BTENER INFORMACIÓN (Recupera el objeto completo) ---
    public Object obtenerInformacion(Comparable claveBuscada) {
        return obtenerInformacionAux(this.raiz, claveBuscada);
    }

    private Object obtenerInformacionAux(NodoAVLDicc n, Comparable claveBuscada) {
        Object resultado = null;
        if (n != null) {
            int comparacion = claveBuscada.compareTo(n.getClave());
            if (comparacion == 0) {
                resultado = n.getDato(); 
            } else if (comparacion < 0) {
                resultado = obtenerInformacionAux(n.getIzquierdo(), claveBuscada);
            } else {
                resultado = obtenerInformacionAux(n.getDerecho(), claveBuscada);
            }
        }
        return resultado;
    }

    //INSERCIÓN
    public boolean insertar(Comparable clave, Object dato) {
        boolean[] exito = {true}; 
        this.raiz = insertarAux(this.raiz, clave, dato, exito);
        return exito[0];
    }

    private NodoAVLDicc insertarAux(NodoAVLDicc n, Comparable clave, Object dato, boolean[] exito) {
        if (n == null) {
            n = new NodoAVLDicc(clave, dato);
        } else {
            int comparacion = clave.compareTo(n.getClave());
            if (comparacion == 0) {
                exito[0] = false; // Clave duplicada, no se permite
            } else if (comparacion < 0) {
                n.setIzquierdo(insertarAux(n.getIzquierdo(), clave, dato, exito));
            } else {
                n.setDerecho(insertarAux(n.getDerecho(), clave, dato, exito));
            }
            
            if (exito[0]) {
                n.recalcularAltura();
                n = balancear(n);
            }
        }
        return n;
    }

    // ELIMINACIÓN
    public boolean eliminar(Comparable clave) {
        boolean[] exito = {false};
        this.raiz = eliminarAux(this.raiz, clave, exito);
        return exito[0];
    }

    private NodoAVLDicc eliminarAux(NodoAVLDicc n, Comparable clave, boolean[] exito) {
        if (n != null) {
            int comparacion = clave.compareTo(n.getClave());
            if (comparacion < 0) {
                n.setIzquierdo(eliminarAux(n.getIzquierdo(), clave, exito));
            } else if (comparacion > 0) {
                n.setDerecho(eliminarAux(n.getDerecho(), clave, exito));
            } else {
                // ¡Lo encontramos!
                exito[0] = true;
                
                if (n.getIzquierdo() == null) {
                    n = n.getDerecho();
                } else if (n.getDerecho() == null) {
                    n = n.getIzquierdo(); 
                } else {
                    NodoAVLDicc candidato = buscarMinimo(n.getDerecho());
                    n.setClave(candidato.getClave());
                    n.setDato(candidato.getDato());
                    n.setDerecho(eliminarAux(n.getDerecho(), candidato.getClave(), new boolean[1]));
                }
            }
            
            //Si se borra una hoja, 'n' es null. No calculo su altura.
            if (n != null) {
                n.recalcularAltura();
                n = balancear(n);
            }
        }
        return n;
    }

    private NodoAVLDicc buscarMinimo(NodoAVLDicc n) {
        while (n.getIzquierdo() != null) {
            n = n.getIzquierdo();
        }
        return n;
    }

    // BALANCEO Y ROTACIONES
    private NodoAVLDicc balancear(NodoAVLDicc n) {
        int balance = calcularBalance(n);

        if (balance > 1) {
            if (calcularBalance(n.getIzquierdo()) < 0) {
                n.setIzquierdo(rotacionIzquierda(n.getIzquierdo()));
            }
            n = rotacionDerecha(n);
        }
        else if (balance < -1) {
            if (calcularBalance(n.getDerecho()) > 0) {
                n.setDerecho(rotacionDerecha(n.getDerecho()));
            }
            n = rotacionIzquierda(n);
        }
        return n;
    }

    private int calcularBalance(NodoAVLDicc n) {
        int altIzq = (n.getIzquierdo() != null) ? n.getIzquierdo().getAltura() : -1;
        int altDer = (n.getDerecho() != null) ? n.getDerecho().getAltura() : -1;
        return altIzq - altDer;
    }

    private NodoAVLDicc rotacionIzquierda(NodoAVLDicc r) {
        NodoAVLDicc h = r.getDerecho();
        r.setDerecho(h.getIzquierdo());
        h.setIzquierdo(r);
        r.recalcularAltura();
        h.recalcularAltura();
        return h; 
    }

    private NodoAVLDicc rotacionDerecha(NodoAVLDicc r) {
        NodoAVLDicc h = r.getIzquierdo();
        r.setIzquierdo(h.getDerecho());
        h.setDerecho(r);
        r.recalcularAltura();
        h.recalcularAltura();
        return h;
    }
    public Lista listarRango(Comparable min,Comparable max){
        Lista res = new Lista();
        if(this.raiz != null){
            listarRangoAux(min,max,res,this.raiz);
        }
        return res;
    }
    private void listarRangoAux(Comparable min, Comparable max, Lista lista, NodoAVLDicc aux) {
        if (aux != null) {
            if (aux.getClave().compareTo(min) < 0) {
                listarRangoAux(min, max, lista, aux.getDerecho());
            } else if (aux.getClave().compareTo(max) > 0) {
                listarRangoAux(min, max, lista, aux.getIzquierdo());
            } else {
                listarRangoAux(min, max, lista, aux.getIzquierdo());
                lista.insertar(aux.getDato(), 1);
                listarRangoAux(min, max, lista, aux.getDerecho());
            }
        }
    }

    public String toString() {
        return (this.raiz == null) ? "Diccionario vacío" : generarStringArbol(this.raiz, "");
    }

    private String generarStringArbol(NodoAVLDicc nodo, String prefijo) {
    // Inicializamos el resultado vacío
    String resultado = "";
    // Solo procesamos si el nodo actual no es nulo
    if (nodo != null) {
        // 1. Recorremos primero el hijo derecho (se imprime arriba)
        resultado += generarStringArbol(nodo.getDerecho(), prefijo + "        ");
        // 2. Calculamos el balance actual para mostrarlo
        int altIzq = (nodo.getIzquierdo() != null) ? nodo.getIzquierdo().getAltura() : -1;
        int altDer = (nodo.getDerecho() != null) ? nodo.getDerecho().getAltura() : -1;
        int balance = altIzq - altDer;
        // 3. Imprimimos el nodo actual con su información clave
        resultado += prefijo + "|-- " + nodo.getClave() + " (Alt: " + nodo.getAltura() + ", Bal: " + balance + ")\n";
        // 4. Recorremos el hijo izquierdo (se imprime abajo)
        resultado += generarStringArbol(nodo.getIzquierdo(), prefijo + "        ");
    }
    return resultado;
}
    public Lista listarInorden() {//el mas prolijo para un diccionarioAvl porque quedan ordenados por puntaje
        Lista lis = new Lista();
        listarInordenAux(this.raiz, lis);
        return lis;
    }

    private void listarInordenAux(NodoAVLDicc nodo, Lista lis) {
        if (nodo != null) {
            listarInordenAux(nodo.getIzquierdo(), lis);
            lis.insertar(nodo.getDato(), lis.longitud() + 1);
            listarInordenAux(nodo.getDerecho(), lis);
        }
    }
}
