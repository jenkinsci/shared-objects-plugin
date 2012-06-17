package org.jenkinsci.plugins.sharedobjects.type;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.sharedobjects.SharedObjectException;
import org.jenkinsci.plugins.sharedobjects.SharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectTypeDescriptor;
import org.jenkinsci.plugins.sharedobjects.SimpleSharedObjectType;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Gregory Boissinot
 */
public class StringValueSharedObjectType extends SimpleSharedObjectType {

    private String value;

    @DataBoundConstructor
    public StringValueSharedObjectType(String name, String profiles, String value) {
        super(name, profiles);
        this.value = Util.fixEmptyAndTrim(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getEnvVarValue(AbstractBuild build, SharedObjectLogger logger) throws SharedObjectException {
        logger.info(String.format("Populating a string value to the shared object with the name %s.", name));
        return value;
    }

    @Extension
    public static class StringValueSharedObjectTypeDescriptor extends SharedObjectTypeDescriptor {

        @Override
        public String getDisplayName() {
            return "Simple content";
        }

        @Override
        public Class<? extends SharedObjectType> getType() {
            return StringValueSharedObjectType.class;
        }
    }
}
