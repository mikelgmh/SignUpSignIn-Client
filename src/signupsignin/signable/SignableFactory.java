/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.signable;

import interfaces.Signable;

/**
 *
 * @author Aketza
 */
public class SignableFactory {

    /**
     * Gets a signable implementation.
     *
     * @return An implementation of type Signable.
     */
    public Signable getSignableImplementation() {
        return new SignableImplementation();
    }
}
