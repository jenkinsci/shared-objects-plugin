package org.jenkinsci.plugins.sharedobjects;

import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;

import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public abstract class SharedObjectsType implements ExtensionPoint, Describable<SharedObjectsType>, Serializable {

    protected String name;

    @Override
    public Descriptor<SharedObjectsType> getDescriptor() {
        return (SharedObjectsTypeDescriptor) Hudson.getInstance().getDescriptor(getClass());
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    public abstract String getEnvVarValue();
}
