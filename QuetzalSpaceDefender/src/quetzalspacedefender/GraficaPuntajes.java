package quetzalspacedefender;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraficaPuntajes {

    public static void mostrarGrafica() {

        Piloto[] pilotos = DatosJuego.getPilotos();

        DefaultCategoryDataset datos =
                new DefaultCategoryDataset();

        for (Piloto piloto : pilotos) {

            datos.addValue(
                    piloto.getPuntaje(),
                    "Puntaje",
                    piloto.getNombre()
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
                new JFrame("Gráfica de Puntajes");

        ChartPanel panel =
                new ChartPanel(grafica);

        panel.setPreferredSize(
                new Dimension(800, 500)
        );

        ventana.add(panel);
        ventana.pack();
        ventana.setLocationRelativeTo(null);

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setVisible(true);

        try {

            ChartUtils.saveChartAsPNG(
                    new File("grafica_puntajes.png"),
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
}
