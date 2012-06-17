package org.jenkinsci.plugins.sharedobjects.type;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.sharedobjects.SharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectTypeDescriptor;
import org.jenkinsci.plugins.sharedobjects.SimpleSharedObjectType;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Gregory Boissinot
 */
public class PublicFilePathSharedObjectType extends SimpleSharedObjectType {

    private String publicFilePath;

    @DataBoundConstructor
    public PublicFilePathSharedObjectType(String name, String profiles, String publicFilePath) {
        super(name, profiles);
        this.publicFilePath = Util.fixEmptyAndTrim(publicFilePath);
    }

    @SuppressWarnings("unused")
    public String getPublicFilePath() {
        return publicFilePath;
    }

    public String getEnvVarValue(AbstractBuild build, SharedObjectLogger logger) {
        logger.info(String.format("Populating the file path %s associated to the shared object with the name %s.", publicFilePath, name));
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
