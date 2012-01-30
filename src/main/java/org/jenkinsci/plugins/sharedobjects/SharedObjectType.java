package org.jenkinsci.plugins.sharedobjects;

import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;

import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public abstract class SharedObjectType implements ExtensionPoint, Describable<SharedObjectType>, Serializable {

    protected String name;

    protected String profile;

    @Override
    public Descriptor<SharedObjectType> getDescriptor() {
        return (SharedObjectTypeDescriptor) Hudson.getInstance().getDescriptor(getClass());
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public abstract String getEnvVarValue(SharedObjectLogger logger) throws SharedObjectException;
}
