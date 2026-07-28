package Sistema;

import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Lista.Lista;
import java.util.HashMap;
import Sistema.Grafo.Grafo;
import Sistema.EscapeHouse;

public class ethan {
    //ejercicio 1
    public String mostrarInfoEquipo(String nombre) {
        Equipo equipo = equipos.get(nombre);
        String data="";

        if (equipo != null) {
            data=equipo.toString();
        } else {
            data="No existe un equipo con ese nombre.";
        }
        return data;
    }

    //ejercicio 2
    public String posiblesDesafios(Equipo equipo, Habitacion hab){
        String data="";
        if(equipo != null){
            Habitacion habEquipoParado=(Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());
            boolean adyacente=mapa.existeArcoDirecto(habEquipoParado,hab);
            if(adyacente){
                int diferencia=mapa.ObtenerEtiqueta(habEquipoParado,hab)-equipo.getPuntajeHabitacion();
                if(diferencia<=0){
                    data="El equipo puede pasar a la habitacion, no necesita completar ningun desafio";
                }else{
                    HashMap<Integer, Lista> des = desafiosResueltos.get(equipo.getNombre());
                    Lista aux = null;
                    if(des != null){
                        aux = des.get(equipo.getCodigoHabitacion());
                    }
                    Lista l=eliminarDesafiosRealizados(habEquipoParado,aux);
                    Lista filtrados=filtrarDesafiosPorPuntaje(l,diferencia);
                    if(!filtrados.esVacia()){
                        data="Los desafios posibles son: "+filtrados.toString();
                    }else{
                        data="Error, no existe un desafio posible para pasar de habitacion";
                    }
                }
            }else{
                data="La habitacion no es adyacente al equipo";
            }
        }
        return data;
    }

    private Lista eliminarDesafiosRealizados(Habitacion habActual, Lista desafiosResueltos) {
        Lista disponibles = habActual.getDesafios().listarInorden();
        if (desafiosResueltos != null && !desafiosResueltos.esVacia()) {
            int i = 1;
            int longitud=desafiosResueltos.longitud();
            while (i <= longitud) {
                Desafio resuelto = (Desafio) desafiosResueltos.recuperar(i);
                int j = 1;
                boolean encontrado = false;
                while (j <= disponibles.longitud() && !encontrado) {//necesario preguntar la longitud
                                                                    //en cada iteracion.
                    Desafio actual = (Desafio) disponibles.recuperar(j);
                    if(actual.getPuntaje() == resuelto.getPuntaje()) {
                        disponibles.eliminar(j);
                        encontrado = true;
                    } else {
                        j++;
                    }
                }
                i++;
            }
        }    
        return disponibles;
    }

    private Lista filtrarDesafiosPorPuntaje(Lista l, int diferencia){
        //destruye la lista para eficiencia, cuidado
        Lista filtrados=new Lista();
        while(!l.esVacia()){
            Desafio desafioActual=(Desafio) l.recuperar(1);
            if(desafioActual.getPuntaje()>=diferencia){
                filtrados.insertar(desafioActual, 1);
            }
            l.eliminar(1);
        }
        return filtrados;
    }

