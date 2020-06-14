package com.asset.fc.engine;

import com.asset.fc.engine.manager.Engine_ApplicationManager;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, Exception {

        Engine_ApplicationManager ap = new Engine_ApplicationManager();
        ap.Start();
    }
}
