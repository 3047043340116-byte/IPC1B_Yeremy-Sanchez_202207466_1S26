package quetzalspacedefender;

public class Proyectil extends Thread {

    private int x;
    private final int y;
    private volatile boolean activo;

    public Proyectil(int x, int y) {
        this.x = x;
        this.y = y;
        this.activo = true;
    }

    @Override
    public void run() {

        while (activo && x < 900) {

            x += 15;

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        activo = false;
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
