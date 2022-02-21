/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Threading;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Admin
 */
public class ClockThread extends Thread {
    private JLabel lbl;
    private String s;

    public ClockThread(JLabel lbl, String s) {
        this.lbl = lbl;
        this.s = s;
    }
    
    
    
    public void run (){
        SimpleDateFormat sdf = new SimpleDateFormat(s);
        while(true){
            try {
                Date now = new Date();
                String st = sdf.format(now);
                
                lbl.setText(st);
                
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
            
}
