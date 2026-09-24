package quetzalspacedefender;

public class Partida {

    private final String piloto;
    private final String nave;
    private final int puntaje;
    private final String resultado;
    private final String fecha;

    public Partida(
            String piloto,
            String nave,
            int puntaje,
            String resultado,
            String fecha) {

        this.piloto = piloto;
        this.nave = nave;
        this.puntaje = puntaje;
        this.resultado = resultado;
        this.fecha = fecha;
    }

    public String getPiloto() {
        return piloto;
    }

    public String getNave() {
        return nave;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public String getResultado() {
        return resultado;
    }

    public String getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return fecha
                + " | Piloto: " + piloto
                + " | Nave: " + nave
                + " | Puntos: " + puntaje
                + " | Resultado: " + resultado;
    }
}
