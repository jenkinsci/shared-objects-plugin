package org.jenkinsci.plugins.sharedobjects;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.model.TaskListener;
import hudson.remoting.Callable;
import org.jenkinsci.lib.envinject.EnvInjectException;
import org.jenkinsci.plugins.envinject.model.EnvInjectJobPropertyContributor;
import org.jenkinsci.plugins.envinject.model.EnvInjectJobPropertyContributorDescriptor;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectDataStore;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class SharedObjectJobProperty extends EnvInjectJobPropertyContributor {

    private boolean populateSharedObjects;

    private String profiles;

    public SharedObjectJobProperty() {
    }

    @DataBoundConstructor
    public SharedObjectJobProperty(boolean populateSharedObjects, String profiles) {
        this.populateSharedObjects = populateSharedObjects;
        this.profiles = Util.fixEmptyAndTrim(profiles);
    }

    @SuppressWarnings("unused")
    public boolean isPopulateSharedObjects() {
        return populateSharedObjects;
    }

    @SuppressWarnings("unused")
    public String getProfiles() {
        return profiles;
    }

    @Override
    public void init() {
        populateSharedObjects = false;
    }

    @Override
    public Map<String, String> getEnvVars(AbstractBuild build, TaskListener listener) throws EnvInjectException {
        SharedObjectLogger logger = new SharedObjectLogger(listener);
        Map<String, String> result = new HashMap<String, String>();
        if (populateSharedObjects) {
            logger.info("Injecting shared objects as environment variables");
            try {
                SharedObjectType[] sharedObjectTypes =
                        Hudson.getInstance().getRootPath().act(new Callable<SharedObjectType[], EnvInjectException>() {
                            public SharedObjectType[] call() throws EnvInjectException {
                                SharedObjectDataStore dataStore = new SharedObjectDataStore();
                                try {
                                    return dataStore.readSharedObjectsFile();
                                } catch (SharedObjectException e) {
                                    throw new EnvInjectException(e);
                                }
                            }
                        });

                if (sharedObjectTypes != null) {

                    boolean restrictionActivated = true;
                    if (profiles == null || profiles.trim().length() == 0) {
                        restrictionActivated = false;
                    }

                    // find out if profile name is a job argument
                    Map<String, String> envVars = build.getEnvironment(listener);
                    String real_profile = Util.replaceMacro(profiles, envVars);

                    if (restrictionActivated) {
                        logger.info(String.format("Restricting shared objects to the following usage %s", real_profile));
                    }

                    for (SharedObjectType type : sharedObjectTypes) {
                        if (type != null) {
                            if (!restrictionActivated) {
                                addSharedObject(result, type, build, logger);
                                continue;
                            }
                            if (restrictionActivated && isProfileActivated(real_profile, type)) {
                                addSharedObject(result, type, build, logger);
                                continue;
                            }
                        }
                    }
                }

            } catch (SharedObjectException se) {
                throw new EnvInjectException(se);
            } catch (IOException e) {
                throw new EnvInjectException(e);
            } catch (InterruptedException e) {
                throw new EnvInjectException(e);
            }
        }
        return result;
    }

    private void addSharedObject(Map<String, String> result, SharedObjectType type, AbstractBuild build, SharedObjectLogger logger) throws SharedObjectException {

        if (type instanceof SimpleSharedObjectType) {
            SimpleSharedObjectType simpleSharedObjectType = (SimpleSharedObjectType) type;
            String value = simpleSharedObjectType.getEnvVarValue(build, logger);
            if (value != null) {
                result.put(simpleSharedObjectType.getName(), value);
            }
            return;
        }

        if (type instanceof MultipleSharedObjectType) {
            MultipleSharedObjectType multipleSharedObjectType = (MultipleSharedObjectType) type;
            Map<String, String> envVars = multipleSharedObjectType.getEnvVarValue(build, logger);
            if (envVars != null) {
                result.putAll(envVars);
            }
            return;
        }

    }

    private boolean isProfileActivated(String profiles, SharedObjectType type) {
        String profilesType = type.getProfiles();
        if (profilesType == null) {
            return true;
        }
        if (profilesType.length() == 0) {
            return true;
        }

        String profilesTypeArray[] = profilesType.split(";");

        List<String> profilesJobs = Arrays.asList(profiles.split(";"));
        for (String profileType : profilesTypeArray) {
            if (profilesJobs.contains(profileType)) {
                return true;
            }
        }
        return false;
    }

    @Extension
    public static class SharedObjectJobPropertyDescriptor extends EnvInjectJobPropertyContributorDescriptor {

        @Override
        public String getDisplayName() {
            return "Populate shared objects";
        }


    }
}
