package org.jenkinsci.plugins.sharedobjects;

import hudson.model.Descriptor;
import hudson.model.Hudson;

import java.util.List;

/**
 * @author Gregory Boissinot
 */
public abstract class SharedObjectsTypeDescriptor<T extends SharedObjectsType> extends Descriptor<SharedObjectsType> {

    public static List<SharedObjectsTypeDescriptor> all() {
        return Hudson.getInstance().getExtensionList(SharedObjectsTypeDescriptor.class);
    }

    public abstract Class<? extends SharedObjectsType> getType();
}
