package org.jenkinsci.plugins.sharedobjects.type;

import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.model.TaskListener;
import hudson.util.ArgumentListBuilder;
import org.jenkinsci.plugins.sharedobjects.SharedObjectException;
import org.jenkinsci.plugins.sharedobjects.SharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Gregory Boissinot
 */
public class ClearcaseSharedObjectType extends SharedObjectType {

    private String viewName;

    private String elementPath;

    @DataBoundConstructor
    public ClearcaseSharedObjectType(String name, String viewName, String elementPath) {
        this.name = Util.fixEmpty(name);
        this.viewName = Util.fixEmpty(viewName);
        this.elementPath = Util.fixEmpty(elementPath);
    }

    @SuppressWarnings("unused")
    public String getViewName() {
        return viewName;
    }

    @SuppressWarnings("unused")
    public String getElementPath() {
        return elementPath;
    }

    @Override
    public String getEnvVarValue(TaskListener listener) throws SharedObjectException {

        try {
            return runCommandAndReturn(String.format("cleartool setview -exec 'cat %s' %s", elementPath, viewName), listener);
        } catch (IOException ioe) {
            throw new SharedObjectException(ioe);
        } catch (InterruptedException ie) {
            throw new SharedObjectException(ie);
        }
    }

    private String runCommandAndReturn(String command, TaskListener listener) throws IOException, InterruptedException {
        listener.getLogger().println("[SharedObject] - Running the command " + command);
        Launcher launcher = new Launcher.LocalLauncher(listener);
        ArgumentListBuilder cmds = new ArgumentListBuilder();
        cmds.addTokenized(command);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int cmdCode = launcher.launch().cmds(cmds).stdout(outputStream).join();
        if (cmdCode != 0) {
            return new String(outputStream.toByteArray());
        }
        return null;
    }

    @Extension
    public static class ClearcaseSharedObjectTypeDescriptor extends SharedObjectTypeDescriptor {

        @Override
        public String getDisplayName() {
            return "Clearcase Element";
        }

        @Override
        public Class<? extends SharedObjectType> getType() {
            return ClearcaseSharedObjectType.class;
        }
    }

}
