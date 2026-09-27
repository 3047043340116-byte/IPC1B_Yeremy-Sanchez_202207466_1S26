package quetzalspacedefender;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class Reporte {

    public static void generarReporte() {

        Partida[] partidas =
                DatosJuego.getPartidas();

        Piloto[] pilotos =
                DatosJuego.getPilotos();

        String nombreArchivo =
                "reporte_quetzal.html";

        // Ordenar pilotos para obtener el Top.
        Piloto[] ordenados =
                Arrays.copyOf(
                        pilotos,
                        pilotos.length
                );

        Arrays.sort(
                ordenados,
                Comparator.comparingInt(
                        Piloto::getPuntaje
                ).reversed()
        );

        try (
                BufferedWriter escritor =
                        new BufferedWriter(
                                new FileWriter(
                                        nombreArchivo
                                )
                        )
        ) {

            // ==========================================
            // INICIO DEL DOCUMENTO
            // ==========================================

            escritor.write(
                    "<!DOCTYPE html>"
            );

            escritor.newLine();

            escritor.write(
                    "<html lang='es'>"
            );

            escritor.newLine();

            escritor.write(
                    "<head>"
            );

            escritor.newLine();

            escritor.write(
                    "<meta charset='UTF-8'>"
            );

            escritor.newLine();

            escritor.write(
                    "<title>"
                    + "Quetzal Space Defender"
                    + "</title>"
            );

            escritor.newLine();

            // ==========================================
            // ESTILOS
            // ==========================================

            escritor.write(
                    "<style>"
                    + "body{"
                    + "font-family:Arial;"
                    + "margin:40px;"
                    + "background:#f4f6f7;"
                    + "color:#222;"
                    + "}"
                    + "h1{"
                    + "color:#1b4f72;"
                    + "}"
                    + "h2{"
                    + "color:#2874a6;"
                    + "margin-top:35px;"
                    + "}"
                    + "table{"
                    + "border-collapse:collapse;"
                    + "width:100%;"
                    + "background:white;"
                    + "}"
                    + "th,td{"
                    + "border:1px solid #999;"
                    + "padding:8px;"
                    + "text-align:left;"
                    + "}"
                    + "th{"
                    + "background:#1b4f72;"
                    + "color:white;"
                    + "}"
                    + "tr:nth-child(even){"
                    + "background:#eaf2f8;"
                    + "}"
                    + ".contenedor-grafica{"
                    + "text-align:center;"
                    + "margin-top:20px;"
                    + "}"
                    + ".grafica{"
                    + "max-width:800px;"
                    + "width:100%;"
                    + "}"
                    + "</style>"
            );

            escritor.newLine();

            escritor.write(
                    "</head>"
            );

            escritor.newLine();

            escritor.write(
                    "<body>"
            );

            escritor.newLine();

            // ==========================================
            // TITULO
            // ==========================================

            escritor.write(
                    "<h1>"
                    + "Quetzal Space Defender"
                    + "</h1>"
            );

            escritor.newLine();

            String fecha =
                    new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss"
                    ).format(
                            new Date()
                    );

            escritor.write(
                    "<p>"
                    + "<strong>Reporte generado:</strong> "
                    + fecha
                    + "</p>"
            );

            escritor.newLine();

            // ==========================================
            // RESUMEN
            // ==========================================

            escritor.write(
                    "<h2>Resumen</h2>"
            );

            escritor.newLine();

            escritor.write(
                    "<p>"
                    + "Cantidad de pilotos registrados: "
                    + pilotos.length
                    + "</p>"
            );

            escritor.newLine();

            escritor.write(
                    "<p>"
                    + "Cantidad de partidas registradas: "
                    + partidas.length
                    + "</p>"
            );

            escritor.newLine();

            // ==========================================
            // TOP DE PUNTAJES
            // ==========================================

            escritor.write(
                    "<h2>Top de Puntajes</h2>"
            );

            escritor.newLine();

            escritor.write(
                    "<table>"
            );

            escritor.newLine();

            escritor.write(
                    "<tr>"
                    + "<th>Posición</th>"
                    + "<th>Piloto</th>"
                    + "<th>Nave</th>"
                    + "<th>Puntaje</th>"
                    + "</tr>"
            );

            escritor.newLine();

            int limite =
                    Math.min(
                            ordenados.length,
                            10
                    );

            for (int i = 0; i < limite; i++) {

                Piloto piloto =
                        ordenados[i];

                escritor.write(
                        "<tr>"
                        + "<td>"
                        + (i + 1)
                        + "</td>"
                        + "<td>"
                        + piloto.getNombre()
                        + "</td>"
                        + "<td>"
                        + piloto.getNave()
                        + "</td>"
                        + "<td>"
                        + piloto.getPuntaje()
                        + "</td>"
                        + "</tr>"
                );

                escritor.newLine();
            }

            escritor.write(
                    "</table>"
            );

            escritor.newLine();

            // ==========================================
            // GRAFICA
            // ==========================================

            escritor.write(
                    "<h2>Gráfica de Puntajes</h2>"
            );

            escritor.newLine();

            if (pilotos.length > 0) {

                // Generar la gráfica antes de
                // colocarla en el reporte.
                GraficaPuntajes.guardarGrafica();

                escritor.write(
                        "<div "
                        + "class='contenedor-grafica'>"
                );

                escritor.newLine();

                escritor.write(
                        "<img "
                        + "src='grafica_puntajes.png' "
                        + "alt='Gráfica de puntajes' "
                        + "class='grafica'>"
                );

                escritor.newLine();

                escritor.write(
                        "</div>"
                );

                escritor.newLine();

            } else {

                escritor.write(
                        "<p>"
                        + "No existen pilotos para "
                        + "generar la gráfica."
                        + "</p>"
                );

                escritor.newLine();
            }

            // ==========================================
            // HISTORIAL
            // ==========================================

            escritor.write(
                    "<h2>Historial de Partidas</h2>"
            );

            escritor.newLine();

            if (partidas.length == 0) {

                escritor.write(
                        "<p>"
                        + "Todavía no existen partidas "
                        + "registradas."
                        + "</p>"
                );

                escritor.newLine();

            } else {

                escritor.write(
                        "<table>"
                );

                escritor.newLine();

                escritor.write(
                        "<tr>"
                        + "<th>Fecha</th>"
                        + "<th>Piloto</th>"
                        + "<th>Nave</th>"
                        + "<th>Puntaje</th>"
                        + "<th>Resultado</th>"
                        + "</tr>"
                );

                escritor.newLine();

                for (Partida partida :
                        partidas) {

                    escritor.write(
                            "<tr>"
                            + "<td>"
                            + partida.getFecha()
                            + "</td>"
                            + "<td>"
                            + partida.getPiloto()
                            + "</td>"
                            + "<td>"
                            + partida.getNave()
                            + "</td>"
                            + "<td>"
                            + partida.getPuntaje()
                            + "</td>"
                            + "<td>"
                            + partida.getResultado()
                            + "</td>"
                            + "</tr>"
                    );

                    escritor.newLine();
                }

                escritor.write(
                        "</table>"
                );

                escritor.newLine();
            }

            // ==========================================
            // PIE DEL REPORTE
            // ==========================================

            escritor.write(
                    "<hr>"
            );

            escritor.newLine();

            escritor.write(
                    "<p>"
                    + "Quetzal Space Defender - "
                    + "Práctica 2"
                    + "</p>"
            );

            escritor.newLine();

            escritor.write(
                    "</body>"
            );

            escritor.newLine();

            escritor.write(
                    "</html>"
            );

            escritor.newLine();

            System.out.println(
                    "Reporte generado: "
                    + nombreArchivo
            );

        } catch (IOException e) {

            System.out.println(
                    "Error generando reporte: "
                    + e.getMessage()
            );
        }
    }

    private static void generarImagenGrafica() {

        // Esta función utiliza la misma clase
        // de gráfica que utiliza el menú.

        try {

            GraficaPuntajes.mostrarGrafica();

        } catch (Exception e) {

            System.out.println(
                    "No se pudo generar la gráfica: "
                    + e.getMessage()
            );
        }
    }
}