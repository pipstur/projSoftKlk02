/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package klijent;

import communication.Operation;
import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import domain.Poruka;
import domain.User;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author vojislav
 */
public class Controller {
    public Socket socket;
    public Sender sender;
    public Receiver receiver;
    static Controller instance;

    public Controller() throws IOException {
        socket = new Socket("127.0.0.1",9000);
        sender = new Sender(socket);
        receiver = new Receiver(socket);
    }
    
    public static Controller getInstance() throws IOException{
        if(instance==null){
            instance=new Controller();
        }
        return instance;
    }
    
    // ovo je specificna funkcija koja jedina zapravo ceka response a to je pre nego sto se klijentska nit prvi put pokrene, u login frejmu
    public User login(User user) throws IOException, Exception{
        Request request = new Request(Operation.LOGIN, user);
        sender.send(request);
        Response response = (Response) receiver.receive();
        if(response.getException()==null){
            return (User) response.getResult();
        }else{
            throw response.getException();
        }
    }
    
    // standardno ostalo
    public void posaljiPoruku(Poruka poruka) throws Exception{
        Request request = new Request(Operation.SEND_MESSAGE, poruka);
        System.out.println("Poruka poslata"+poruka);
        sender.send(request);
    }

    public void getOnlineUsers(User user) throws Exception {
        Request request = new Request(Operation.GET_ONLINE_USERS, user);
        sender.send(request);
    }

    public void getAllFromUser(List<User> parKorisnika) throws Exception{
        Request request = new Request(Operation.GET_ALL_FROM_USER, parKorisnika);
        sender.send(request);
    }
    
    
    
    
}
