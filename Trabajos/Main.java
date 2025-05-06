package Trabajos;

import kareltherobot.World;
import Trabajos.Sistema;

public class Main {
    public static void main(String[] args) {
        World.readWorld("MetroMed.kwld");
        World.setVisible(true);
        World.setDelay(0);
        Sistema sistema_metro = new Sistema();
        World.setDelay(5);
        sistema_metro.inicializar();
        sistema_metro.empezar_ruta();
    }

}
