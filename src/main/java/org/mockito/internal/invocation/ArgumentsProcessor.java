/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ArgumentMatcherVerbose;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * by Szczepan Faber, created at: 3/31/12
 */
public class ArgumentsProcessor {
    // drops hidden synthetic parameters (last continuation parameter from Kotlin suspending functions)
    // and expands varargs
    public static Object[] expandArgs(MockitoMethod method, Object[] args) {
        int nParams = method.getParameterTypes().length;
        if (args != null && args.length > nParams)
            args = Arrays.copyOf(args, nParams); // drop extra args (currently -- Kotlin continuation synthetic arg)
        return expandVarArgs(method.isVarArgs(), args);
    }

    // expands array varArgs that are given by runtime (1, [a, b]) into true
    // varArgs (1, a, b);
    private static Object[] expandVarArgs(final boolean isVarArgs, final Object[] args) {
        if (!isVarArgs || isNullOrEmpty(args) || args[args.length - 1] != null && !args[args.length - 1].getClass().isArray()) {
            return args == null ? new Object[0] : args;
        }

        final int nonVarArgsCount = args.length - 1;
        Object[] varArgs;
        if (args[nonVarArgsCount] == null) {
            // in case someone deliberately passed null varArg array
            varArgs = new Object[] { null };
        } else {
            varArgs = ArrayEquals.createObjectArray(args[nonVarArgsCount]);
        }
        final int varArgsCount = varArgs.length;
        Object[] newArgs = new Object[nonVarArgsCount + varArgsCount];
        System.arraycopy(args, 0, newArgs, 0, nonVarArgsCount);
        System.arraycopy(varArgs, 0, newArgs, nonVarArgsCount, varArgsCount);
        return newArgs;
    }

    private static <T> boolean isNullOrEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static List<ArgumentMatcher> argumentsToMatchers(Object[] arguments) {
        List<ArgumentMatcher> matchers = new ArrayList<ArgumentMatcher>(arguments.length);
        for (Object arg : arguments) {
            if (arg != null && arg.getClass().isArray()) {
                matchers.add(new ArrayEquals(arg));
            } else {
                matchers.add(new Equals(arg));
            }
        }
        return matchers;
    }

    public static Object[] putVarArgsInsideAnArray(MockitoMethod method, Object[] args) {
        if (method.isVarArgs()) {
            int varArgsStartPosition = method.getParameterTypes().length-1;
            Object[] varargs = Arrays.copyOfRange(args, varArgsStartPosition, args.length);
            if (varargs.length == 1 && varargs[0] == null) {
                return args;
            }
            Object[] newArray = Arrays.copyOfRange(args, 0, varArgsStartPosition+1);
            newArray[varArgsStartPosition] = varargs;
            return newArray;
        }
        return args;
    }

    public static List<ArgumentMatcher> argumentsToMatchersWithNullType(MockitoMethod method, Object[] arguments) {
        Object[] argsWithVarArgsInArray = putVarArgsInsideAnArray(method, arguments);

        List<ArgumentMatcher> matchers = new ArrayList<ArgumentMatcher>(argsWithVarArgsInArray.length);
        for (int i = 0; i < argsWithVarArgsInArray.length; ++i) {
            Object arg = argsWithVarArgsInArray[i];
            ArgumentMatcher argumentMatcher;
            if (arg != null && arg.getClass().isArray()) {
                argumentMatcher = new ArrayEquals(arg);
            } else {
                argumentMatcher = new Equals(arg);
            }
            if (argumentMatcher.matches(null)) {
                argumentMatcher = new ArgumentMatcherVerbose(method.getParameterTypes()[i], argumentMatcher);
            }
            matchers.add(argumentMatcher);
        }
        return matchers;
    }
}
