package com.cleancoder.args.marshaler;

import com.cleancoder.args.Args;
import com.cleancoder.args.ArgsException;
import com.cleancoder.args.ArgumentMarshaler;

import java.util.Iterator;

public class BooleanArgumentMarshaler implements ArgumentMarshaler {
  private boolean booleanValue = false;

  public void set(Iterator<String> currentArgument) throws ArgsException {
    booleanValue = true;
  }

  public static boolean getValue(Args args, char arg) {
    ArgumentMarshaler am = args.get(arg);
    if (am != null && am instanceof BooleanArgumentMarshaler)
      return ((BooleanArgumentMarshaler) am).booleanValue;
    else
      return false;
  }
}
