package Trabajos;

import Trabajos.Tren;
import kareltherobot.Directions;
import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Sistema {

    volatile char estado = 'I'; //I para inicialización, R para ruta, y C para cierre.
    public final ReentrantLock bloqueo = new ReentrantLock();
    volatile short trenes_extremos = 0;
    ArrayList<Tren> taller = new ArrayList<>();
    ArrayList<Tren> lineaA = new ArrayList<>();
    ArrayList<Tren> lineaB = new ArrayList<>();

    boolean[][] posiciones = new boolean[36][21];
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
            posiciones[y][x] = true;
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
    }

    public void inicializar(){

        new Thread(taller.getLast()).start(); 
        new Thread(taller.get(taller.size()-2)).start(); 
        new Thread(taller.get(taller.size()-3)).start(); 
    }


    public void despachar(int numero_tren){
            new Thread(taller.get(numero_tren)).start();
    }

    public void proceso_ruta(){
        while(trenes_extremos != 3);
        estado = 'R';
        int current_tren = 28;
        while(estado != 'C'){
            if(current_tren != -1){
                despachar(current_tren);
                current_tren--;
            }
        }
    }
}
