package Sistema;

import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Lista.Lista;

public class ethan {
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
    public String posiblesDesafios(Equipo equipo, Habitacion hab){
        Habitacion habEquipoParado=casona.obtenerInformacion(equipo.getCodigoHabitacion());
        boolean adyacente=existeCamino(habEquipoParado,hab);
        String data="";
        if(adyacente){
            int diferencia=ObtenerEtiqueta(habEquipoParado,hab)-equipo.getPuntajeAcumulado();
            if(diferencia<=0){
                data="El equipo puede pasar a la habitacion, no necesita completar ningun desafio";
            }else{
                HashMap<Integer, Lista> des = desafiosResueltos.get(equipo.getNombre());
                Lista aux=des.get(equipo.getCodigoHabitacion());
                Lista l=eliminarDesafiosRealizados(habEquipoParado,aux);
                l=filtrarDesafiosNecesarios(l,diferencia);
                if(l!=null){
                    data="Los desafios posibles son: "+l.toString();
                }else{
                    data="Error, no existe un desafio posible para pasar de habitacion";
                }
            }
        }else{
            data="La habitacion no es adyacente al equipo";
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
    private Lista filtrarDesafiosNecesarios(Lista l, int diferencia){
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
}
