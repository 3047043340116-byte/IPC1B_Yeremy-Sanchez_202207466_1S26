package quetzalspacedefender;

public class Nave {

    private final String tipo;
    private final int velocidad;
    private final int tiempoDisparo;

    public Nave(String tipo) {

        this.tipo = tipo;

        switch (tipo) {

            case "Explorador":
                // Dificultad Fácil
                velocidad = 15;
                tiempoDisparo = 2000;
                break;

            case "Caza Estelar":
                // Dificultad Normal
                velocidad = 10;
                tiempoDisparo = 1000;
                break;

            case "Acorazado":
                // Dificultad Difícil
                velocidad = 5;
                tiempoDisparo = 300;
                break;

            default:
                // Valor por defecto
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

    public String getDificultad() {

        switch (tipo) {

            case "Explorador":
                return "Fácil";

            case "Caza Estelar":
                return "Normal";

            case "Acorazado":
                return "Difícil";

            default:
                return "Normal";
        }
    }
}