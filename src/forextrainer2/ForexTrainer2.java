/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forextrainer2;

import forexPatternTrainer.MainPatternTrainer;
import gui.SimulatorTrainer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author david
 */
public class ForexTrainer2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
    } 
    catch (UnsupportedLookAndFeelException e) {
       // handle exception
    }
    catch (ClassNotFoundException e) {
       // handle exception
    }
    catch (InstantiationException e) {
       // handle exception
    }
    catch (IllegalAccessException e) {
       // handle exception
    }
        // TODO code application logic here
        String  workingDir = "c:\\";
        if (args.length>0)
            workingDir = args[0];
        //SimulatorTrainer simulator = new SimulatorTrainer(workingDir);
        //simulator.setVisible(true);
        MainPatternTrainer mainFrame = new MainPatternTrainer();
        mainFrame.setVisible(true);
    }
}
