package signupsignin.signable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import exceptions.ErrorConnectingDatabaseException;
import exceptions.ErrorConnectingServerException;
import exceptions.UserAlreadyExistException;
import interfaces.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.*;
import java.util.ResourceBundle;
import message.Message;
import message.TypeMessage;
import user.User;

/**
 *
 * @author Mikel
 */
public class SignableImplementation implements Signable {

    public SignableImplementation() {

    }


    @Override
    public User signIn(User user) throws UserNotFoundException, ErrorConnectingDatabaseException,
            PasswordMissmatchException, ErrorClosingDatabaseResources, QueryException, ErrorConnectingServerException {
        Message message = new Message(user, TypeMessage.SIGN_IN);
        ServerConnector serverConnector = new ServerConnector(message);
        try {
            serverConnector.start();
            serverConnector.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        message = serverConnector.getMessage();

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
    public User signUp(User user) throws UserAlreadyExistException, ErrorConnectingServerException, ErrorConnectingDatabaseException, QueryException {
        Message message = new Message(user, TypeMessage.SIGN_UP);
        ServerConnector serverConnector = new ServerConnector(message);
        try {
            serverConnector.start();
            serverConnector.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorConnectingServerException();
        }
        message = serverConnector.getMessage();
        switch (message.getType()) {
            case CONNECTION_ERROR:
                throw new ErrorConnectingServerException();
            case USER_EXISTS:
                throw new UserAlreadyExistException(message.getUser());
            case DATABASE_ERROR:
                throw new ErrorConnectingDatabaseException();
            case QUERY_ERROR:
                throw new QueryException();
        }
        return user;
    }

}

class ServerConnector extends Thread {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ResourceBundle rb = ResourceBundle.getBundle("config.config");
    private Socket clientSocket;
    private Message message = null;

    public Message getMessage() {
        return message;
    }

    public ServerConnector(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            this.sendMessage(message);
            this.getMessageFromServer();
            this.stopConnection();
        } catch (ErrorConnectingServerException ex) {
            Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(Message msg) throws ErrorConnectingServerException {
        try {
            clientSocket = new Socket(rb.getString("SOCKET_HOST"), Integer.parseInt(rb.getString("SOCKET_PORT")));
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(msg); // Send message to server
        } catch (IOException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorConnectingServerException();
        }
    }

    public void getMessageFromServer() throws ErrorConnectingServerException {
        try {
            ois = new ObjectInputStream(this.clientSocket.getInputStream());
            message = (Message) ois.readObject(); // Take message from the server
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorConnectingServerException();
        }
    }

    public void stopConnection() throws ErrorConnectingServerException {
        try {
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorConnectingServerException();
        }
    }
}
