package quetzalspacedefender;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraficaPuntajes {

    public static void mostrarGrafica() {

        Piloto[] pilotos =
                DatosJuego.getPilotos();

        if (pilotos.length == 0) {

            JOptionPane.showMessageDialog(
                    null,
                    "No hay pilotos registrados.",
                    "Gráfica de Puntajes",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // Crear una copia para no modificar
        // el orden original de los pilotos.
        Piloto[] ordenados =
                Arrays.copyOf(
                        pilotos,
                        pilotos.length
                );

        // Ordenar de mayor a menor puntaje.
        Arrays.sort(
                ordenados,
                Comparator.comparingInt(
                        Piloto::getPuntaje
                ).reversed()
        );

        DefaultCategoryDataset datos =
                new DefaultCategoryDataset();

        // Mostrar máximo 10 pilotos.
        int limite =
                Math.min(
                        ordenados.length,
                        10
                );

        for (int i = 0; i < limite; i++) {

            datos.addValue(
                    ordenados[i].getPuntaje(),
                    "Puntaje",
                    ordenados[i].getNombre()
            );
        }

        JFreeChart grafica =
                ChartFactory.createBarChart(
                        "Top de Puntajes",
                        "Piloto",
                        "Puntos",
                        datos
                );

        JFrame ventana =
                new JFrame(
                        "Gráfica de Puntajes"
                );

        ChartPanel panel =
                new ChartPanel(grafica);

        panel.setPreferredSize(
                new Dimension(
                        800,
                        500
                )
        );

        ventana.add(panel);

        ventana.pack();

        ventana.setLocationRelativeTo(
                null
        );

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setVisible(true);

        // Guardar la gráfica como PNG
        try {

            ChartUtils.saveChartAsPNG(
                    new File(
                            "grafica_puntajes.png"
                    ),
                    grafica,
                    800,
                    500
            );

        } catch (IOException e) {

            System.out.println(
                    "No se pudo guardar la gráfica: "
                    + e.getMessage()
            );
        }
    }

public static void guardarGrafica() {

    Piloto[] pilotos =
            DatosJuego.getPilotos();

    if (pilotos.length == 0) {
        return;
    }

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

    DefaultCategoryDataset datos =
            new DefaultCategoryDataset();

    int limite =
            Math.min(
                    ordenados.length,
                    10
            );

    for (int i = 0; i < limite; i++) {

        datos.addValue(
                ordenados[i].getPuntaje(),
                "Puntaje",
                ordenados[i].getNombre()
        );
    }

    JFreeChart grafica =
            ChartFactory.createBarChart(
                    "Top de Puntajes",
                    "Piloto",
                    "Puntos",
                    datos
            );

    try {

        ChartUtils.saveChartAsPNG(
                new File(
                        "grafica_puntajes.png"
                ),
                grafica,
                800,
                500
        );

    } catch (IOException e) {

        System.out.println(
                "Error guardando gráfica: "
                + e.getMessage()
        );
    }
}

}