import java.util.*;

class Empleado {
    public int id;
    public String nombre;

    public Empleado(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}

class Ticket {
    private int numero;
    private String descripcion;
    private String tipo; // ← Tipo del problema (ej. "red", "hardware", etc.)
    private Integer responsable;
    private String retroalimentacion;

    public Ticket(int numero, String descripcion, String tipo) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.responsable = null;
        this.retroalimentacion = "";
    }

    public int getNumero() { return numero; }
    public String getDescripcion() { return descripcion; }
    public String getTipo() { return tipo; }

    public Integer getResponsable() { return responsable; }
    public String getRetroalimentacion() { return retroalimentacion; }

    public void setResponsable(int id) { this.responsable = id; }
    public void setRetroalimentacion(String texto) { this.retroalimentacion = texto; }

    @Override
    public String toString() {
        return "Ticket #" + numero +
               "\nTipo: " + tipo +
               "\nDescripcion: " + descripcion +
               "\nResponsable: " + (responsable != null ? responsable : "No resuelto") +
               "\nRetroalimentacion: " + retroalimentacion + "\n";
    }
}

class Tecnico extends Empleado {
    private String especialidad;
    private Tecnico siguiente;

    public Tecnico(int id, String nombre, String especialidad) {
        super(id, nombre);
        this.especialidad = especialidad;
        this.siguiente = null;
    }

    public void setSiguiente(Tecnico siguiente) {
        this.siguiente = siguiente;
    }

    public Ticket arreglar(Ticket ticket) {
        if (ticket.getTipo().equalsIgnoreCase(this.especialidad)) {
            ticket.setResponsable(this.id);
            ticket.setRetroalimentacion("Problema resuelto por " + nombre + " (especialidad: " + especialidad + ")");
            return ticket;
        } else if (siguiente != null) {
            return siguiente.arreglar(ticket);
        } else {
            return null;
        }
    }
}

class SoporteTecnico {
    private Tecnico primerTecnico;
    private List<Ticket> tickets;
    private int contador;

    public SoporteTecnico(Tecnico primerTecnico) {
        this.primerTecnico = primerTecnico;
        this.tickets = new ArrayList<>();
        this.contador = 1;
    }

    public int crearTicket(String descripcion, String tipo) {
        Ticket nuevo = new Ticket(contador++, descripcion, tipo);
        tickets.add(nuevo);
        return nuevo.getNumero();
    }

    public boolean resolver(int idTicket) {
        Ticket ticket = buscarTicket(idTicket);
        if (ticket == null) return false;

        Ticket resuelto = primerTecnico.arreglar(ticket);
        if (resuelto != null) {
            actualizarTicket(resuelto);
            return true;
        }
        return false;
    }

    private Ticket buscarTicket(int id) {
        for (Ticket t : tickets) {
            if (t.getNumero() == id) return t;
        }
        return null;
    }

    private void actualizarTicket(Ticket actualizado) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getNumero() == actualizado.getNumero()) {
                tickets.set(i, actualizado);
                break;
            }
        }
    }

    public void imprimirTickets() {
        for (Ticket t : tickets) {
            System.out.println(t);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Crear tecnicos
        Tecnico t1 = new Tecnico(101, "Carlos", "hardware");
        Tecnico t2 = new Tecnico(102, "Ana", "red");
        Tecnico t3 = new Tecnico(103, "Luis", "software");

        // Enlazar cadena
        t1.setSiguiente(t2);
        t2.setSiguiente(t3);

        // Crear soporte tecnico con la cabeza de la cadena
        SoporteTecnico soporte = new SoporteTecnico(t1);

        // Cliente reporta un ticket
        int idTicket1 = soporte.crearTicket("Falla en el router del segundo piso", "red");
        int idTicket2 = soporte.crearTicket("No enciende la computadora", "hardware");
        int idTicket3 = soporte.crearTicket("Error al abrir el programa", "software");
        int idTicket4 = soporte.crearTicket("Problema desconocido", "seguridad"); // Nadie lo podra resolver

        // Resolver todos los tickets
        soporte.resolver(idTicket1);
        soporte.resolver(idTicket2);
        soporte.resolver(idTicket3);
        soporte.resolver(idTicket4);

        // Mostrar historial
        soporte.imprimirTickets();
    }
}