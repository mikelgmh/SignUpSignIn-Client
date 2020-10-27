/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signable;

import interfaces.Signable;

/**
 *
 * @author Mikel
 */
public class SignableFactory {

    public Signable getSignableImplementation(String implementation) {
        Signable signable = null;
        switch (implementation) {
            case "CLIENT_SIGNABLE":
                signable = new SignableImplementation();
                break;
        }
        return signable;
    }
}
