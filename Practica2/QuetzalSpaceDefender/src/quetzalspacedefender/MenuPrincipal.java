package quetzalspacedefender;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Comparator;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {

        setTitle("Quetzal Space Defender");
        setSize(550, 600);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setResizable(false);
    }

    private void crearInterfaz() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JLabel titulo =
                new JLabel(
                        "QUETZAL SPACE DEFENDER",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        panel.add(titulo, BorderLayout.NORTH);

        JPanel botones =
                new JPanel(
                        new GridLayout(
                                7, 1, 10, 10
                        )
                );

        JButton jugar = new JButton("Jugar");
        JButton crearPiloto = new JButton("Crear Piloto");
        JButton topPuntajes = new JButton("Top de Puntajes");
        JButton historial = new JButton("Historial");
        JButton reporte = new JButton("Generar Reporte");
        JButton grafica = new JButton("Gráfica de Puntajes");
        JButton salir = new JButton("Salir");

        botones.add(jugar);
        botones.add(crearPiloto);
        botones.add(topPuntajes);
        botones.add(historial);
        botones.add(reporte);
        botones.add(grafica);
        botones.add(salir);

        panel.add(botones, BorderLayout.CENTER);

        add(panel);

        jugar.addActionListener(e -> iniciarJuego());
        crearPiloto.addActionListener(e -> crearNuevoPiloto());
        topPuntajes.addActionListener(e -> mostrarTopPuntajes());
        historial.addActionListener(e -> mostrarHistorial());
        reporte.addActionListener(e -> generarReporte());
        grafica.addActionListener(e -> GraficaPuntajes.mostrarGrafica());
        salir.addActionListener(e -> System.exit(0));
    }

    private void crearNuevoPiloto() {

        String nombre =
                JOptionPane.showInputDialog(
                        this,
                        "Ingrese el nombre del piloto:"
                );

        if (nombre == null) {
            return;
        }

        nombre = nombre.trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre no puede estar vacío.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (nombre.contains("|")) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre no puede contener el símbolo |.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (DatosJuego.buscarPiloto(nombre) != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ese piloto ya existe.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String[] opciones = {
            "Explorador",
            "Caza Estelar",
            "Acorazado"
        };

        String nave =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Seleccione el tipo de nave:",
                        "Crear Piloto",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[1]
                );

        if (nave == null) {
            return;
        }

        Piloto nuevo = new Piloto(nombre, nave);

        if (DatosJuego.agregarPiloto(nuevo)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Piloto creado correctamente."
                    + "\n\nNombre: " + nombre
                    + "\nNave: " + nave
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible crear el piloto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void iniciarJuego() {

        Piloto[] pilotos = DatosJuego.getPilotos();

        if (pilotos.length == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes crear un piloto."
            );

            return;
        }

        String[] nombres =
                new String[pilotos.length];

        for (int i = 0; i < pilotos.length; i++) {
            nombres[i] = pilotos[i].getNombre();
        }

        String seleccionado =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Seleccione un piloto:",
                        "Jugar",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        nombres,
                        nombres[0]
                );

        if (seleccionado == null) {
            return;
        }

        Piloto piloto =
                DatosJuego.buscarPiloto(seleccionado);

        if (piloto == null) {
            return;
        }

        JFrame ventanaJuego =
                new JFrame("Quetzal Space Defender");

        Juego juego = new Juego(piloto);

        ventanaJuego.add(juego);
        ventanaJuego.pack();
        ventanaJuego.setLocationRelativeTo(null);

        ventanaJuego.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventanaJuego.setResizable(false);
        ventanaJuego.setVisible(true);

        juego.requestFocusInWindow();
    }

    private void mostrarTopPuntajes() {

        Piloto[] pilotos = DatosJuego.getPilotos();

        if (pilotos.length == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay pilotos registrados."
            );

            return;
        }

        Arrays.sort(
                pilotos,
                Comparator.comparingInt(
                        Piloto::getPuntaje
                ).reversed()
        );

        StringBuilder texto = new StringBuilder();

        texto.append(
                "========== TOP DE PUNTAJES ==========\n\n"
        );

        for (int i = 0; i < pilotos.length; i++) {

            texto.append(i + 1)
                    .append(". ")
                    .append(pilotos[i].getNombre())
                    .append(" - ")
                    .append(pilotos[i].getPuntaje())
                    .append(" puntos\n");
        }

        JOptionPane.showMessageDialog(
                this,
                texto.toString(),
                "Top de Puntajes",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarHistorial() {

        Partida[] partidas = DatosJuego.getPartidas();

        if (partidas.length == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Todavía no hay partidas registradas."
            );

            return;
        }

        StringBuilder texto = new StringBuilder();

        texto.append(
                "========== HISTORIAL ==========\n\n"
        );

        for (Partida partida : partidas) {
            texto.append(partida).append("\n");
        }

        JTextArea area =
                new JTextArea(texto.toString());

        area.setEditable(false);

        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        JScrollPane scroll =
                new JScrollPane(area);

        scroll.setPreferredSize(
                new Dimension(700, 400)
        );

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Historial de Partidas",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void generarReporte() {

        Reporte.generarReporte();

        JOptionPane.showMessageDialog(
                this,
                "Reporte generado correctamente.\n"
                + "Busca el archivo:\n\n"
                + "reporte_quetzal.html"
        );
    }
}
