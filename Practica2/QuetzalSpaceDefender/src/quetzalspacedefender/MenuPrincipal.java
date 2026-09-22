package quetzalspacedefender;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuPrincipal extends JFrame {

    private ArrayList<Piloto> pilotos;

    public MenuPrincipal() {

        pilotos = new ArrayList<>();

        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {

        setTitle("Quetzal Space Defender");
        setSize(500, 450);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);
    }

    private void crearInterfaz() {

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(6, 1, 10, 10));

        JLabel titulo =
                new JLabel(
                        "QUETZAL SPACE DEFENDER",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        JButton jugar =
                new JButton("Jugar");

        JButton crearPiloto =
                new JButton("Crear Piloto");

        JButton topPuntajes =
                new JButton("Top de Puntajes");

        JButton salir =
                new JButton("Salir");

        panel.add(titulo);
        panel.add(jugar);
        panel.add(crearPiloto);
        panel.add(topPuntajes);
        panel.add(salir);

        add(panel);

        jugar.addActionListener(e ->
                iniciarJuego()
        );

        crearPiloto.addActionListener(e ->
                crearNuevoPiloto()
        );

        topPuntajes.addActionListener(e ->
                mostrarTopPuntajes()
        );

        salir.addActionListener(e ->
                System.exit(0)
        );
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
                    "El nombre no puede estar vacío."
            );

            return;
        }

        for (Piloto piloto : pilotos) {

            if (piloto.getNombre()
                    .equalsIgnoreCase(nombre)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ese piloto ya existe."
                );

                return;
            }
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

        Piloto nuevo =
                new Piloto(nombre, nave);

        pilotos.add(nuevo);

        JOptionPane.showMessageDialog(
                this,
                "Piloto creado correctamente."
                + "\n\nNombre: " + nombre
                + "\nNave: " + nave
        );
    }

    private void iniciarJuego() {

        if (pilotos.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes crear un piloto."
            );

            return;
        }

        String[] nombres =
                new String[pilotos.size()];

        for (int i = 0; i < pilotos.size(); i++) {
            nombres[i] =
                    pilotos.get(i).getNombre();
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

        Piloto pilotoSeleccionado = null;

        for (Piloto piloto : pilotos) {

            if (piloto.getNombre()
                    .equals(seleccionado)) {

                pilotoSeleccionado = piloto;
                break;
            }
        }

        if (pilotoSeleccionado == null) {
            return;
        }

        JFrame ventanaJuego =
                new JFrame(
                        "Quetzal Space Defender - Juego"
                );

        Juego juego =
                new Juego(pilotoSeleccionado);

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

        if (pilotos.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Todavía no hay pilotos registrados."
            );

            return;
        }

        pilotos.sort(
                (a, b) ->
                        Integer.compare(
                                b.getPuntaje(),
                                a.getPuntaje()
                        )
        );

        StringBuilder texto =
                new StringBuilder();

        texto.append(
                "TOP DE PUNTAJES\n\n"
        );

        int posicion = 1;

        for (Piloto piloto : pilotos) {

            texto.append(posicion)
                    .append(". ")
                    .append(piloto.getNombre())
                    .append(" - ")
                    .append(piloto.getPuntaje())
                    .append(" puntos\n");

            posicion++;
        }

        JOptionPane.showMessageDialog(
                this,
                texto.toString(),
                "Top de Puntajes",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}