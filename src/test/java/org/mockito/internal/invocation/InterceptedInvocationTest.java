package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.invocation.Invocation;

import static org.junit.Assert.assertEquals;

public class InterceptedInvocationTest {

    @Test
    public void toStringWithNullArgument() {
        Invocation invocation = new InvocationBuilder().argTypes(String.class).arg(null).toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.", "iMethods.simpleMethod((String) null);", result);
    }

    @Test
    public void toStringVarargsNull() {
        Invocation invocation = new InvocationBuilder()
            .method("varargs")
            .argTypes(String[].class)
            .args(new Object[]{null})
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.varargs((String[]) null);", result);
    }

    @Test
    public void toStringVarargsWithNullArguments() {
        Invocation invocation = new InvocationBuilder()
            .method("varargs")
            .argTypes(String[].class)
            .args(new Object[]{new String[]{null, null}})
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.varargs([null, null]);", result);
    }

    @Test
    public void toStringVarargsStringAndNull() {
        Invocation invocation = new InvocationBuilder()
            .method("varargs")
            .argTypes(String[].class)
            .args(new Object[]{new String[]{"test", null}})
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.varargs([\"test\", null]);", result);
    }

    @Test
    public void toStringVarargsNonNullArgs() {
        Invocation invocation = new InvocationBuilder()
            .method("varargs")
            .argTypes(String[].class)
            .args(new Object[]{new String[]{"test1", "test2"}})
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.varargs([\"test1\", \"test2\"]);", result);
    }

    @Test
    public void toStringVarargsEmptyArgs() {
        Invocation invocation = new InvocationBuilder()
            .method("varargs")
            .argTypes(String[].class)
            .args(new Object[]{new String[]{}})
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.varargs([]);", result);
    }

    @Test
    public void toStringMixedVarargsNonNullParameters() {
        Invocation invocation = new InvocationBuilder()
            .method("mixedVarargs")
            .argTypes(Object.class, String[].class)
            .args("test1", new String[]{"test2", "test3"})
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.mixedVarargs(\n" +
                "    \"test1\",\n" +
                "    [\"test2\", \"test3\"]\n" +
                ");", result);
    }

    @Test
    public void toStringMixedVarargsNullParameters() {
        Invocation invocation = new InvocationBuilder()
            .method("mixedVarargs")
            .argTypes(Object.class, String[].class)
            .args("test1", new String[]{null, null})
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.mixedVarargs(\"test1\", [null, null]);", result);
    }

    @Test
    public void toStringMixedVarargsNullVarargs() {
        Invocation invocation = new InvocationBuilder()
            .method("mixedVarargs")
            .argTypes(Object.class, String[].class)
            .args("test1", null)
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.mixedVarargs(\n" +
                "    \"test1\",\n" +
                "    (String[]) null\n" +
                ");", result);
    }

    @Test
    public void toStringWithNonNullArgument() {
        Invocation invocation = new InvocationBuilder().argTypes(String.class).arg("test").toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.", "iMethods.simpleMethod(\"test\");", result);
    }

    @Test
    public void toStringWithNullArguments() {
        Invocation invocation = new InvocationBuilder()
            .method("fourArgumentMethod")
            .argTypes(int.class, String.class, String.class, boolean[].class)
            .args(null, null, null, null)
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.fourArgumentMethod(\n" +
                "    (int) null,\n" +
                "    (String) null,\n" +
                "    (String) null,\n" +
                "    (boolean[]) null\n" +
                ");", result);
    }

    @Test
    public void toStringWithNullAndNonNullArguments() {
        Invocation invocation = new InvocationBuilder()
            .method("fourArgumentMethod")
            .argTypes(int.class, String.class, String.class, boolean[].class)
            .args(0, null, "Test", null)
            .toInvocation();

        String result = invocation.toString();
        assertEquals("toString printed incorrectly.",
            "iMethods.fourArgumentMethod(\n    0,\n    (String) null," +
                "\n    \"Test\",\n    (boolean[]) null\n);", result);
    }
}
