package quetzalspacedefender;

import java.awt.Color;
import java.awt.Graphics;

public class Asteroide extends Thread {

    private int x;
    private int y;
    private boolean activo;

    public Asteroide(int x, int y) {

        this.x = x;
        this.y = y;
        this.activo = true;
    }

    @Override
    public void run() {

        while (activo && x > -60) {

            x -= 4;

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        activo = false;
    }

    public void dibujar(Graphics g) {

        g.setColor(Color.GRAY);
        g.fillOval(x, y, 45, 45);
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
    }
}