package org.jenkinsci.plugins.sharedobjects;

import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;

import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public abstract class MultipleSharedObjectType extends SharedObjectType {

    protected MultipleSharedObjectType(String name, String profiles) {
        super(name, profiles);
    }

    public abstract Map<String, String> getEnvVarValue(AbstractBuild build, SharedObjectLogger logger) throws SharedObjectException;
}
