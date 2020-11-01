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

import exceptions.*;
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
    public User signIn(User user) throws UserNotFoundException, ErrorConnectingDatabaseException,
            PasswordMissmatchException, ErrorClosingDatabaseResources, QueryException {
        Message message = new Message(user, TypeMessage.SIGN_IN);
        this.sendMessage(message);
        message = this.getMessage();
        this.stopConnection();

        // Comprobar el tipo de mensaje que ha recibido
        switch (message.getType()) {
            case USER_DOES_NOT_EXIST:
                throw new UserNotFoundException();
            case CONNECTION_ERROR:
                throw new ErrorConnectingDatabaseException();
            case LOGIN_ERROR:
                throw new PasswordMissmatchException();
            case DATABASE_ERROR:
                throw new ErrorClosingDatabaseResources();
            case QUERY_ERROR:
                throw new QueryException();
            default:
                break;
        }
        return message.getUser();
    }

    @Override
    public User signUp(User user) {
        Message message = new Message(user, TypeMessage.SIGN_UP);
        this.sendMessage(message);
        message = this.getMessage();
        this.stopConnection();
        return message.getUser();
    }

    public void sendMessage(Message msg) {
        try {
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
