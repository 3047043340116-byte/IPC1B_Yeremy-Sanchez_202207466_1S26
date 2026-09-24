package quetzalspacedefender;

public class Nave {

    private final String tipo;
    private final int velocidad;
    private final int tiempoDisparo;

    public Nave(String tipo) {

        this.tipo = tipo;

        switch (tipo) {

            case "Explorador":
                velocidad = 15;
                tiempoDisparo = 2000;
                break;

            case "Caza Estelar":
                velocidad = 10;
                tiempoDisparo = 1000;
                break;

            case "Acorazado":
                velocidad = 5;
                tiempoDisparo = 300;
                break;

            default:
                velocidad = 10;
                tiempoDisparo = 1000;
        }
    }

    public String getTipo() {
        return tipo;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getTiempoDisparo() {
        return tiempoDisparo;
    }
}
