package Trabajos;
import kareltherobot.*;
import kareltherobot.Robot;

import java.awt.*;


class Tren extends Robot
{


    String linea;
    int x;
    int y;
    String sgte_accion;
    Sistema ref_sistema;
    boolean esperando_moverse = false;

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public void setSgte_accion(String sgte_accion) {
        this.sgte_accion = sgte_accion;
    }

    public Tren(int Street, int Avenue, Sistema ref_sistema)
    {
        super(Street, Avenue, Directions.East, 0);
        y = Street;
        x = Avenue;
        this.ref_sistema = ref_sistema;
        World.setupThread(this);
    }


    public Tren(int Street, int Avenue, Direction direction, Color color, Sistema ref_sistema)
    {
        super(Street, Avenue, direction, 0, color);
        y = Street;
        x = Avenue;
        World.setupThread(this);
        this.ref_sistema = ref_sistema;

    }

    public void salir_taller(){


        while(x != 16 || y != 32){
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

    }

    public void race()
    {
        while(! nextToABeeper())
            move();
        pickBeeper();
        turnOff();
    }

    public void run()
    {
        siguiente_accion();
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

    public void siguiente_accion(){
        if(sgte_accion.equals("salirtaller")) salir_taller();
        else if(sgte_accion.equals("ruta")) ruta();
    }

}
