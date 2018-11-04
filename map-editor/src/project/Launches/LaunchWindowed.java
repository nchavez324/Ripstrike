package project.Launches;

import project.Runner;
import project.view.ViewCore.Display;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaunchWindowed {
    public static void main(String [] args){
        try {
             Runner wz = new Runner(Display.WINDOW, "-a");
            wz.run();
        } catch (IOException ex) {
            Logger.getLogger(LaunchWindowed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
