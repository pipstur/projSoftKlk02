/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package klijent;

import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import domain.Poruka;
import domain.User;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author vojislav
 */
public class Client extends  Thread {
    User user;
    Socket socket;
    Sender sender;
    Receiver receiver;
    public JTextArea txtChat;
    public JComboBox cbLoginovani;
    public JFrame frmKlijent;

    public Client(User user, Socket socket, JTextArea txtChat, JComboBox cbLoginovani, JFrame frmKlijent) {
        this.user = user;
        this.socket = socket;
        this.txtChat = txtChat;
        this.cbLoginovani = cbLoginovani;
        this.frmKlijent = frmKlijent;
        
        sender = new Sender(socket);
        receiver = new Receiver(socket);
        
    }
    
    
    
    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                Request request = (Request) receiver.receive();
                Response response = new Response();
                // ispod svake u switchu pise continue jer ne saljem 
                // zapravo nista serveru vec ovaj thread sluzi samo da prima info u realtime-u
                switch(request.getOperation()){
                    // za primanje poruke koje ostali posalju
                    case SEND_MESSAGE:
                        Poruka poruka = (Poruka) request.getArgument();
                        txtChat.setText(txtChat.getText()+"\n"+poruka);
                        continue;
                    
                    // za primanje onlajn korisnika
                    case GET_ONLINE_USERS:
                        List<User> korisnici = (List<User>) request.getArgument();
                        cbLoginovani.setModel(new DefaultComboBoxModel(korisnici.toArray()));
                        continue;
                    
                    // za primanje svih poruka od od odredjenog korisnika
                    case GET_ALL_FROM_USER:
                        List<Poruka> porukeOdKorisnika = (List<Poruka>) request.getArgument();
                        String tekstOdKorisnika = "";
                        for(Poruka porukaOdKorisnika : porukeOdKorisnika){
                            tekstOdKorisnika = tekstOdKorisnika + "\n"+porukaOdKorisnika;
                        }
                        txtChat.setText(tekstOdKorisnika);
                        continue;
                    case SERVER_CLOSED:
                        socket.close();
                        frmKlijent.dispose();
                        this.interrupt();
                        continue;
                }
                
                sender.send(response);
            } catch (Exception ex) {
                if(!isInterrupted())
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
