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
    public User signIn(User user) throws ErrorConnectingServerException {
        Message message = new Message(user, TypeMessage.SIGN_IN);
        sendMessage(message);
        stopConnection();
        return user;
    }

    @Override
    public User signUp(User user) throws UserAlreadyExistException, ErrorConnectingServerException, ErrorConnectingDatabaseException {
        Message message = new Message(user, TypeMessage.SIGN_UP);
        message = this.sendMessage(message);
        this.stopConnection();
        this.checkMessageForExceptions(message);
        return user;

    }

    public Message sendMessage(Message msg) throws ErrorConnectingServerException {
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
            throw new ErrorConnectingServerException();
        }
    }

    public void checkMessageForExceptions(Message message) throws UserAlreadyExistException, ErrorConnectingServerException, ErrorConnectingDatabaseException {
        switch (message.getType()) {
            case CONNECTION_ERROR:
                throw new ErrorConnectingServerException();
            case USER_EXISTS:
                throw new UserAlreadyExistException(message.getUser());
            case DATABASE_ERROR:
                throw new ErrorConnectingDatabaseException();

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
