package org.jenkinsci.plugins.sharedobjects.type;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import org.apache.commons.io.FileUtils;
import org.jenkinsci.plugins.sharedobjects.MultipleSharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectException;
import org.jenkinsci.plugins.sharedobjects.SharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectTypeDescriptor;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Gregory Boissinot
 */
public class FileURLPropertiesSharedObjectType extends MultipleSharedObjectType {

    private String propertiesFile;

    @DataBoundConstructor
    public FileURLPropertiesSharedObjectType(String name, String profiles, String propertiesFile) {
        super(name, profiles);
        this.propertiesFile = Util.fixEmptyAndTrim(propertiesFile);
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    @Override
    public Map<String, String> getEnvVars(AbstractBuild build, SharedObjectLogger logger) throws SharedObjectException {

        logger.info(String.format("Trying to retrieve a properties file through the url value %s associated to the shared object with the name %s.", propertiesFile, name));

        if (propertiesFile == null) {
            return null;
        }

        File file = new File(propertiesFile);
        if (!file.exists()) {
            logger.error(String.format("The file %s doesn't exist.", file));
        }

        try {
            return loadProperties(FileUtils.readFileToString(file));
        } catch (IOException ioe) {
            throw new SharedObjectException(ioe);
        }
    }


    private Map<String, String> loadProperties(String content) throws SharedObjectException {

        Map<String, String> result = new HashMap<String, String>();

        Properties properties = new Properties();
        StringReader stringReader = new StringReader(content);
        try {
            properties.load(stringReader);
        } catch (IOException ioe) {
            throw new SharedObjectException(ioe);
        }
        stringReader.close();

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            result.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }

        return result;

    }

    @Extension
    public static class FileURLPropertiesSharedObjectTypeDescriptor extends SharedObjectTypeDescriptor {

        @Override
        public String getDisplayName() {
            return "A public file path to a properties file";
        }

        @Override
        public Class<? extends SharedObjectType> getType() {
            return FileURLPropertiesSharedObjectType.class;
        }
    }
}