    //ejercicio 3
    //ejercicio considerando que entra por parametro la habitacion
    /*  
    public boolean jugarDesafio(Equipo equipo, Habitacion laHabitacion, Desafio elDesafio) {
        boolean exito = revisarCondiciones(equipo, laHabitacion, elDesafio);

        if (exito) {
            equipo.setPuntajeHabitacion(equipo.getPuntajeHabitacion() + elDesafio.getPuntaje());
            equipo.setPuntajeAcumulado(equipo.getPuntajeAcumulado() + elDesafio.getPuntaje());

            HashMap<Integer, Lista> aux = desafiosResueltos.get(equipo.getNombre());

            if (aux == null) {
                aux = new HashMap<>();
                desafiosResueltos.put(equipo.getNombre(), aux);
            }

            Lista listaDesafios = aux.get(laHabitacion.getCodigo());

            if (listaDesafios == null) {
                listaDesafios = new Lista();
                aux.put(laHabitacion.getCodigo(), listaDesafios);
            }

            listaDesafios.insertar(elDesafio, listaDesafios.longitud() + 1);
        }

        return exito;
    }
    private boolean revisarCondiciones(Equipo equipo, Habitacion laHabitacion, Desafio elDesafio) {
        boolean exito = validarParametros(equipo, laHabitacion, elDesafio);

        if (exito) {
            Habitacion hab = (Habitacion) casona.obtenerInformacion(laHabitacion.getCodigo());

            exito = hab != null
                    && equipo.getCodigoHabitacion() == laHabitacion.getCodigo()
                    && hab.getDesafios().contiene(elDesafio.getPuntaje())
                    && desafioNoResuelto(equipo, elDesafio);
        }

        return exito;
    }
        private boolean validarParametros(Equipo equipo, Habitacion laHabitacion, Desafio elDesafio) {
        return equipo != null
                && laHabitacion != null
                && elDesafio != null;
    }
     */
    //ejercicio considerando que no es necesaria la habitacion por parametro
    public boolean jugarDesafio(Equipo equipo, Desafio elDesafio) {//ejercicio pide habitacion pero no es necesaria(?)
        boolean exito = revisarCondiciones(equipo, elDesafio);
        if (exito) {
            equipo.setPuntajeHabitacion(equipo.getPuntajeHabitacion() + elDesafio.getPuntaje());
            equipo.setPuntajeAcumulado(equipo.getPuntajeAcumulado() + elDesafio.getPuntaje());
            HashMap<Integer, Lista> aux = desafiosResueltos.get(equipo.getNombre());
            if (aux == null) {
                aux = new HashMap<>();
                desafiosResueltos.put(equipo.getNombre(), aux);
            }

            Habitacion laHabitacion = (Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());
            Lista listaDesafios = aux.get(laHabitacion.getCodigo());

            if (listaDesafios == null) {
                listaDesafios = new Lista();
                aux.put(laHabitacion.getCodigo(), listaDesafios);
            }

            listaDesafios.insertar(elDesafio, listaDesafios.longitud() + 1);
        }

        return exito;
    }

    private boolean revisarCondiciones(Equipo equipo, Desafio elDesafio) {
        boolean exito = validarParametros(equipo, elDesafio);
        if (exito) {
            Habitacion hab =(Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());
            exito = hab != null
                    && hab.getDesafios().contiene(elDesafio.getPuntaje())
                    && desafioNoResuelto(equipo, elDesafio);
        }
        return exito;
    }

    private boolean validarParametros(Equipo equipo, Desafio elDesafio) {
        return equipo != null
                && elDesafio != null;
    }

    private boolean desafioNoResuelto(Equipo equipo, Desafio elDesafio) {
        boolean exito = true;
        HashMap<Integer, Lista> aux = desafiosResueltos.get(equipo.getNombre());
        if (aux != null) {

            Lista lista = aux.get(equipo.getCodigoHabitacion());
            if (lista != null) {
                int i = 1;
                int longi=lista.longitud();
                while (i <= longi && exito) {
                    Desafio d = (Desafio) lista.recuperar(i);

                    if (d.getPuntaje() == elDesafio.getPuntaje()) {
                        exito = false;
                    }
                    i++;
                }
            }
        }
        return exito;
    }
    //ejericio 4
    public boolean cambiarDeHabitacion(Equipo equipo, Habitacion habAPasar){
        boolean exito=verificarCondicionesCambio(equipo,habAPasar);
        if(exito){
            equipo.setPuntajeHabitacion(0);
            equipo.setCodigoHabitacion(habAPasar.getCodigo());
        }
        return exito;
    }
    private boolean verificarCondicionesCambio(Equipo equipo, Habitacion habAPasar){
        boolean exito = false;
        if (equipo != null && habAPasar != null) {
            Habitacion habActual =
                (Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());
            if (habActual != null) {
                exito = mapa.existeArcoDirecto(habActual, habAPasar)
                        && equipo.getPuntajeHabitacion() >=
                        mapa.ObtenerEtiqueta(habActual, habAPasar);
            }
        }
        return exito;
    }
    public boolean puedeSalir(String nombreEquipo) {
        boolean exito = false;
        Equipo equipo = equipos.get(nombreEquipo);
        if (equipo != null) {
            Habitacion habActual =
                    (Habitacion) casona.obtenerInformacion(equipo.getCodigoHabitacion());
            if (habActual != null) {
                exito = habActual.isTieneSalida()
                        && equipo.getPuntajeAcumulado() >= equipo.getPuntajeExigido();
            }
        }
        return exito;
    }
}
