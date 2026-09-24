package quetzalspacedefender;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Juego extends JPanel implements KeyListener {

    private static final int ANCHO = 900;
    private static final int ALTO = 550;

    private int jugadorX = 80;
    private int jugadorY = 250;

    private boolean arriba;
    private boolean abajo;

    private boolean jugando = true;
    private boolean partidaRegistrada = false;

    private long bloqueadoHasta = 0;

    private final Piloto piloto;
    private final Nave nave;

    private final ArrayList<Proyectil> proyectiles =
            new ArrayList<>();

    private final ArrayList<Enemigo> enemigos =
            new ArrayList<>();

    private final ArrayList<Asteroide> asteroides =
            new ArrayList<>();

    private final ArrayList<Premio> premios =
            new ArrayList<>();

    private final Random random = new Random();

    private long ultimoDisparo = 0;
    private long ultimoEnemigo = 0;
    private long ultimoAsteroide = 0;
    private long ultimoPremio = 0;

    private final Timer timer;

    public Juego(Piloto piloto) {

        this.piloto = piloto;
        this.nave = new Nave(piloto.getNave());

        setPreferredSize(
                new Dimension(ANCHO, ALTO)
        );

        setBackground(Color.BLACK);

        setFocusable(true);

        addKeyListener(this);

        timer = new Timer(
                30,
                e -> actualizarJuego()
        );

        timer.start();

        SwingUtilities.invokeLater(
                this::requestFocusInWindow
        );
    }

    private void actualizarJuego() {

        if (!jugando) {
            repaint();
            return;
        }

        moverJugador();

        generarEnemigo();

        generarAsteroide();

        generarPremio();

        dispararAutomaticamente();

        limpiarObjetos();

        verificarColisiones();

        repaint();
    }

    private void moverJugador() {

        if (System.currentTimeMillis() < bloqueadoHasta) {
            return;
        }

        int velocidad =
                Math.max(
                        1,
                        nave.getVelocidad() / 2
                );

        if (arriba && jugadorY > 20) {

            jugadorY -= velocidad;
        }

        if (abajo && jugadorY < ALTO - 80) {

            jugadorY += velocidad;
        }
    }

    private void generarEnemigo() {

        long actual =
                System.currentTimeMillis();

        if (actual - ultimoEnemigo >= 1200) {

            int y =
                    40 + random.nextInt(
                            ALTO - 100
                    );

            Enemigo enemigo =
                    new Enemigo(
                            ANCHO,
                            y
                    );

            enemigos.add(enemigo);

            enemigo.start();

            ultimoEnemigo = actual;
        }
    }

    private void generarAsteroide() {

        long actual =
                System.currentTimeMillis();

        if (actual - ultimoAsteroide >= 2500) {

            int y =
                    40 + random.nextInt(
                            ALTO - 100
                    );

            Asteroide asteroide =
                    new Asteroide(
                            ANCHO,
                            y
                    );

            asteroides.add(asteroide);

            asteroide.start();

            ultimoAsteroide = actual;
        }
    }

    private void generarPremio() {

        long actual =
                System.currentTimeMillis();

        if (actual - ultimoPremio >= 5000) {

            int y =
                    40 + random.nextInt(
                            ALTO - 100
                    );

            Premio.Tipo[] tipos =
                    Premio.Tipo.values();

            Premio.Tipo tipo =
                    tipos[
                            random.nextInt(
                                    tipos.length
                            )
                    ];

            premios.add(
                    new Premio(
                            ANCHO,
                            y,
                            tipo
                    )
            );

            ultimoPremio = actual;
        }

        for (Premio premio : premios) {

            if (premio.isActivo()) {

                premio.mover();
            }
        }
    }

    private void dispararAutomaticamente() {

        long actual =
                System.currentTimeMillis();

        if (actual - ultimoDisparo
                >= nave.getTiempoDisparo()) {

            Proyectil proyectil =
                    new Proyectil(
                            jugadorX + 50,
                            jugadorY + 15
                    );

            proyectiles.add(proyectil);

            proyectil.start();

            ultimoDisparo = actual;
        }
    }

    private void limpiarObjetos() {

        proyectiles.removeIf(
                p -> !p.isActivo()
        );

        enemigos.removeIf(
                e -> !e.isActivo()
        );

        asteroides.removeIf(
                a -> !a.isActivo()
        );

        premios.removeIf(
                p -> !p.isActivo()
        );
    }

    private void verificarColisiones() {

        // ==========================================
        // PROYECTILES CONTRA ENEMIGOS
        // ==========================================

        for (Proyectil proyectil : proyectiles) {

            if (!proyectil.isActivo()) {
                continue;
            }

            for (Enemigo enemigo : enemigos) {

                if (!enemigo.isActivo()) {
                    continue;
                }

                if (colision(
                        proyectil.getX(),
                        proyectil.getY(),
                        15,
                        8,
                        enemigo.getX(),
                        enemigo.getY(),
                        40,
                        30)) {

                    enemigo.detener();

                    proyectil.detener();

                    piloto.sumarPuntos(10);

                    break;
                }
            }
        }

        // ==========================================
        // JUGADOR CONTRA ENEMIGOS
        // ==========================================

        for (Enemigo enemigo : enemigos) {

            if (!enemigo.isActivo()) {
                continue;
            }

            if (colision(
                    jugadorX,
                    jugadorY,
                    50,
                    40,
                    enemigo.getX(),
                    enemigo.getY(),
                    40,
                    30)) {

                finDelJuego(
                        "Nave destruida"
                );

                return;
            }
        }

        // ==========================================
        // JUGADOR CONTRA ASTEROIDES
        // ==========================================

        for (Asteroide asteroide : asteroides) {

            if (!asteroide.isActivo()) {
                continue;
            }

            if (colision(
                    jugadorX,
                    jugadorY,
                    50,
                    40,
                    asteroide.getX(),
                    asteroide.getY(),
                    45,
                    45)) {

                asteroide.detener();

                bloqueadoHasta =
                        System.currentTimeMillis()
                        + 2000;
            }
        }

        // ==========================================
        // JUGADOR CONTRA PREMIOS
        // ==========================================

        for (Premio premio : premios) {

            if (!premio.isActivo()) {
                continue;
            }

            if (colision(
                    jugadorX,
                    jugadorY,
                    50,
                    40,
                    premio.getX(),
                    premio.getY(),
                    30,
                    30)) {

                premio.recoger();

                switch (premio.getTipo()) {

                    case SNITCH:

                        // +150 puntos
                        piloto.sumarPuntos(150);

                        // Destruye enemigos visibles
                        for (Enemigo enemigo : enemigos) {

                            if (enemigo.isActivo()) {
                                enemigo.detener();
                            }
                        }

                        break;

                    case QUAFFLE:

                        // +10 puntos
                        piloto.sumarPuntos(10);

                        break;

                    case BLUDGER:

                        // Bloquea durante 2 segundos
                        bloqueadoHasta =
                                System.currentTimeMillis()
                                + 2000;

                        break;
                }
            }
        }
    }

    private boolean colision(
            int x1,
            int y1,
            int ancho1,
            int alto1,
            int x2,
            int y2,
            int ancho2,
            int alto2) {

        return x1 < x2 + ancho2
                && x1 + ancho1 > x2
                && y1 < y2 + alto2
                && y1 + alto1 > y2;
    }

    private void detenerTodosLosHilos() {

        for (Proyectil proyectil : proyectiles) {

            proyectil.detener();
        }

        for (Enemigo enemigo : enemigos) {

            enemigo.detener();
        }

        for (Asteroide asteroide : asteroides) {

            asteroide.detener();
        }
    }

    private void finDelJuego(String motivo) {

        if (!jugando) {
            return;
        }

        jugando = false;

        if (timer != null) {
            timer.stop();
        }

        detenerTodosLosHilos();

        DatosJuego.guardarCambiosPilotos();

        registrarPartida(motivo);

        JOptionPane.showMessageDialog(
                this,

                "FIN DE LA PARTIDA\n\n"
                + "Piloto: "
                + piloto.getNombre()
                + "\nNave: "
                + piloto.getNave()
                + "\nDificultad: "
                + nave.getDificultad()
                + "\nPuntaje: "
                + piloto.getPuntaje()
                + "\nMotivo: "
                + motivo,

                "Game Over",

                JOptionPane.INFORMATION_MESSAGE
        );

        Window ventana =
                SwingUtilities.getWindowAncestor(
                        this
                );

        if (ventana != null) {

            ventana.dispose();
        }
    }

    private void registrarPartida(
            String resultado) {

        if (partidaRegistrada) {
            return;
        }

        partidaRegistrada = true;

        String fecha =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm:ss"
                ).format(
                        new Date()
                );

        Partida partida =
                new Partida(
                        piloto.getNombre(),
                        piloto.getNave(),
                        piloto.getPuntaje(),
                        resultado,
                        fecha
                );

        DatosJuego.registrarPartida(
                partida
        );
    }

    @Override
    protected void paintComponent(
            Graphics g) {

        super.paintComponent(g);

        dibujarFondo(g);

        dibujarJugador(g);

        dibujarProyectiles(g);

        dibujarEnemigos(g);

        dibujarAsteroides(g);

        dibujarPremios(g);

        dibujarInformacion(g);
    }

    private void dibujarFondo(Graphics g) {

        g.setColor(Color.WHITE);

        for (int i = 0; i < 50; i++) {

            int x =
                    (i * 97) % ANCHO;

            int y =
                    (i * 53) % ALTO;

            g.fillRect(
                    x,
                    y,
                    2,
                    2
            );
        }
    }

    private void dibujarJugador(Graphics g) {

        g.setColor(Color.CYAN);

        int[] xPoints = {
            jugadorX,
            jugadorX,
            jugadorX + 50
        };

        int[] yPoints = {
            jugadorY,
            jugadorY + 40,
            jugadorY + 20
        };

        g.fillPolygon(
                xPoints,
                yPoints,
                3
        );

        if (System.currentTimeMillis()
                < bloqueadoHasta) {

            g.setColor(Color.RED);

            g.drawString(
                    "¡BLOQUEADO!",
                    jugadorX - 10,
                    jugadorY - 8
            );
        }
    }

    private void dibujarProyectiles(
            Graphics g) {

        g.setColor(Color.YELLOW);

        for (Proyectil proyectil :
                proyectiles) {

            g.fillRect(
                    proyectil.getX(),
                    proyectil.getY(),
                    15,
                    5
            );
        }
    }

    private void dibujarEnemigos(
            Graphics g) {

        for (Enemigo enemigo :
                enemigos) {

            enemigo.dibujar(g);
        }
    }

    private void dibujarAsteroides(
            Graphics g) {

        for (Asteroide asteroide :
                asteroides) {

            asteroide.dibujar(g);
        }
    }

    private void dibujarPremios(
            Graphics g) {

        for (Premio premio :
                premios) {

            premio.dibujar(g);
        }
    }

    private void dibujarInformacion(
            Graphics g) {

        g.setColor(Color.WHITE);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        g.drawString(
                "Piloto: "
                + piloto.getNombre(),
                20,
                25
        );

        g.drawString(
                "Nave: "
                + piloto.getNave(),
                20,
                45
        );

        g.drawString(
                "Dificultad: "
                + nave.getDificultad(),
                20,
                65
        );

        g.drawString(
                "Puntos: "
                + piloto.getPuntaje(),
                20,
                85
        );

        g.drawString(
                "↑ ↓ = Mover",
                700,
                25
        );

        g.drawString(
                "ESC = Salir",
                700,
                45
        );
    }

    @Override
    public void keyPressed(
            KeyEvent e) {

        if (e.getKeyCode()
                == KeyEvent.VK_UP) {

            arriba = true;
        }

        if (e.getKeyCode()
                == KeyEvent.VK_DOWN) {

            abajo = true;
        }

        if (e.getKeyCode()
                == KeyEvent.VK_ESCAPE) {

            finDelJuego(
                    "Partida terminada por el jugador"
            );
        }
    }

    @Override
    public void keyReleased(
            KeyEvent e) {

        if (e.getKeyCode()
                == KeyEvent.VK_UP) {

            arriba = false;
        }

        if (e.getKeyCode()
                == KeyEvent.VK_DOWN) {

            abajo = false;
        }
    }

    @Override
    public void keyTyped(
            KeyEvent e) {
    }
}