/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import communication.Request;
import domain.Poruka;
import domain.User;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vojislav
 */
public class Server extends Thread{
    ServerSocket serverSocket;
    List<Poruka> poruke = new ArrayList<>();
    List<User> korisnici = new ArrayList<>();
    List<ClientThread> clientThreads = new ArrayList<>();

    public Server() {
        User k1 = new User("k1", "k1", "Pera 1", "Peric 1" );
        User k2 = new User("k2", "k2", "Pera 2", "Peric 2" );
        User k3 = new User("k3", "k3", "Pera 3", "Peric 3" );
        korisnici.add(k1); korisnici.add(k2);korisnici.add(k3);
    }

    @Override
    public void run() {
        // standardno ceka i acceptuje konekcije
        try {
            serverSocket = new ServerSocket(9000);
            while(!isInterrupted()){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Konektovan");
                
                // dodaje klijentski thread u listu i startuje ga, ovo je individualno za svakog korisnika i svaki od 
                // clientThread-ova osluskuje od svog korisnika
                ClientThread clientThread = new ClientThread(clientSocket,this);
                clientThread.start();
                clientThreads.add(clientThread);
                // dodati u clientThreads kako bi mogao kasnije da rolam kroz njih po potrebi
            }    
                
            } catch (IOException ex) {
                if(!isInterrupted()){
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        
    }
    public void stopServer() throws IOException{
        // interruptuje se thread
        interrupt();
        try {
            // interruptuju se svi clientThread-ovi i zatvaraju im se socketi
            for (ClientThread clientThread : clientThreads) {
                if(clientThread!=null&&clientThread.isAlive()){
                    clientThread.socket.close();
                    clientThread.interrupt();
                    System.out.println("Ugasen korisnik: "+clientThread.user.getEmail());
                }
                    
            }
            // zatvara se serverski soket
            if(serverSocket!=null&&!serverSocket.isClosed()){
                serverSocket.close(); 
                System.out.println("Iskljucen server");
            }
            // brisu se sve poruke koje su poslate
            poruke.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Object loginUser(User user) {
        // ide kroz listu korisnika koja se  cuva u operativnoj memoriji i autentifikuje, ako ne nadje onda vraca null
        for(User korisnik : korisnici){
            if(korisnik.getEmail().equals(user.getEmail())&&korisnik.getPassword().equals(user.getPassword())){
                return korisnik;
            }
        }
        return null;
    }

    void posaljiSvima(Poruka poruka) throws Exception {
        // svakom od thread-ova prosledjuje poruku
        for(ClientThread clientThread : clientThreads){
            clientThread.posaljiPoruku(poruka);
        }
        // dodaje poruku u listu poruka za kasnije koriscenje
        poruke.add(poruka);
    }
    public List<Poruka> getPoruke(){
        // vraca sve poruke poslate od trenutka poslednjeg paljenja
        return poruke;
    }

    void getLoggedInUsers(User korisnik) throws Exception{
        // ovo je fakd ap sto sam radio uzasno je al sta je tu je, radi poso
        
        // prvo trazi sve korisnike u clientThreads (loginovanim korisnicima u sustini) koji nisu korisnik koji je ovo zahtevao
        List<User> loggedInUsers = new ArrayList<>();
        for(ClientThread clientThread : clientThreads){
            if(!korisnik.getEmail().equals(clientThread.user.getEmail())){
              loggedInUsers.add(clientThread.user);  
            }
            
        }
        
        // i onda trazi onoga kome treba da posalje, ovo zasigurno moze jednostavnije
        for(ClientThread clientThread : clientThreads){
            if(korisnik.getEmail().equals(clientThread.user.getEmail())){
              clientThread.posaljiKorisnike(loggedInUsers); 
            }
            
        }
    }

    void posaljiSpecificnom(Poruka poruka) throws Exception{
        // za slanje specificnom korisniku, 
        // poredi da li korisnik koji treba da primi poruku ima email identican onom koji ima user clientThread-a
        // i poredi da li korisnik koji salje poruku ima email identican onom koji ima user clientThread-a
        for(ClientThread clientThread : clientThreads){
            if(
                    poruka.getPrimalac().getEmail().equals(clientThread.user.getEmail())
                    ||
                    poruka.getPosaljilac().getEmail().equals(clientThread.user.getEmail()))
            {
                clientThread.posaljiPoruku(poruka);
            }
        }
        // dodaje poruku za kasnije
        poruke.add(poruka);
    }

    void getAllFromUser(List<User> parKorisnika)throws Exception {
        List<Poruka> porukeOdKorisnika = new ArrayList<>();
        User posiljalac = parKorisnika.get(0);
        User primalac = parKorisnika.get(1);
        // ovo je haos aritmetika
        // poredi da li je posiljalacki email jednak posiljalackom emailu koji se nalazi u poruci, i da je pritom primalacki email jednak primalackom emailu koji se nalazi u poruci
        // ILI 
        // da je da li je posiljalacki email jednak posiljalackom emailu koji se nalazi u poruci, a da je pritom primalac jednak null (tj. salje se svima).
//        for(Poruka poruka : poruke){
//            if((posiljalac.getEmail().equals(poruka.getPosaljilac().getEmail())&&poruka.getPrimalac()==null)){
//                porukeOdKorisnika.add(poruka);
//            } 
//            else if (posiljalac.getEmail().equals(poruka.getPosaljilac().getEmail())
//                    &&
//                    primalac.getEmail().equals(poruka.getPrimalac().getEmail())){
//                porukeOdKorisnika.add(poruka);
//            }else{
//                
//            }   
//        }
        for (Poruka poruka : poruke) {
            if (posiljalac.getEmail().equals(poruka.getPosaljilac().getEmail())) {
                if (poruka.getPrimalac() == null || (primalac != null && primalac.getEmail().equals(poruka.getPrimalac().getEmail()))) {
                    porukeOdKorisnika.add(poruka);
                }
            }
        }
        // i onda trazi onog ko je zahtevao, tj. primaoca
        for(ClientThread clientThread : clientThreads){
            if(clientThread.user.getEmail().equals(primalac.getEmail())){
                clientThread.getAllFromUser(porukeOdKorisnika);
            }
        }
        
    }
    
    
}
