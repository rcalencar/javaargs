package com.cleancoder.args.marshaler;

import com.cleancoder.args.Args;
import com.cleancoder.args.ArgsException;
import com.cleancoder.args.ArgumentMarshaler;

import static com.cleancoder.args.ArgsException.ErrorCode.*;

import java.util.*;

public class DoubleArgumentMarshaler implements ArgumentMarshaler {
  private double doubleValue = 0;

  public void set(Iterator<String> currentArgument) throws ArgsException {
    String parameter = null;
    try {
      parameter = currentArgument.next();
      doubleValue = Double.parseDouble(parameter);
    } catch (NoSuchElementException e) {
      throw new ArgsException(MISSING_DOUBLE);
    } catch (NumberFormatException e) {
      throw new ArgsException(INVALID_DOUBLE, parameter);
    }
  }

  public static double getValue(Args args, char arg) {
    ArgumentMarshaler am = args.get(arg);
    if (am != null && am instanceof DoubleArgumentMarshaler)
      return ((DoubleArgumentMarshaler) am).doubleValue;
    else
      return 0.0;
  }
}
