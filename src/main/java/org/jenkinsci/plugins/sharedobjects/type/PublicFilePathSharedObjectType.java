package org.jenkinsci.plugins.sharedobjects.type;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.sharedobjects.SharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectTypeDescriptor;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Gregory Boissinot
 */
public class PublicFilePathSharedObjectType extends SharedObjectType {

    private String publicFilePath;

    @DataBoundConstructor
    public PublicFilePathSharedObjectType(String name, String profiles, String publicFilePath) {
        this.name = Util.fixEmptyAndTrim(name);
        this.profiles = Util.fixEmptyAndTrim(profiles);
        this.publicFilePath = Util.fixEmptyAndTrim(publicFilePath);
    }

    @SuppressWarnings("unused")
    public String getPublicFilePath() {
        return publicFilePath;
    }

    public String getEnvVarValue(AbstractBuild build, SharedObjectLogger logger) {
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
