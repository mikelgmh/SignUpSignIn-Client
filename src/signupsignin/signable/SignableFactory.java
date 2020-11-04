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
