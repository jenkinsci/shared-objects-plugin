package org.jenkinsci.plugins.sharedobjects;

import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.Items;

/**
 * @author Gregory Boissinot
 */
public class AliasInitializer {

    @Initializer(before = InitMilestone.PLUGINS_STARTED)
    @SuppressWarnings("unused")
    public static void addAliases() {
        Items.XSTREAM.alias("SharedObjectsManagement", SharedObjectsManagement.class);
        for (SharedObjectTypeDescriptor descriptor : SharedObjectTypeDescriptor.all()) {
            Class<? extends SharedObjectType> classz = descriptor.getType();
            Items.XSTREAM.alias(getClassName(classz), classz);
        }
    }

    private static String getClassName(Class<? extends SharedObjectType> classType) {
        String name = classType.getName();
        String packageSep = ".";
        if (name.contains(packageSep)) {
            return name.substring(name.lastIndexOf(packageSep) + 1);
        }
        return name;
    }

}
