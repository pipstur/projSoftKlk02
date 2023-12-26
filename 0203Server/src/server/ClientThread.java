/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import communication.Operation;
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

/**
 *
 * @author vojislav
 */
public class ClientThread extends Thread{
    Server server;
    Sender sender;
    Receiver receiver;
    public User user;
    Socket socket;
   
    public ClientThread(Socket clientSocket, Server server) {
        this.socket = clientSocket;
        this.server = server;
        sender = new Sender(socket);
        receiver = new Receiver(socket);
        
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                Request request = (Request) receiver.receive();
                Response response = new Response();
                switch(request.getOperation()){
                    // login je standardno za verifikaciju od strane servera i vraca info klijentskom controlleru, pa samim tim i login formi
                    case LOGIN:
                        User user = (User)request.getArgument();
                        user = (User) server.loginUser(user);
                        response.setResult(user);
                        this.user = user;
                        break;
                        
                    // Ako je primalac null onda svima prosledi poruku, a ako ipak ima onda prosledjuje samo tom korisniku kome je namenjena    
                    case SEND_MESSAGE:
                        Poruka poruka = (Poruka)request.getArgument();
                        if(poruka.getPrimalac()==null){
                          server.posaljiSvima(poruka);
                          continue;
                        }else{
                            server.posaljiSpecificnom(poruka);
                            continue;
                        }
                    
                    // Koristi par korisnika kako bi uporedilo posiljaoca i primaoca u porukama 
                    case GET_ALL_FROM_USER:
                        List<User> parKorisnika = (List<User>) request.getArgument();
                        server.getAllFromUser(parKorisnika);
                        continue;
                        
                    // koristi ovog korisnika kako ne bi njega vracalo u listi onlajn korisnika    
                    case GET_ONLINE_USERS:
                        User korisnik = (User) request.getArgument();
                        server.getLoggedInUsers(korisnik);
                        continue;
                }
                sender.send(response);
            } catch (Exception ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // za hendlovanje rekvestova, tj. slanje onim korisnicima kojima treba da se posalje, putem njihovih individualnih threadova
    
    void posaljiPoruku(Poruka poruka) throws Exception {
        Request request = new Request(Operation.SEND_MESSAGE, poruka);
        sender.send(request);
    }

    void posaljiKorisnike(List<User> loggedInUsers) throws Exception{
        Request request = new Request(Operation.GET_ONLINE_USERS, loggedInUsers);
        sender.send(request);
    }

    void getAllFromUser(List<Poruka> porukeOdKorisnika)throws Exception {
        Request request = new Request(Operation.GET_ALL_FROM_USER, porukeOdKorisnika);
        sender.send(request);
    }
    
    
}
