package signupsignin.signable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import interfaces.Signable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.TypeMessage;
import user.User;

/**
 *
 * @author Mikel
 */
public class SignableImplementation implements Signable {

    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public SignableImplementation() {

    }

    @Override
    public User signIn(User user) {
        Message message = new Message(user, TypeMessage.SIGN_IN);
        startConnection("localhost", 3333);
        sendMessage(message);
        stopConnection();
        return user;
    }

    @Override
    public User signUp(User user) {
        Message message = new Message(user, TypeMessage.SIGN_UP);
        this.startConnection("localhost", 3333);
        this.sendMessage(message);
        this.stopConnection();
        return user;

    }

    public String sendMessage(Message msg) {
        String resp = "";
        try {
            // out.println(msg);
            oos.writeObject(msg);
            //resp = (String) ois.readObject();
            System.out.println("RESPUESTA::::: ");
            System.out.println(resp);
           // resp = in.readLine();
            return resp;
        } catch (IOException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resp;
    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            //out = new PrintWriter(clientSocket.getOutputStream(), true);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            // ois = new ObjectInputStream(this.clientSocket.getInputStream());
            //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopConnection() {
        try {
            //in.close();
            //out.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
