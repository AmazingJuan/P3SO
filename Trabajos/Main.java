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
        World.setDelay(30);
        String decision = "";
        new Thread(sistema_metro).start();
        while(sistema_metro.trenes_extremos != 3);
        while(!decision.equalsIgnoreCase("si")){
            System.out.println("Son las 4:20? (Si/No)");
            decision = scanner.nextLine();
        }
        sistema_metro.estado = 'R';
    }

}
