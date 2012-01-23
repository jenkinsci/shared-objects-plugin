package org.jenkinsci.plugins.sharedobjects;

/**
 * @author Gregory Boissinot
 */
public class SharedObjectException extends Exception {

    public SharedObjectException() {
    }

    public SharedObjectException(String s) {
        super(s);
    }

    public SharedObjectException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SharedObjectException(Throwable throwable) {
        super(throwable);
    }
}
