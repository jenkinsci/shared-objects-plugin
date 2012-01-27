package org.jenkinsci.plugins.sharedobjects.service;

import hudson.model.Hudson;
import hudson.util.XStream2;
import org.jenkinsci.plugins.sharedobjects.SharedObjectException;
import org.jenkinsci.plugins.sharedobjects.SharedObjectType;
import org.jenkinsci.plugins.sharedobjects.SharedObjectsManagement;

import java.io.*;

/**
 * @author Gregory Boissinot
 */
public class SharedObjectsDataStore {

    public void writeSharedObjectsFile(SharedObjectType[] types) throws SharedObjectException {
        XStream2 xStream2 = new XStream2();
        File sharedObjectsFile = new File(Hudson.getInstance().getRootDir(), "sharedObjects.xml");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(sharedObjectsFile, false);
            xStream2.toXML(new SharedObjectsManagement(types), fileWriter);
        } catch (IOException e) {
            throw new SharedObjectException(e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new SharedObjectException(e);
                }
            }
        }
    }

    public SharedObjectType[] readSharedObjectsFile() throws SharedObjectException {
        XStream2 xStream2 = new XStream2();
        File sharedObjectsFile = new File(Hudson.getInstance().getRootDir(), "sharedObjects.xml");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(sharedObjectsFile);
            if (sharedObjectsFile.exists()) {
                SharedObjectsManagement sharedObjectsManagement = (SharedObjectsManagement) xStream2.fromXML(fileReader);
                return sharedObjectsManagement.getTypes();
            }
        } catch (FileNotFoundException e) {
            throw new SharedObjectException(e);
        } catch (IOException e) {
            throw new SharedObjectException(e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    throw new SharedObjectException(e);
                }
            }

        }
        return new SharedObjectType[0];
    }

}
