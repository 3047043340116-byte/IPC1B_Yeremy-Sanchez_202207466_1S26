package quetzalspacedefender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DatosJuego {

    private static final String ARCHIVO_PILOTOS = "pilotos.txt";
    private static final String ARCHIVO_PARTIDAS = "partidas.txt";

    private static final Piloto[] pilotos = new Piloto[100];
    private static final Partida[] partidas = new Partida[500];

    private static int cantidadPilotos = 0;
    private static int cantidadPartidas = 0;

    public static void cargarDatos() {
        cargarPilotos();
        cargarPartidas();
    }

    public static boolean agregarPiloto(Piloto piloto) {

        if (piloto == null
                || cantidadPilotos >= pilotos.length
                || buscarPiloto(piloto.getNombre()) != null) {
            return false;
        }

        pilotos[cantidadPilotos] = piloto;
        cantidadPilotos++;

        guardarPilotos();

        return true;
    }

    public static Piloto buscarPiloto(String nombre) {

        if (nombre == null) {
            return null;
        }

        for (int i = 0; i < cantidadPilotos; i++) {

            if (pilotos[i].getNombre().equalsIgnoreCase(nombre)) {
                return pilotos[i];
            }
        }

        return null;
    }

    public static Piloto[] getPilotos() {

        Piloto[] resultado = new Piloto[cantidadPilotos];

        for (int i = 0; i < cantidadPilotos; i++) {
            resultado[i] = pilotos[i];
        }

        return resultado;
    }

    public static void guardarCambiosPilotos() {
        guardarPilotos();
    }

    private static void guardarPilotos() {

        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter(ARCHIVO_PILOTOS))) {

            for (int i = 0; i < cantidadPilotos; i++) {

                escritor.write(
                        pilotos[i].getNombre()
                        + "|"
                        + pilotos[i].getNave()
                        + "|"
                        + pilotos[i].getPuntaje()
                );

                escritor.newLine();
            }

        } catch (IOException e) {
            System.out.println(
                    "Error al guardar pilotos: " + e.getMessage()
            );
        }
    }

    private static void cargarPilotos() {

        File archivo = new File(ARCHIVO_PILOTOS);

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = lector.readLine()) != null
                    && cantidadPilotos < pilotos.length) {

                String[] datos = linea.split("\\|");

                if (datos.length >= 3) {

                    Piloto piloto =
                            new Piloto(datos[0], datos[1]);

                    try {
                        piloto.setPuntaje(
                                Integer.parseInt(datos[2])
                        );
                    } catch (NumberFormatException e) {
                        piloto.setPuntaje(0);
                    }

                    pilotos[cantidadPilotos] = piloto;
                    cantidadPilotos++;
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Error al cargar pilotos: " + e.getMessage()
            );
        }
    }

    public static void registrarPartida(Partida partida) {

        if (partida == null || cantidadPartidas >= partidas.length) {
            return;
        }

        partidas[cantidadPartidas] = partida;
        cantidadPartidas++;

        guardarPartidas();
    }

    public static Partida[] getPartidas() {

        Partida[] resultado = new Partida[cantidadPartidas];

        for (int i = 0; i < cantidadPartidas; i++) {
            resultado[i] = partidas[i];
        }

        return resultado;
    }

    private static void guardarPartidas() {

        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter(ARCHIVO_PARTIDAS))) {

            for (int i = 0; i < cantidadPartidas; i++) {

                Partida partida = partidas[i];

                escritor.write(
                        partida.getPiloto()
                        + "|"
                        + partida.getNave()
                        + "|"
                        + partida.getPuntaje()
                        + "|"
                        + partida.getResultado()
                        + "|"
                        + partida.getFecha()
                );

                escritor.newLine();
            }

        } catch (IOException e) {
            System.out.println(
                    "Error al guardar partidas: " + e.getMessage()
            );
        }
    }

    private static void cargarPartidas() {

        File archivo = new File(ARCHIVO_PARTIDAS);

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = lector.readLine()) != null
                    && cantidadPartidas < partidas.length) {

                String[] datos = linea.split("\\|");

                if (datos.length >= 5) {

                    int puntaje;

                    try {
                        puntaje = Integer.parseInt(datos[2]);
                    } catch (NumberFormatException e) {
                        puntaje = 0;
                    }

                    partidas[cantidadPartidas] =
                            new Partida(
                                    datos[0],
                                    datos[1],
                                    puntaje,
                                    datos[3],
                                    datos[4]
                            );

                    cantidadPartidas++;
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Error al cargar partidas: " + e.getMessage()
            );
        }
    }
}
