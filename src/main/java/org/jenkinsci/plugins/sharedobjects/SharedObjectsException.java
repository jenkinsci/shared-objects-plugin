package org.jenkinsci.plugins.sharedobjects;

/**
 * @author Gregory Boissinot
 */
public class SharedObjectsException extends Exception {

    public SharedObjectsException() {
    }

    public SharedObjectsException(String s) {
        super(s);
    }

    public SharedObjectsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SharedObjectsException(Throwable throwable) {
        super(throwable);
    }
}
