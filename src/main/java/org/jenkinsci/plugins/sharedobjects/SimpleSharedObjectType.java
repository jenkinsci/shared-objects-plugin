package org.jenkinsci.plugins.sharedobjects;

import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;

/**
 * @author Gregory Boissinot
 */
public abstract class SimpleSharedObjectType extends SharedObjectType{

    protected SimpleSharedObjectType(String name, String profiles) {
        super(name, profiles);
    }

    public abstract String getEnvVarValue(AbstractBuild build, SharedObjectLogger logger) throws SharedObjectException ;
}
