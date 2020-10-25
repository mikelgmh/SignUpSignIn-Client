/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.signable;

import interfaces.Signable;
import user.User;
import message.Message;
import message.TypeMessage;

/**
 *
 * @author Mikel
 */
public class SignableImplementation implements Signable {

    @Override
    public User signIn(User user) {
        //Creamos el mensaje con los parametros
        Message message = new Message(user, TypeMessage.SIGN_IN);
        //Al final del todo le devolvemos los datos del User al usuario.
        return user;
    }

    @Override
    public User signUp(User user) {
        //Creamos el mensaje con los parametros
        Message message = new Message(user, TypeMessage.SIGN_UP);
        //Al final del todo le devolvemos los datos del User al usuario.
        return user;
    }
}
