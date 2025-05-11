package Trabajos;

import kareltherobot.World;
import Trabajos.Sistema;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        World.readWorld("MetroMed.kwld");
        World.setVisible(true);
        World.setDelay(0);
        Sistema sistema_metro = new Sistema();
        World.setDelay(20);
        String decision = "";
        new Thread(sistema_metro).start();
        System.out.println("Pase 1");
        sistema_metro.despachar();
        System.out.println("Pase 2");
        sistema_metro.despachar();
        System.out.println("Pase 3");
        sistema_metro.despachar();
        while(!decision.toLowerCase().equals("y")){
            System.out.print("¿Desea dar comienzo a la ruta del sistema metro? (Y): ");
            decision = scanner.nextLine();
        }
        sistema_metro.estado = 'R';


    }

}
