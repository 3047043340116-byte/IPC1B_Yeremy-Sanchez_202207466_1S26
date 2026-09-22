package quetzalspacedefender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Juego extends JPanel implements KeyListener {

    private int jugadorX = 80;
    private int jugadorY = 250;

    private int ancho = 900;
    private int alto = 550;

    private boolean arriba = false;
    private boolean abajo = false;
    private boolean jugando = true;

    private Piloto piloto;
    private Nave nave;

    private ArrayList<Proyectil> proyectiles;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<Asteroide> asteroides;
    private ArrayList<Premio> premios;

    private Random random;

    private long ultimoDisparo = 0;
    private long ultimoEnemigo = 0;
    private long ultimoAsteroide = 0;
    private long ultimoPremio = 0;

    public Juego(Piloto piloto) {

        this.piloto = piloto;
        this.nave = new Nave(piloto.getNave());

        proyectiles = new ArrayList<>();
        enemigos = new ArrayList<>();
        asteroides = new ArrayList<>();
        premios = new ArrayList<>();

        random = new Random();

        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.BLACK);

        setFocusable(true);
        addKeyListener(this);

        Timer timer = new Timer(30, e -> actualizarJuego());
        timer.start();

        requestFocusInWindow();
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

        if (arriba && jugadorY > 20) {
            jugadorY -= nave.getVelocidad() / 2;
        }

        if (abajo && jugadorY < alto - 80) {
            jugadorY += nave.getVelocidad() / 2;
        }
    }

    private void generarEnemigo() {

        long tiempoActual = System.currentTimeMillis();

        if (tiempoActual - ultimoEnemigo > 1500) {

            int y = 40 + random.nextInt(alto - 100);

            Enemigo enemigo = new Enemigo(ancho, y);

            enemigos.add(enemigo);
            enemigo.start();

            ultimoEnemigo = tiempoActual;
        }
    }

    private void generarAsteroide() {

        long tiempoActual = System.currentTimeMillis();

        if (tiempoActual - ultimoAsteroide > 2500) {

            int y = 40 + random.nextInt(alto - 100);

            Asteroide asteroide = new Asteroide(ancho, y);

            asteroides.add(asteroide);
            asteroide.start();

            ultimoAsteroide = tiempoActual;
        }
    }

    private void generarPremio() {

        long tiempoActual = System.currentTimeMillis();

        if (tiempoActual - ultimoPremio > 5000) {

            int y = 40 + random.nextInt(alto - 100);

            Premio.Tipo[] tipos = Premio.Tipo.values();

            Premio.Tipo tipo =
                    tipos[random.nextInt(tipos.length)];

            premios.add(
                    new Premio(ancho, y, tipo)
            );

            ultimoPremio = tiempoActual;
        }

        for (Premio premio : premios) {

            if (premio.isActivo()) {
                premio.mover();
            }
        }
    }

    private void dispararAutomaticamente() {

        long tiempoActual = System.currentTimeMillis();

        if (tiempoActual - ultimoDisparo >= nave.getTiempoDisparo()) {

            Proyectil proyectil =
                    new Proyectil(jugadorX + 50, jugadorY + 15);

            proyectiles.add(proyectil);
            proyectil.start();

            ultimoDisparo = tiempoActual;
        }
    }

    private void limpiarObjetos() {

        proyectiles.removeIf(p -> !p.isActivo());
        enemigos.removeIf(e -> !e.isActivo());
        asteroides.removeIf(a -> !a.isActivo());
        premios.removeIf(p -> !p.isActivo());
    }

    private void verificarColisiones() {

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
                }
            }
        }

        for (Enemigo enemigo : enemigos) {

            if (colision(
                    jugadorX,
                    jugadorY,
                    50,
                    40,
                    enemigo.getX(),
                    enemigo.getY(),
                    40,
                    30)) {

                finDelJuego();
            }
        }

        for (Asteroide asteroide : asteroides) {

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

                jugadorY += 20;

                if (jugadorY > alto - 80) {
                    jugadorY = alto - 80;
                }
            }
        }

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

                        piloto.sumarPuntos(150);

                        for (Enemigo enemigo : enemigos) {
                            enemigo.detener();
                        }

                        break;

                    case QUAFFLE:

                        piloto.sumarPuntos(10);

                        break;

                    case BLUDGER:

                        jugadorY += 50;

                        if (jugadorY > alto - 80) {
                            jugadorY = alto - 80;
                        }

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

        return x1 < x2 + ancho2 &&
               x1 + ancho1 > x2 &&
               y1 < y2 + alto2 &&
               y1 + alto1 > y2;
    }

    private void finDelJuego() {

        jugando = false;

        JOptionPane.showMessageDialog(
                this,
                "Fin de la partida\n"
                + "Piloto: " + piloto.getNombre()
                + "\nPuntaje: " + piloto.getPuntaje(),
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );

        Window ventana = SwingUtilities.getWindowAncestor(this);

        if (ventana != null) {
            ventana.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

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

        for (int i = 0; i < 40; i++) {

            int x = (i * 97) % ancho;
            int y = (i * 53) % alto;

            g.fillRect(x, y, 2, 2);
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

        g.fillPolygon(xPoints, yPoints, 3);
    }

    private void dibujarProyectiles(Graphics g) {

        g.setColor(Color.YELLOW);

        for (Proyectil proyectil : proyectiles) {

            g.fillRect(
                    proyectil.getX(),
                    proyectil.getY(),
                    15,
                    5
            );
        }
    }

    private void dibujarEnemigos(Graphics g) {

        for (Enemigo enemigo : enemigos) {
            enemigo.dibujar(g);
        }
    }

    private void dibujarAsteroides(Graphics g) {

        for (Asteroide asteroide : asteroides) {
            asteroide.dibujar(g);
        }
    }

    private void dibujarPremios(Graphics g) {

        for (Premio premio : premios) {
            premio.dibujar(g);
        }
    }

    private void dibujarInformacion(Graphics g) {

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        g.drawString(
                "Piloto: " + piloto.getNombre(),
                20,
                25
        );

        g.drawString(
                "Nave: " + piloto.getNave(),
                20,
                45
        );

        g.drawString(
                "Puntos: " + piloto.getPuntaje(),
                20,
                65
        );
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            arriba = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            abajo = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            finDelJuego();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            arriba = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            abajo = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}