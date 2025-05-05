package Trabajos;
import kareltherobot.*;
import kareltherobot.Robot;

import java.awt.*;


class Tren extends Robot
{

    char estado; //I para inicializando, R para ruta, y C para cerrando.
    String ruta;
    int x;
    int y;
    String accion;
    Sistema ref_sistema;
    boolean esperando_moverse = false;

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Tren(int Street, int Avenue, Sistema ref_sistema)
    {
        super(Street, Avenue, Directions.East, 0);
        y = Street;
        x = Avenue;
        this.ref_sistema = ref_sistema;
        World.setupThread(this);
        estado = 'I';
    }


    public Tren(int Street, int Avenue, Direction direction, Color color, Sistema ref_sistema)
    {
        super(Street, Avenue, direction, 0, color);
        y = Street;
        x = Avenue;
        World.setupThread(this);
        this.ref_sistema = ref_sistema;
        estado = 'I';

    }

    public void salir_taller(){

        while(x != 15 || y != 32){
            if(!esperando_moverse) {
                if ((y == 35 && x == 15) || (x == 1 && y == 35)
                        || (x == 1 && y == 34)) turnLeft();

                if (y == 34 && x == 14) {
                    turnLeft();
                    turnLeft();
                    turnLeft();
                }

                if (y == 32 && x == 14) {
                    turnLeft();
                    turnLeft();
                    turnLeft();
                    turnLeft();
                    turnLeft();
                }
            }
            avanzar();
        }
        ir_extremos();


    }


    public boolean llego_extremo(){
        short x;
        short y;
        if(ruta.equals("B")){
            x = 1;
            y = 16;
        }
        else if (ruta.equals("AE")){
            x = 19;
            y = 35;
        }
        else{
            y=1;
            x=11;
        }

        if(this.x == x && this.y == y){
            ref_sistema.trenes_extremos++;
            return true;
        }
        else return false;
    }


    public void turnRight(){
        turnLeft();
        turnLeft();
        turnLeft();
    }

    public void ir_extremos(){
        while (!llego_extremo()) {
            if(!esperando_moverse) {
                if (ruta.equals("B") || ruta.equals("AN")) {
                    if (
                            (y == 32 && x == 16) ||
                                    (y == 29 && x == 16) ||
                                    (y == 26 && x == 15) ||
                                    (y == 23 && x == 13)
                    ) {
                        turnRight();
                    } else if (
                            (y == 29 && x == 15) ||
                                    (y == 26 && x == 13) ||
                                    (y == 23 && x == 11)
                    ) {
                        turnLeft();
                    }
                }

                if (ruta.equals("B")) {
                    if (
                            (y == 14 && x == 11) ||
                                    (y == 14 && x == 7) ||
                                    (y == 15 && x == 2)
                    ) {
                        turnRight();
                    } else if (
                            (y == 15 && x == 7) ||
                                    (y == 17 && x == 2) ||
                                    (y == 17 && x == 1)
                    ) {
                        turnLeft();
                    }
                }

                if (ruta.equals("AN")) {
                    if (
                            (y == 18 && x == 16) ||
                                    (y == 11 && x == 16) ||
                                    (y == 5 && x == 13) ||
                                    (y == 2 && x == 12)
                    ) {
                        turnRight();
                    } else if (
                            (y == 18 && x == 11) ||
                                    (y == 11 && x == 13) ||
                                    (y == 5 && x == 12) ||
                                    (y == 2 && x == 10) ||
                                    (y == 1 && x == 10)
                    ) {
                        turnLeft();
                    }
                }

                if (!ruta.equals("B") && !ruta.equals("AN")) {
                    if (
                            (y == 34 && x == 17)
                    ) {
                        turnRight();
                    } else if (
                            (y == 32 && x == 17) ||
                                    (y == 34 && x == 20) ||
                                    (y == 35 && x == 20)
                    ) {
                        turnLeft();
                    }
                }
            avanzar();
        }
        esperar_inicializacion();

    }

    public void esperar_inicializacion(){
        while(ref_sistema.estado == 'I');
        ruta();
    }

    public void run()
    {
        ejecutar_accion();
    }

    public void avanzar(){

        int next_x = x;
        int next_y = y;
        if (facingEast()) next_x++;
        else if (facingWest()) next_x--;
        else if (facingNorth()) next_y++;
        else if (facingSouth()) next_y--;
        ref_sistema.bloqueo.lock();
        if(!ref_sistema.posiciones[next_y][next_x]) {
            ref_sistema.posiciones[y][x] = false;
            x = next_x;
            y = next_y;
            ref_sistema.posiciones[next_y][next_x] = true;
            ref_sistema.bloqueo.unlock();
            esperando_moverse = false;
            move();
        }
        else{
            ref_sistema.bloqueo.unlock();
            esperando_moverse = true;
        }

    }

    public void ruta(){
        //aca me falta poner la logica de las rutas y poner a dormir el thread cuando se alcance una estación
    }

    public void ejecutar_accion(){
        if(accion.equals("salirtaller")) salir_taller();
        else if(accion.equals("ruta")) ruta();
    }

}
