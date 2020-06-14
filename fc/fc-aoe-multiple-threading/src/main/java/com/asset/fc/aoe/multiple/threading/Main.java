package com.asset.fc.aoe.multiple.threading;

import com.asset.fc.aoe.multiple.threading.manager.AOEApplicationManager;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        AOEApplicationManager ap = new AOEApplicationManager();
        ap.Start();

        Scanner input = new Scanner(System.in);
        input.next(); // take an input signle for shutdown

        System.out.println("The system is Terminating....");
        System.exit(0); //termiantes the current running JVM

    }
}
