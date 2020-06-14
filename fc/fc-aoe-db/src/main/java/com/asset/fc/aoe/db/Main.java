package com.asset.fc.aoe.db;

import com.asset.fc.aoe.db.manager.AOE_ApplicationManager;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        AOE_ApplicationManager ap = new AOE_ApplicationManager();
        ap.Start();

        Scanner input = new Scanner(System.in);
        input.next(); // take an input signle for shutdown

        System.out.println("The system is Terminating....");
        System.exit(0); //termiantes the current running JVM

    }
}
