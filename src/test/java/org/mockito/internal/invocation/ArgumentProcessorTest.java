package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.matchers.ArgumentMatcherVerbose;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockitousage.IMethods;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ArgumentProcessorTest {

    @Test
    public void putVarArgsInsideAnArrayOneArgThenNoModification() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("simpleMethod", String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object[] arguments = new Object[]{"arg"};

        Object[] result = ArgumentsProcessor.putVarArgsInsideAnArray(delegatingMethod, arguments);
        assertThat(result).describedAs("Non var args arguments shouldn't be modified").isEqualTo(arguments);
    }

    @Test
    public void putVarArgsInsideAnArrayWithTwoArgsThenNoModification() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("simpleMethod", String.class, Integer.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object[] arguments = new Object[]{"arg", 1};

        Object[] result = ArgumentsProcessor.putVarArgsInsideAnArray(delegatingMethod, arguments);
        assertThat(result).describedAs("Non var args arguments shouldn't be modified").isEqualTo(arguments);
    }

    @Test
    public void putVarArgsInsideAnArrayWithSingleArrayThenNoModification() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("oneArray", int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object[] arguments = new Object[]{new int[]{1, 2}};

        Object[] result = ArgumentsProcessor.putVarArgsInsideAnArray(delegatingMethod, arguments);
        assertThat(result).describedAs("Non var args arguments shouldn't be modified").isEqualTo(arguments);
    }

    @Test
    public void putVarArgsInsideAnArrayWithStrinAndArrayThenNoModification() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("simpleMethod", String.class, String[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object[] arguments = new Object[]{"string", new String[]{"array1", "array2"}};

        Object[] result = ArgumentsProcessor.putVarArgsInsideAnArray(delegatingMethod, arguments);
        assertThat(result).describedAs("Non var args arguments shouldn't be modified").isEqualTo(arguments);
    }

    @Test
    public void putVarArgsInsideAnArrayWith1ArgVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"arg"};
        Object[] expected = new Object[]{new String[]{"arg"}};

        putVarArgsInsideAnArrayWhenThereIsOnlyVarArgs(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWith2ArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Object[] expected = new Object[]{new String[]{"arg1", "arg2"}};

        putVarArgsInsideAnArrayWhenThereIsOnlyVarArgs(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWithEmptyVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{};
        Object[] expected = new Object[]{new String[]{}};

        putVarArgsInsideAnArrayWhenThereIsOnlyVarArgs(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWithNullVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{null};
        Object[] expected = new Object[]{null};

        putVarArgsInsideAnArrayWhenThereIsOnlyVarArgs(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWithStringAnd1EmptyVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String"};
        Object[] expected = new Object[]{"String", new String[]{}};

        putVarArgsInsideAnArrayWithMixedVarArgs(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWithStringAnd1ArgVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String", "arg1"};
        Object[] expected = new Object[]{"String", new String[]{"arg1"}};

        putVarArgsInsideAnArrayWithMixedVarArgs(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWithStringAnd2ArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String", "arg1", "arg2"};
        Object[] expected = new Object[]{"String", new String[]{"arg1", "arg2"}};

        putVarArgsInsideAnArrayWithMixedVarArgs(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWithStringAnd2NullArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String", null, null};
        Object[] expected = new Object[]{"String", new String[]{null, null}};

        putVarArgsInsideAnArrayWithMixedVarArgs(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWithStringAndNullVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String", null};
        Object[] expected = new Object[]{"String", null};

        putVarArgsInsideAnArrayWithMixedVarArgs(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWith2StringsAndEmptyVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2"};
        Object[] expected = new Object[]{"String1", "String2", new String[]{}};

        putVarArgsInsideAnArrayWithMixedVarArgsWith3Params(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWith2StringsAnd1ArgVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2", "arg1"};
        Object[] expected = new Object[]{"String1", "String2", new String[]{"arg1"}};

        putVarArgsInsideAnArrayWithMixedVarArgsWith3Params(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWith2StringsAnd2ArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2", "arg1", "arg2"};
        Object[] expected = new Object[]{"String1", "String2", new String[]{"arg1", "arg2"}};

        putVarArgsInsideAnArrayWithMixedVarArgsWith3Params(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWith2StringsAnd2NullArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2", null, null};
        Object[] expected = new Object[]{"String1", "String2", new String[]{null, null}};

        putVarArgsInsideAnArrayWithMixedVarArgsWith3Params(arguments, expected);
    }

    @Test
    public void putVarArgsInsideAnArrayWith2StringsAndNullVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2", null};
        Object[] expected = new Object[]{"String1", "String2", null};

        putVarArgsInsideAnArrayWithMixedVarArgsWith3Params(arguments, expected);
    }

    @Test
    public void argumentsToMatchersWithNullTypeSingleArg() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"arg"};
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("simpleMethod", String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 1 element.").hasSize(1);
        assertThat(argumentMatchers.get(0)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithTwoArgs() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("simpleMethod", String.class, Integer.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object[] arguments = new Object[]{"arg", 1};

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 2 element.").hasSize(2);
        assertThat(argumentMatchers.get(0)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
        assertThat(argumentMatchers.get(1)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithNullArg() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("simpleMethod", String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object[] arguments = new Object[]{null};

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 1 element.").hasSize(1);
        assertThat(argumentMatchers.get(0)).describedAs("An ArgumentMatcherVerbose matcher should be created.")
            .isInstanceOf(ArgumentMatcherVerbose.class);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithSingleArray() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("oneArray", int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object[] arguments = new Object[]{new int[]{1, 2}};

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 1 element.").hasSize(1);
        assertThat(argumentMatchers.get(0)).describedAs("An ArrayEquals matcher should be created.")
            .isInstanceOf(ArrayEquals.class);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithStrinAndArray() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("simpleMethod", String.class, String[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object[] arguments = new Object[]{"string", new String[]{"array1", "array2"}};

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 2 elements.").hasSize(2);
        assertThat(argumentMatchers.get(0)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
        assertThat(argumentMatchers.get(1)).describedAs("An ArrayEquals matcher should be created.")
            .isInstanceOf(ArrayEquals.class);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWith1ArgVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"arg"};

        argumentsToMatchersWithNullTypeWithVarArgs(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWith2ArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"arg1", "arg2"};

        argumentsToMatchersWithNullTypeWithVarArgs(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithEmptyVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{};

        argumentsToMatchersWithNullTypeWithVarArgs(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithNullVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{null};

        DelegatingMethod delegatingMethod = getVarargsMethod();

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 1 element.").hasSize(1);
        assertThat(argumentMatchers.get(0)).describedAs("An ArgumentMatcherVerbose matcher should be created.")
            .isInstanceOf(ArgumentMatcherVerbose.class);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithStringAnd1EmptyVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String"};

        argumentsToMatchersWithNullTypeWithMixedVarArgs(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithStringAnd1ArgVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String", "arg1"};

        argumentsToMatchersWithNullTypeWithMixedVarArgs(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithStringAnd2ArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String", "arg1", "arg2"};

        argumentsToMatchersWithNullTypeWithMixedVarArgs(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithStringAnd2NullArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String", null, null};

        argumentsToMatchersWithNullTypeWithMixedVarArgs(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWithStringAndNullVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String", null};

        DelegatingMethod delegatingMethod = getMixedVarargsMethod();

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 2 elements.").hasSize(2);
        assertThat(argumentMatchers.get(0)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
        assertThat(argumentMatchers.get(1)).describedAs("An ArgumentMatcherVerbose matcher should be created.")
            .isInstanceOf(ArgumentMatcherVerbose.class);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWith2StringsAndEmptyVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2"};

        argumentsToMatchersWithNullTypeMixedVarargs3Params(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWith2StringsAnd1ArgVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2", "arg1"};

        argumentsToMatchersWithNullTypeMixedVarargs3Params(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWith2StringsAnd2ArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2", "arg1", "arg2"};

        argumentsToMatchersWithNullTypeMixedVarargs3Params(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWith2StringsAnd2NullArgsVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2", null, null};

        argumentsToMatchersWithNullTypeMixedVarargs3Params(arguments);
    }

    @Test
    public void argumentsToMatchersWithNullTypeWith2StringsAndNullVarArgs() throws NoSuchMethodException {
        Object[] arguments = new Object[]{"String1", "String2", null};

        DelegatingMethod delegatingMethod = getMixedVarargsWith3ParamsMethod();

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 3 elements.").hasSize(3);
        assertThat(argumentMatchers.get(0)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
        assertThat(argumentMatchers.get(1)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
        assertThat(argumentMatchers.get(2)).describedAs("An ArgumentMatcherVerbose matcher should be created.")
            .isInstanceOf(ArgumentMatcherVerbose.class);
    }

    private void putVarArgsInsideAnArrayWhenThereIsOnlyVarArgs(Object[] arguments, Object expected) throws NoSuchMethodException {
        DelegatingMethod delegatingMethod = getVarargsMethod();

        Object[] result = ArgumentsProcessor.putVarArgsInsideAnArray(delegatingMethod, arguments);
        assertThat(result).describedAs("Varargs should be in an array").isEqualTo(expected);
    }

    private void putVarArgsInsideAnArrayWithMixedVarArgs(Object[] arguments, Object expected) throws NoSuchMethodException {
        DelegatingMethod delegatingMethod = getMixedVarargsMethod();

        Object[] result = ArgumentsProcessor.putVarArgsInsideAnArray(delegatingMethod, arguments);
        assertThat(result).describedAs("Varargs should be in an array and non-varargs should not be modified")
            .isEqualTo(expected);
    }

    private void putVarArgsInsideAnArrayWithMixedVarArgsWith3Params(Object[] arguments, Object expected) throws NoSuchMethodException {
        DelegatingMethod delegatingMethod = getMixedVarargsWith3ParamsMethod();

        Object[] result = ArgumentsProcessor.putVarArgsInsideAnArray(delegatingMethod, arguments);
        assertThat(result).describedAs("Varargs should be in an array and non-varargs should not be modified")
            .isEqualTo(expected);
    }

    private void argumentsToMatchersWithNullTypeMixedVarargs3Params(Object[] arguments) throws NoSuchMethodException {
        DelegatingMethod delegatingMethod = getMixedVarargsWith3ParamsMethod();

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 3 elements.").hasSize(3);
        assertThat(argumentMatchers.get(0)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
        assertThat(argumentMatchers.get(1)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
        assertThat(argumentMatchers.get(2)).describedAs("An ArrayEquals matcher should be created.")
            .isInstanceOf(ArrayEquals.class);
    }

    private void argumentsToMatchersWithNullTypeWithMixedVarArgs(Object[] arguments) throws NoSuchMethodException {
        DelegatingMethod delegatingMethod = getMixedVarargsMethod();

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 2 elements.").hasSize(2);
        assertThat(argumentMatchers.get(0)).describedAs("An Equals matcher should be created.")
            .isInstanceOf(Equals.class);
        assertThat(argumentMatchers.get(1)).describedAs("An ArrayEquals matcher should be created.")
            .isInstanceOf(ArrayEquals.class);
    }

    private void argumentsToMatchersWithNullTypeWithVarArgs(Object[] arguments) throws NoSuchMethodException {
        DelegatingMethod delegatingMethod = getVarargsMethod();

        List<ArgumentMatcher> argumentMatchers = ArgumentsProcessor.argumentsToMatchersWithNullType(delegatingMethod, arguments);
        assertThat(argumentMatchers).describedAs("argumentMatchers should not be null.").isNotNull();
        assertThat(argumentMatchers).describedAs("argumentMatchers should have 1 element.").hasSize(1);
        assertThat(argumentMatchers.get(0)).describedAs("An ArrayEquals matcher should be created.")
            .isInstanceOf(ArrayEquals.class);
    }

    private DelegatingMethod getMixedVarargsWith3ParamsMethod() throws NoSuchMethodException {
        VarArgsExtension varArgsExtension = mock(VarArgsExtension.class);
        Method method = varArgsExtension.getClass().getMethod("mixedVarargsWith3Params",
            Object.class, Object.class, String[].class);
        return new DelegatingMethod(method);
    }

    private DelegatingMethod getMixedVarargsMethod() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("mixedVarargs", Object.class, String[].class);
        return new DelegatingMethod(method);
    }

    private DelegatingMethod getVarargsMethod() throws NoSuchMethodException {
        IMethods iMethods = mock(IMethods.class);
        Method method = iMethods.getClass().getMethod("varargs", String[].class);
        return new DelegatingMethod(method);
    }
}

interface VarArgsExtension {
    void mixedVarargsWith3Params(Object arg1, Object arg2, String ... string);
}
