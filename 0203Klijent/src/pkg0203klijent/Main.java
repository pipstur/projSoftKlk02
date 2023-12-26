/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pkg0203klijent;

import javax.swing.JFrame;

/**
 *
 * @author vojislav
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // ako zelite vise korisnika samo desni klik run Main.java vise puta i unesite k1,k2,k3 kao korisnike
        // to su im sifre mrzelo me da kucam svaki put pa sam promenio, mozete da vidite to u klasi Server
        JFrame frmMain = new FrmLogin();
        frmMain.setVisible(true);
    }
    
}
