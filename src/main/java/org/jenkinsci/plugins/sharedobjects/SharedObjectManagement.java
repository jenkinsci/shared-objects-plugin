package org.jenkinsci.plugins.sharedobjects;

import hudson.Extension;
import hudson.model.ManagementLink;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectDataStore;
import org.kohsuke.stapler.StaplerProxy;

/**
 * @author Gregory Boissinot
 */
@Extension
public class SharedObjectManagement extends ManagementLink implements StaplerProxy {

    private SharedObjectType[] types;

    public SharedObjectType[] getTypes() {
        return types;
    }

    @SuppressWarnings("unused")
    public SharedObjectManagement() {
    }

    public SharedObjectManagement(SharedObjectType[] types) {
        this.types = types;
    }

    @Override
    public String getIconFileName() {
        return "document.gif";
    }

    public String getDisplayName() {
        return "Shared Objects";
    }

    @Override
    public String getUrlName() {
        return "sharedObjects";
    }

    public Object getTarget() {
        SharedObjectDataStore store = new SharedObjectDataStore();
        SharedObjectType[] types = new SharedObjectType[0];
        try {
            types = store.readSharedObjectsFile();
        } catch (SharedObjectException e) {
            e.printStackTrace();
        }
        return new SharedObjectManagementResult(types);
    }
}
