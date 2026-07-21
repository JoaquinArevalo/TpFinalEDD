package Sistema;
import Sistema.Diccionario.DiccionarioAVL;
import Sistema.Grafo.Grafo;
import Sistema.Lista.Lista;
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

        if (hab == null) {
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
        //Si no es nulo, ni es primera puerta , ni salida  entonces elimina la habitacion
        if (hab != null && codigo != 1 && !hab.isTieneSalida()){
            exito=true;
            avlHabitaciones.eliminar(codigo);
            grafoCasona.eliminarVertice(codigo);
        }  
        return exito;
    }

    public boolean modificarHabitacion(int codigo, String nuevoNombre, int nuevaPlanta, double nuevosMetros) {
        boolean exito = false;
        Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codigo);
        if(hab !=null){
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
        if (hab == null || hab.getDesafios().obtenerInformacion(puntaje) == null ) {
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
            boolean encontrado = hab.getDesafios().eliminar(puntaje);
            //si existe un desafio con ese puntaje en esa habitacion
            if (encontrado){
                exito = true;
            }
        }

        return exito;
    }

    public boolean modificarDesafio(int codHabitacion, int puntajeOriginal, String nuevoNombre, String nuevoTipo) {
        boolean exito = false;
        Habitacion hab = (Habitacion) avlHabitaciones.obtenerInformacion(codHabitacion);
        if (hab != null){
            //buscamos que exista el desafio con ese puntaje 
            Desafio des = (Desafio) hab.getDesafios().obtenerInformacion(puntajeOriginal);
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
        // .get  devuelve si existe ese objeto en la tabla
        if (hashEquipos.get(nombre) == null) {
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

    public boolean modificarEquipo(String nombreOriginal, int puntajeExigido, int puntajeAcum, int codHab, int puntajeHab) {
        Boolean exito = false;
        Equipo equipoActual = hashEquipos.get(nombreOriginal);
        if (equipoActual != null) {
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
