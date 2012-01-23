package org.jenkinsci.plugins.sharedobjects;

import hudson.model.Descriptor;
import hudson.model.Hudson;

import java.util.List;

/**
 * @author Gregory Boissinot
 */
public abstract class SharedObjectTypeDescriptor<T extends SharedObjectType> extends Descriptor<SharedObjectType> {

    public static List<SharedObjectTypeDescriptor> all() {
        return Hudson.getInstance().getExtensionList(SharedObjectTypeDescriptor.class);
    }

    public abstract Class<? extends SharedObjectType> getType();
}
