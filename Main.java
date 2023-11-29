import java.util.Random;


public class Main {
    public static void main(String[] args) {
        Taller taller = new Taller();
        Trabajador trabajador1 = new Trabajador(taller, "Mangas");
        Trabajador trabajador2 = new Trabajador(taller, "Cuerpos");
        Trabajador trabajador3 = new Trabajador(taller, "Prendas");


        trabajador1.start();
        trabajador2.start();
        trabajador3.start();
    }


    public static class Taller {
        private int numMangas;
        private int numMangasMAX = 10;


        private int numCuerpos;
        private int numCuerposMAX = 5;


        private int numPrendas;


        public synchronized void hacerMangas() throws InterruptedException {
            while (numMangas >= numMangasMAX) {
                System.out.println("Cesta de mangas llena. Esperando...");
                wait();
            }
            numMangas++;
            System.out.println("Se ha fabricado una manga. Hay " + numMangas + " mangas en la cesta");
            notifyAll();
        }


        public synchronized void hacerCuerpos() throws InterruptedException {
            while (numCuerpos >= numCuerposMAX) {
                System.out.println("Cesta de cuerpos llena. Esperando...");
                wait();
            }
            numCuerpos++;
            System.out.println("Se ha fabricado un cuerpo. Hay " + numCuerpos + " cuerpos en la cesta");
            notifyAll();
        }


        public synchronized void hacerPrendas() {
            while (numMangas < 2 || numCuerpos < 1) {
                System.out.println("No hay suficientes mangas o cuerpos para hacer una prenda. Esperando...");
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            numMangas -= 2;
            numCuerpos--;
            numPrendas++;
            System.out.println("Se ha fabricado una prenda. Prendas totales fabricadas: " + numPrendas);
            notifyAll();
        }
    }


    public static class Trabajador extends Thread {
        private Taller taller;
        private String trabajador;
        private Random random = new Random();


        public Trabajador(Taller taller, String trabajador) {
            this.taller = taller;
            this.trabajador = trabajador;
        }


        @Override
        public void run() {
            try {
                for (;;) {
                    trabajar();
                    Thread.sleep(random.nextInt(3000) + 2000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


        private void trabajar() throws InterruptedException {
            if (trabajador.equals("Mangas")) {
                System.out.println("Se ha fabricado una manga");
                taller.hacerMangas();
            } else if (trabajador.equals("Cuerpos")) {
                System.out.println("Se ha fabricado un cuerpo");
                taller.hacerCuerpos();
            } else if (trabajador.equals("Prendas")) {
                Thread.sleep(random.nextInt(12000) + 6000);
                taller.hacerPrendas();
            }
        }
    }
}