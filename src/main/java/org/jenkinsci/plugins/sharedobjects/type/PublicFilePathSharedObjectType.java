package org.jenkinsci.plugins.sharedobjects.type;

import hudson.Extension;
import org.jenkinsci.plugins.sharedobjects.SharedObjectsType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectsTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Gregory Boissinot
 */
public class PublicFilePathSharedObjectType extends SharedObjectsType {

    private String publicFilePath;

    @DataBoundConstructor
    public PublicFilePathSharedObjectType(String name, String publicFilePath) {
        this.name = name;
        this.publicFilePath = publicFilePath;
    }

    @SuppressWarnings("unused")
    public String getPublicFilePath() {
        return publicFilePath;
    }

    public String getEnvVarValue() {
        //TODO Resolve variables
        return publicFilePath;
    }

    @Extension
    public static class PublicFilePathSharedObjectTypeDescriptor extends SharedObjectsTypeDescriptor {

        @Override
        public String getDisplayName() {
            return "Public File Path";
        }

        @Override
        public Class<? extends SharedObjectsType> getType() {
            return PublicFilePathSharedObjectType.class;
        }
    }
}
