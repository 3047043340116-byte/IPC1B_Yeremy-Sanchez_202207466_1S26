package quetzalspacedefender;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        DatosJuego.cargarDatos();

        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}
