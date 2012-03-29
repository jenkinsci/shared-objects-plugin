package org.jenkinsci.plugins.sharedobjects.service;

import hudson.model.TaskListener;

import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public class SharedObjectLogger implements Serializable {

    private TaskListener listener;

    public SharedObjectLogger(TaskListener listener) {
        this.listener = listener;
    }

    public TaskListener getListener() {
        return listener;
    }

    public void info(String message) {
        listener.getLogger().println("[SharedObjects] - " + message);
    }

    public void error(String message) {
        listener.getLogger().println("[SharedObjects] - [ERROR] - " + message);
    }
}

