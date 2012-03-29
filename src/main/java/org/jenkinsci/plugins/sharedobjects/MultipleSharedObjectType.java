package org.jenkinsci.plugins.sharedobjects;

import hudson.Util;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.sharedobjects.service.SharedObjectLogger;

import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public abstract class MultipleSharedObjectType extends SharedObjectType {

    protected MultipleSharedObjectType(String name, String profiles) {
        super(name, profiles);
    }

    public Map<String, String> getEnvVarValue(AbstractBuild build, SharedObjectLogger logger) throws SharedObjectException {
        Map<String, String> result = getEnvVars(build, logger);
        resolveVars(result, result);
        return result;
    }

    public abstract Map<String, String> getEnvVars(AbstractBuild build, SharedObjectLogger logger) throws SharedObjectException;

    protected void resolveVars(Map<String, String> variables, Map<String, String> env) {

        //Resolve variables against env
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String value = Util.replaceMacro(entry.getValue(), env);
            entry.setValue(value);
        }

        //Resolve variables against variables itself
        boolean stopToResolveVars = false;
        int nbUnresolvedVar = 0;

        while (!stopToResolveVars) {
            int previousNbUnresolvedVar = nbUnresolvedVar;
            nbUnresolvedVar = 0;
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                String value = Util.replaceMacro(entry.getValue(), variables);
                entry.setValue(value);
                if (isUnresolvedVar(value)) {
                    nbUnresolvedVar++;
                }
            }
            if (previousNbUnresolvedVar == nbUnresolvedVar) {
                stopToResolveVars = true;
            }
        }
    }

    private boolean isUnresolvedVar(String value) {
        return value != null && value.contains("$") && !value.contains("\\$");
    }
}
