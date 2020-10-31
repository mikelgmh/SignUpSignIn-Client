package signupsignin.signable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import interfaces.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.UserNotFoundException;
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
    public User signIn(User user) throws UserNotFoundException {
        Message message = new Message(user, TypeMessage.SIGN_IN);
        this.sendMessage(message);
        message = this.getMessage();
        this.stopConnection();
        // TODO: Pasar todo el switch que comprueba el tipo de mensaje a un metodo
        switch (message.getType()) {
            case USER_DOES_NOT_EXIST:
                throw new UserNotFoundException();
            default:
                break;
        }
        return user;
    }

    @Override
    public User signUp(User user) {
        Message message = new Message(user, TypeMessage.SIGN_UP);
        this.sendMessage(message);
        this.stopConnection();
        return user;
    }

    public void sendMessage(Message msg) {
        try {
            // TODO: Coger los campos del socket desde el archivo de configuración
            clientSocket = new Socket("localhost", 3333);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(msg); // Send message to server
        } catch (IOException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Message getMessage() {
        Message message = null;
        try {
            ois = new ObjectInputStream(this.clientSocket.getInputStream());
            message = (Message) ois.readObject(); // Take message from the server
        } catch (IOException | ClassNotFoundException e) {
            // TODO: Logger de la excepcion de getMessage()
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
