package org.jenkinsci.plugins.sharedobjects.service;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import org.jenkinsci.plugins.sharedobjects.SharedObjectException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Gregory Boissinot
 */
public class SharedObjectManagementFile {

    public String getTemporaryFilePath(AbstractBuild build, String sharedObjectName, String profiles) throws SharedObjectException {

        if (sharedObjectName == null) {
            throw new SharedObjectException("A shared object name is required.");
        }

        FilePath tmpDir = Hudson.getInstance().getRootPath().child(new File(build.getRootDir(), "/sharedObjects/").getAbsolutePath());
        try {
            tmpDir.mkdirs();
        } catch (IOException e) {
            throw new SharedObjectException(e);
        } catch (InterruptedException e) {
            throw new SharedObjectException(e);
        }
        String tmpFileName = sharedObjectName + ((profiles == null) ? "" : ("-" + Arrays.asList(profiles.split("-")).toString().hashCode()));
        FilePath tmpFile = tmpDir.child("FETCHED_" + tmpFileName);
        return tmpFile.getRemote();
    }
}
