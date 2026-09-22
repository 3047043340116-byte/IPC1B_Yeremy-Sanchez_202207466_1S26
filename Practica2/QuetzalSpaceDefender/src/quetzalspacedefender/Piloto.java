package quetzalspacedefender;

public class Piloto {

    private String nombre;
    private String nave;
    private int puntaje;

    public Piloto(String nombre, String nave) {
        this.nombre = nombre;
        this.nave = nave;
        this.puntaje = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNave() {
        return nave;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setNave(String nave) {
        this.nave = nave;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public void sumarPuntos(int puntos) {
        puntaje += puntos;
    }

    @Override
    public String toString() {
        return nombre + " - " + nave + " - " + puntaje + " puntos";
    }
}