package org.jenkinsci.plugins.sharedobjects;

import hudson.DescriptorExtensionList;
import hudson.model.Hudson;
import net.sf.json.JSON;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectsDataStore;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static hudson.Functions.checkPermission;

/**
 * @author Gregory Boissinot
 */
public class SharedObjectsManagementResult {

    private SharedObjectsType[] types;

    public SharedObjectsManagementResult(SharedObjectsType[] types) {
        this.types = types;
    }

    @SuppressWarnings("unused")
    public SharedObjectsType[] getTypes() {
        return types;
    }

    @SuppressWarnings("unchecked")
    public DescriptorExtensionList getListSharedObjectsDescriptors() {
        return DescriptorExtensionList.createDescriptorList(Hudson.getInstance(), SharedObjectsType.class);
    }

    @SuppressWarnings("unused")
    public void doSaveConfig(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {
        checkPermission(Hudson.ADMINISTER);

        JSONObject submittedForm = req.getSubmittedForm();

        JSON typesJSON;
        try {
            typesJSON = submittedForm.getJSONArray("types");
        } catch (JSONException jsone) {
            typesJSON = submittedForm.getJSONObject("types");
        }

        List<SharedObjectsType> types = req.bindJSONToList(SharedObjectsType.class, typesJSON);
        SharedObjectsType[] typesArray = types.toArray(new SharedObjectsType[types.size()]);

        SharedObjectsDataStore store = new SharedObjectsDataStore();
        try {
            store.writeSharedObjectsFile(typesArray);
        } catch (SharedObjectsException e) {
            e.printStackTrace();
        }

        rsp.sendRedirect2("/manage");
    }

}
