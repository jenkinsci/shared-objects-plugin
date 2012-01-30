package org.jenkinsci.plugins.sharedobjects.type;

import hudson.Extension;
import hudson.Util;
import org.jenkinsci.plugins.sharedobjects.SharedObjectException;
import org.jenkinsci.plugins.sharedobjects.SharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectTypeDescriptor;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Gregory Boissinot
 */
public class StringValueSharedObjectType extends SharedObjectType {

    private String value;

    @DataBoundConstructor
    public StringValueSharedObjectType(String name, String profile, String value) {
        this.name = Util.fixEmpty(name);
        this.profile = Util.fixEmpty(profile);
        this.value = Util.fixEmpty(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getEnvVarValue(SharedObjectLogger logger) throws SharedObjectException {
        return value;
    }

    @Extension
    public static class StringValueSharedObjectTypeDescriptor extends SharedObjectTypeDescriptor {

        @Override
        public String getDisplayName() {
            return "Simple value";
        }

        @Override
        public Class<? extends SharedObjectType> getType() {
            return ClearcaseSharedObjectType.class;
        }
    }
}
