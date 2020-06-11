package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public class ArgumentMatcherVerbose implements ArgumentMatcher<Object>, Serializable {

    private ArgumentMatcher argumentMatcher;
    private Class wantedNullType;

    public ArgumentMatcherVerbose(Class wantedNullType, ArgumentMatcher argumentMatcher) {
        if (wantedNullType == null) throw new IllegalArgumentException("wantedNullType cannot be null.");
        if (argumentMatcher == null) throw new IllegalArgumentException("argumentMatcher cannot be null.");

        this.wantedNullType = wantedNullType;
        this.argumentMatcher = argumentMatcher;
    }

    @Override
    public boolean matches(Object argument) {
        return argumentMatcher.matches(argument);
    }

    @Override
    public String toString() {
        return "("+ wantedNullType.getSimpleName() +") " + argumentMatcher.toString();
    }
}
