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
        sendMessage(message);
        stopConnection();
        return user;
    }

    @Override
    public User signUp(User user) {
        Message message = new Message(user, TypeMessage.SIGN_UP);
        this.sendMessage(message);
        this.stopConnection();
        return user;

    }

    public Message sendMessage(Message msg) {
        Message message = null;
        try {

            clientSocket = new Socket("localhost", 3333);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(msg); // Send message to server

            ois = new ObjectInputStream(this.clientSocket.getInputStream());
            message = (Message) ois.readObject();

            return message;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }

    public void stopConnection() {
        try {

            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
