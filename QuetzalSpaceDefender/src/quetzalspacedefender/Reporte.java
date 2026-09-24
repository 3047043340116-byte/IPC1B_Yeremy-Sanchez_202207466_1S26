package quetzalspacedefender;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reporte {

    public static void generarReporte() {

        Partida[] partidas = DatosJuego.getPartidas();

        String nombreArchivo = "reporte_quetzal.html";

        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter(nombreArchivo))) {

            escritor.write("<!DOCTYPE html>");
            escritor.newLine();

            escritor.write("<html lang='es'>");
            escritor.newLine();

            escritor.write("<head>");
            escritor.newLine();

            escritor.write("<meta charset='UTF-8'>");
            escritor.newLine();

            escritor.write(
                    "<title>Quetzal Space Defender</title>"
            );
            escritor.newLine();

            escritor.write(
                    "<style>"
                    + "body{font-family:Arial;margin:40px;}"
                    + "h1{color:#1b4f72;}"
                    + "table{border-collapse:collapse;width:100%;}"
                    + "th,td{border:1px solid #999;padding:8px;text-align:left;}"
                    + "th{background:#1b4f72;color:white;}"
                    + "</style>"
            );
            escritor.newLine();

            escritor.write("</head>");
            escritor.newLine();

            escritor.write("<body>");
            escritor.newLine();

            escritor.write(
                    "<h1>Quetzal Space Defender</h1>"
            );
            escritor.newLine();

            String fecha =
                    new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss"
                    ).format(new Date());

            escritor.write(
                    "<p>Reporte generado: "
                    + fecha
                    + "</p>"
            );
            escritor.newLine();

            escritor.write("<h2>Historial de partidas</h2>");
            escritor.newLine();

            escritor.write("<table>");
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

            for (Partida partida : partidas) {

                escritor.write(
                        "<tr>"
                        + "<td>" + partida.getFecha() + "</td>"
                        + "<td>" + partida.getPiloto() + "</td>"
                        + "<td>" + partida.getNave() + "</td>"
                        + "<td>" + partida.getPuntaje() + "</td>"
                        + "<td>" + partida.getResultado() + "</td>"
                        + "</tr>"
                );

                escritor.newLine();
            }

            escritor.write("</table>");
            escritor.newLine();

            escritor.write("</body>");
            escritor.newLine();

            escritor.write("</html>");
            escritor.newLine();

            System.out.println(
                    "Reporte generado: " + nombreArchivo
            );

        } catch (IOException e) {

            System.out.println(
                    "Error generando reporte: "
                    + e.getMessage()
            );
        }
    }
}
