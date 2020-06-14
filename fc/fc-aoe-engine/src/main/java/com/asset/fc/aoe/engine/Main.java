package com.asset.fc.aoe.engine;

import com.asset.fc.aoe.engine.manager.AOE_ApplicationManager;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        AOE_ApplicationManager ap = new AOE_ApplicationManager();
        ap.Start();
        System.out.println("The system is Terminating....");
        Scanner input = new Scanner(System.in);
        String shutDownSignle = input.next(); // take an input signle for shutdown
        System.exit(0); //termiantes the current running JVM
    }
}
