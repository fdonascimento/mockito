package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArgumentMatcherVerboseTest {

    @Test
    public void toStringEqualsMatcher() {
        Equals equalsMatcher = new Equals(null);
        ArgumentMatcherVerbose matcherVerbose = new ArgumentMatcherVerbose(String.class, equalsMatcher);
        assertEquals("The toString pattern is not correct.", "(String) null", matcherVerbose.toString());
    }

    @Test
    public void toStringNullMatcher() {
        Null nullMatcher = Null.NULL;
        ArgumentMatcherVerbose matcherVerbose = new ArgumentMatcherVerbose(Object.class, nullMatcher);
        assertEquals("The toString pattern is not correct.", "(Object) isNull()", matcherVerbose.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void wantedNullTypeNullThenFailToInstantiateObject() {
        Null nullMatcher = Null.NULL;
        new ArgumentMatcherVerbose(null, nullMatcher);
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentMatcherNullThenFailToInstantiateObject() {
        new ArgumentMatcherVerbose(String.class, null);
    }
}
