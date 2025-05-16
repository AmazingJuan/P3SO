package Trabajos;

import kareltherobot.Directions;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Sistema implements Runnable {

    public volatile char estado = 'I'; //I para inicialización, R para ruta, y C para cierre.
    public final ReentrantLock lock_move = new ReentrantLock();
    public final ReentrantLock lock_san_antonio = new ReentrantLock();
    public final Condition condicion_san_antonio = lock_san_antonio.newCondition();
    volatile short trenes_extremos = 0;
    ArrayList<Tren> taller = new ArrayList<>();
    ArrayList<Tren> enRuta = new ArrayList<>();
    volatile boolean[][] posiciones = new boolean[36][21];
    volatile boolean tren_sanantonio = false;
    public Sistema(){
        short cont = 0;
        short cont_aux = 1;
        Directions.Direction actual_direction = Directions.North;

        int x = 15, y = 34;

        for(int i = 0; i < 32; i++){
            if(cont_aux == 3) {
                taller.add(new Tren(y, x, actual_direction, Color.GREEN, this));
                taller.get(cont).setRuta("BSA");
                cont_aux = 1;
            }
            else{
                taller.add(new Tren(y, x, actual_direction, Color.BLUE, this));
                if(cont_aux == 1){
                    taller.get(cont).setRuta("AE");
                }
                else{
                    taller.get(cont).setRuta("AN");
                }
                cont_aux++;
            }
            posiciones[y - 1][x - 1] = true;
            taller.get(cont).setAccion("salirtaller");
            if(actual_direction == Directions.East) x++;
            else if(actual_direction == Directions.West) x--;
            else if(actual_direction == Directions.North) y++;
            else if(actual_direction == Directions.South) y--;

            if(y == 35 && x == 15) actual_direction = Directions.West;
            else if((y == 35 && x == 1) || (y == 34 && x == 14) ) actual_direction = Directions.South;
            else if((y == 34 && x == 1) || (y == 32 && x == 14)) actual_direction = Directions.East;
            cont++;
        }

        for(int i = 0; i < 32; i++) new Thread(taller.get(i)).start();

    }

    public void inicializar(){
        despachar();
        despachar();
        despachar();
    }


    public void despachar(){
        taller.getLast().salida_permitida = true;
        enRuta.add(taller.getLast());
        taller.remove(taller.getLast());
    }

    public void run(){
        inicializar();
        proceso_ruta();
    }

    public void esperar(){
        try {
            Thread.sleep(20000); // duerme 3 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void proceso_ruta(){
        while(trenes_extremos != 3 || estado != 'R');
        while(estado != 'C'){
            if(taller.size() != 0){
                despachar();
                esperar();
            }
        }
    }
}
