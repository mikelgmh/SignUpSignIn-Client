/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.signable;

import signupsignin.signable.SignableImplementation;
import interfaces.Signable;

/**
 *
 * @author Aketza
 */
public class SignableFactory {

    public Signable getSignableImplementation() {
       return new SignableImplementation();
        
    }
}
