package signupsignin.signable;

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
 * This class handles the User object created in the controllers and tries to
 * send a Message object to the server using a thread defined in the class
 * below.
 *
 * @author: Mikel
 */
public class SignableImplementation implements Signable {

    // Empty constructor
    public SignableImplementation() {

    }

    /**
     * Sends the user object to the server and returns the logged in username
     * with all the fields filled.
     *
     * @param user The user object that contains the username and password to
     * login.
     * @return The user that has been logged in.
     * @throws exceptions.UserNotFoundException
     * @throws exceptions.ErrorConnectingDatabaseException
     * @throws exceptions.PasswordMissmatchException
     * @throws exceptions.ErrorClosingDatabaseResources
     * @throws exceptions.QueryException
     * @throws exceptions.ErrorConnectingServerException
     */
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

    /**
     * Sends the user object to the server and inserts a record in the database.
     *
     * @param user The user object that contains the needed info to sign up.
     * @return The user that has been signed up.
     * @throws exceptions.UserAlreadyExistException
     * @throws exceptions.ErrorConnectingDatabaseException
     * @throws exceptions.QueryException
     * @throws exceptions.ErrorConnectingServerException
     */
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

/**
 * Manages the connection with the server. Uses sockets.
 *
 * @author Mikel
 */
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

    /**
     * Sends a message to the server, receives a response message and stops the
     * connection.
     */
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

    /**
     * Send a message to the server.
     *
     * @param msg Message to send to the server via sockets.
     */
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

    /**
     * Receives a response message from the server.
     *
     * @throws ErrorConnectingServerException
     */
    public void getMessageFromServer() throws ErrorConnectingServerException {
        try {
            ois = new ObjectInputStream(this.clientSocket.getInputStream());
            message = (Message) ois.readObject(); // Take message from the server
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorConnectingServerException();
        }
    }

    /**
     * Closes the socket.
     *
     * @throws ErrorConnectingServerException
     */
    public void stopConnection() throws ErrorConnectingServerException {
        try {
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SignableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorConnectingServerException();
        }
    }
}
