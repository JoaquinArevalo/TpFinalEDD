package Sistema;
import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Grafo.Grafo;
import java.util.HashMap;
public class parra {

    private DiccionarioAVL avlHabitaciones;
    private Grafo grafoCasona;
    private HashMap<String, Equipo> hashEquipos;
    private HashMap<String, HashMap<Integer, Desafio>> desafiosResueltos;


    //PUNTO 2 : ABM 

    //ABM HABITACIONES

    public boolean altaHabitacion(int codigo, String nombre, int planta, double metros, boolean tieneSalida) {
        boolean exito = false;
        Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codigo);

        if (hab == null && !tieneSalida && codigo != 1) {
            exito=true;
            //Crea habitacion , la insera en el Avl y en grafo
            Habitacion habNueva = new Habitacion(codigo, nombre, planta, metros, tieneSalida);
            avlHabitaciones.insertar(codigo, habNueva); 
            grafoCasona.insertarVertice(codigo);
        }       
        return exito;
    }

    public boolean bajaHabitacion(int codigo) {
        boolean exito = false;
        Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codigo);

        // Si no es nula, ni es primera puerta , ni es salida
        if (hab != null && codigo != 1 && !hab.isTieneSalida() && hab.getDesafios().esVacio()){
            
            // Verificamos si la habitación está ocupada usando for-each 
            boolean habitacionOcupada = false;
            for (Equipo eq : hashEquipos.values()) {
                if (eq.getCodigoHabitacion() == codigo) {
                    habitacionOcupada = true;
                }
            }

            if (!habitacionOcupada) {
                exito = true;
                avlHabitaciones.eliminar(codigo);
                grafoCasona.eliminarVertice(codigo);
            }
        }  
        return exito;
    }

    public boolean modificarHabitacion(int codigo, String nuevoNombre, int nuevaPlanta, double nuevosMetros) {
        boolean exito = false;
        Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codigo);
        if(hab != null && !hab.isTieneSalida() && codigo != 1 ){
            exito=true;
            hab.setNombre(nuevoNombre);
            hab.setPlanta(nuevaPlanta);
            hab.setMetrosCuadrados(nuevosMetros);
        }
        return exito;
    }

    //ABM DESAFIOS

    public boolean altaDesafio(int codHabitacion, int puntaje, String nombre, String tipo) {
        boolean exito = false;
        Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codHabitacion);

        //si nignun desafio del avlDesafios de la habitacion tiene el mismo puntaje
        if (hab != null && hab.buscarDesafio(puntaje) == null) {
            exito=true;
            Desafio nuevo = new Desafio(codHabitacion, puntaje, nombre, tipo);
            hab.getDesafios().insertar(puntaje, nuevo);
        }
        return exito;
    }

    public boolean bajaDesafio(int codHabitacion, int puntaje) {
    boolean exito = false;
    Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codHabitacion);

    if (hab != null) {
        // Borramos el desafío de la habitacion
        boolean encontrado = hab.getDesafios().eliminar(puntaje);
        
        if (encontrado){
            exito = true;
            
            for (HashMap<Integer, Desafio> historialEquipo : desafiosResueltos.values()) {
                
                // Nos fijamos si el equipo tiene un desafío resuelto por esa cantidad de puntos
                Desafio desafio = historialEquipo.get(puntaje);
                
                //  si es ese codigo de hab tiene ese puntaje entonces si lo eliminamos
                if (desafio != null && desafio.getCodigoHabitacion() == codHabitacion) {
                    // Ahora sí, estamos 100% seguros de que es el mismo desafío. Lo borramos.
                    historialEquipo.remove(puntaje);
                }
            }
        }
    }

    return exito;
}

    public boolean modificarDesafio(int codHabitacion, int puntajeOriginal, String nuevoNombre, String nuevoTipo) {
        boolean exito = false;
        Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codHabitacion);
        if (hab != null){
            //buscamos que exista el desafio con ese puntaje 
            Desafio des = hab.buscarDesafio(puntajeOriginal);
            if (des != null){;
                exito = true;
                des.setNombre(nuevoNombre);
                des.setTipo(nuevoTipo);
            }
        }
        return exito;
    }

    //ABM EQUIPOS
    public boolean altaEquipo(String nombre, int puntajeExigido, int puntajeAcum, int codHab, int puntajeHab) {
        boolean exito = false;
        Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codHab);
        // .get  devuelve si existe ese objeto en la tabla
        if (hashEquipos.get(nombre) == null && hab!= null && puntajeExigido >= 0 && puntajeAcum >=0 && puntajeHab>=0 ) {
            exito=true;

            Equipo equipoNuevo = new Equipo(nombre, puntajeExigido, puntajeAcum, codHab, puntajeHab);
            // .put inserta o actualiza  un equipo dentro de la tabla 
            hashEquipos.put(nombre, equipoNuevo);
            // Inicializamos la tabla de  desafios resueltos del equipoNuevo en 0
            desafiosResueltos.put(nombre, new HashMap<>());
        }

        return exito;
    }

    public boolean bajaEquipo(String nombre) {
        boolean exito = false;
        // .get  devuelve si existe ese objeto en la tabla
        if (hashEquipos.get(nombre) != null) {
            exito = true;
            // .remove  elimina el objeto de la tabla 
            hashEquipos.remove(nombre);
            desafiosResueltos.remove(nombre); //  borramos su historial de desafios
        }
        return exito;
    }

    public boolean modificarEquipo(String nombre, int puntajeExigido, int puntajeAcum, int codHab, int puntajeHab) {
        Boolean exito = false;
        Equipo equipoActual = hashEquipos.get(nombre);
        Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codHab);

        if (hashEquipos.get(nombre) != null && hab!= null && puntajeExigido >= 0 && puntajeAcum >=0 && puntajeHab>=0 ) {
            exito = true;

            equipoActual.setPuntajeExigido(puntajeExigido);
            equipoActual.setPuntajeAcumulado(puntajeAcum);
            equipoActual.setCodigoHabitacion(codHab);
            equipoActual.setPuntajeHabitacion(puntajeHab);
        }
        return exito;
    }

    //PUNTO 3 : CONSULTAS SOBRE HABITACIONES 

    
}
