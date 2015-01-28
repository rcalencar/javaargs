package com.cleancoder.args.marshaler;

import com.cleancoder.args.Args;
import com.cleancoder.args.ArgsException;
import com.cleancoder.args.ArgumentMarshaler;

import static com.cleancoder.args.ArgsException.ErrorCode.*;

import java.util.*;

public class StringArrayArgumentMarshaler implements ArgumentMarshaler {
    private List<String> strings = new ArrayList<String>();

    public void set(Iterator<String> currentArgument) throws ArgsException {
        try {
            strings.add(currentArgument.next());
        } catch (NoSuchElementException e) {
            throw new ArgsException(MISSING_STRING);
        }
    }

    public static String[] getValue(Args args, char arg) {
        ArgumentMarshaler am = args.get(arg);
        if (am != null && am instanceof StringArrayArgumentMarshaler)
            return ((StringArrayArgumentMarshaler) am).strings.toArray(new String[0]);
        else
            return new String[0];
    }
}
