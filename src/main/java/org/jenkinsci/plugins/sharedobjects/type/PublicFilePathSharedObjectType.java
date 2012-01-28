package org.jenkinsci.plugins.sharedobjects.type;

import hudson.Extension;
import hudson.Util;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.sharedobjects.SharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Gregory Boissinot
 */
public class PublicFilePathSharedObjectType extends SharedObjectType {

    private String publicFilePath;

    @DataBoundConstructor
    public PublicFilePathSharedObjectType(String name, String publicFilePath) {
        this.name = Util.fixEmpty(name);
        this.publicFilePath = Util.fixEmpty(publicFilePath);
    }

    @SuppressWarnings("unused")
    public String getPublicFilePath() {
        return publicFilePath;
    }

    public String getEnvVarValue(TaskListener listener) {
        //TODO Resolve variables
        return publicFilePath;
    }

    @Extension
    public static class PublicFilePathSharedObjectTypeDescriptor extends SharedObjectTypeDescriptor {

        @Override
        public String getDisplayName() {
            return "Public File Path";
        }

        @Override
        public Class<? extends SharedObjectType> getType() {
            return PublicFilePathSharedObjectType.class;
        }
    }
}
