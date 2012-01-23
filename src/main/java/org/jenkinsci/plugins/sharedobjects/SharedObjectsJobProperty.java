package org.jenkinsci.plugins.sharedobjects;

import hudson.Extension;
import hudson.model.TaskListener;
import org.jenkinsci.lib.envinject.EnvInjectException;
import org.jenkinsci.plugins.envinject.model.EnvInjectJobPropertyContributor;
import org.jenkinsci.plugins.envinject.model.EnvInjectJobPropertyContributorDescriptor;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectsDataStore;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class SharedObjectsJobProperty extends EnvInjectJobPropertyContributor {

    private boolean populateSharedObjects;

    public SharedObjectsJobProperty() {
    }

    @DataBoundConstructor
    public SharedObjectsJobProperty(boolean populateSharedObjects) {
        this.populateSharedObjects = populateSharedObjects;
    }

    public boolean getPopulateSharedObjects() {
        return populateSharedObjects;
    }

    @Override
    public void init() {
        populateSharedObjects = true;
    }

    @Override
    public Map<String, String> getEnvVars(TaskListener listener) throws EnvInjectException {
        Map<String, String> result = new HashMap<String, String>();
        if (populateSharedObjects) {
            SharedObjectsDataStore dataStore = new SharedObjectsDataStore();
            try {
                SharedObjectType[] sharedObjectTypes = dataStore.readSharedObjectsFile();
                for (SharedObjectType type : sharedObjectTypes) {
                    result.put(type.getName(), type.getEnvVarValue());
                }

            } catch (SharedObjectException se) {
                throw new EnvInjectException(se);
            }
        }
        return result;
    }

    @Extension
    public static class SharedObjectJobPropertyDescriptor extends EnvInjectJobPropertyContributorDescriptor {

        @Override
        public String getDisplayName() {
            return "Populate Shared Objects";
        }


    }
}
