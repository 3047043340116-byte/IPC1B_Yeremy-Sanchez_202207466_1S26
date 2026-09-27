package quetzalspacedefender;

import java.awt.Color;
import java.awt.Graphics;

public class Enemigo extends Thread {

    private int x;
    private final int y;
    private volatile boolean activo;
    private final int velocidad;

    public Enemigo(int x, int y) {
        this.x = x;
        this.y = y;
        this.velocidad = 5;
        this.activo = true;
    }

    @Override
    public void run() {

        while (activo && x > -50) {

            x -= velocidad;

            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        activo = false;
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 40, 30);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isActivo() {
        return activo;
    }

    public void detener() {
        activo = false;
        interrupt();
    }
}
