package org.jenkinsci.plugins.sharedobjects;

import hudson.ExtensionPoint;
import hudson.Util;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;

import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public abstract class SharedObjectType implements ExtensionPoint, Describable<SharedObjectType>, Serializable {

    protected String name;

    protected transient String profile;

    protected String profiles;

    protected SharedObjectType(String name, String profiles) {
        this.name = Util.fixEmptyAndTrim(name);
        this.profiles = Util.fixEmptyAndTrim(profiles);
    }

    @Override
    public Descriptor<SharedObjectType> getDescriptor() {
        return (SharedObjectTypeDescriptor) Hudson.getInstance().getDescriptor(getClass());
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    public String getProfiles() {
        if (profile != null) {
            profiles = profile;
        }
        return profiles;
    }
}
