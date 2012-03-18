package org.jenkinsci.plugins.sharedobjects.type;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.sharedobjects.MultipleSharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectException;
import org.jenkinsci.plugins.sharedobjects.SharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectTypeDescriptor;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Gregory Boissinot
 */
public class URLPropertiesSharedObjectType extends MultipleSharedObjectType {

    private String url;

    @DataBoundConstructor
    public URLPropertiesSharedObjectType(String name, String profiles, String url) {
        super(Util.fixEmptyAndTrim(name), Util.fixEmptyAndTrim(profiles));
        this.url = Util.fixEmpty(url);
    }

    @SuppressWarnings("unused")
    public String getUrl() {
        return url;
    }

    @Override
    public Map<String, String> getEnvVarValue(AbstractBuild build, SharedObjectLogger logger) throws SharedObjectException {

        logger.info(String.format("Trying to retrieve a properties file through the url value %s associated to the shared object with the name %s.", url, name));

        if (url == null) {
            return null;
        }

        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        /* Set a connect and read timeout. If this hangs, it can actually
           take down all of the jenkins schedule events.
           This is 5 minutes expressed as milliseconds. */
        client.setConnectTimeout(300000);
        client.setReadTimeout(300000);
        ClientResponse clientResponse = client.resource(url).get(ClientResponse.class);
        String propertiesContent = clientResponse.getEntity(String.class);
        if (propertiesContent == null) {
            return null;
        }
        return loadProperties(propertiesContent);
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
    public static class URLPropertiesSharedObjectTypeDescriptor extends SharedObjectTypeDescriptor {

        @Override
        public String getDisplayName() {
            return "A URL to a properties file";
        }

        @Override
        public Class<? extends SharedObjectType> getType() {
            return URLPropertiesSharedObjectType.class;
        }
    }
}
