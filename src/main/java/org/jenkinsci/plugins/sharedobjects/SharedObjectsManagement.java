package org.jenkinsci.plugins.sharedobjects;

import hudson.Extension;
import hudson.model.ManagementLink;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectsDataStore;
import org.kohsuke.stapler.StaplerProxy;

/**
 * @author Gregory Boissinot
 */
@Extension
public class SharedObjectsManagement extends ManagementLink implements StaplerProxy {

    private SharedObjectsType[] types;

    public SharedObjectsType[] getTypes() {
        return types;
    }

    @SuppressWarnings("unused")
    public SharedObjectsManagement() {
    }

    public SharedObjectsManagement(SharedObjectsType[] types) {
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
        SharedObjectsDataStore store = new SharedObjectsDataStore();
        SharedObjectsType[] types = new SharedObjectsType[0];
        try {
            types = store.readSharedObjectsFile();
        } catch (SharedObjectsException e) {
            e.printStackTrace();
        }
        return new SharedObjectsManagementResult(types);
    }
}
