package quetzalspacedefender;

import java.awt.Color;
import java.awt.Graphics;

public class Premio {

    public enum Tipo {
        SNITCH,
        BLUDGER,
        QUAFFLE
    }

    private int x;
    private final int y;
    private final Tipo tipo;
    private boolean activo;

    public Premio(int x, int y, Tipo tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.activo = true;
    }

    public void mover() {

        x -= 4;

        if (x < -50) {
            activo = false;
        }
    }

    public void dibujar(Graphics g) {

        switch (tipo) {

            case SNITCH:
                g.setColor(Color.YELLOW);
                break;

            case BLUDGER:
                g.setColor(Color.DARK_GRAY);
                break;

            case QUAFFLE:
                g.setColor(Color.ORANGE);
                break;
        }

        g.fillOval(x, y, 30, 30);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void recoger() {
        activo = false;
    }
}
