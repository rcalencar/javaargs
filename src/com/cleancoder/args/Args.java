package com.cleancoder.args;

import java.util.*;

import static com.cleancoder.args.ArgsException.ErrorCode.*;

public class Args {
  private Map<String, Class> marshalers;
  private Map<Character, ArgumentMarshaler> marshalersElements;
  private Set<Character> argsFound;
  private ListIterator<String> currentArgument;

  public Args(String schema, String[] args) throws ArgsException, IllegalAccessException, InstantiationException {
    marshalers = setupMarshalers();
    marshalersElements = new HashMap<Character, ArgumentMarshaler>();
    argsFound = new HashSet<Character>();

    parseSchema(schema);
    parseArgumentStrings(Arrays.asList(args));
  }

  private Map setupMarshalers() {
    Map<String, Class> marshalers = new HashMap<String, Class>();
    marshalers.put("+", BooleanArgumentMarshaler.class);
    marshalers.put("*", StringArgumentMarshaler.class);
    marshalers.put("#", IntegerArgumentMarshaler.class);
    marshalers.put("##", DoubleArgumentMarshaler.class);
    marshalers.put("[*]", StringArrayArgumentMarshaler.class);
    marshalers.put("&", MapArgumentMarshaler.class);

    return marshalers;
  }

  private void parseSchema(String schema) throws ArgsException, InstantiationException, IllegalAccessException {
    for (String element : schema.split(","))
      if (element.length() > 0)
        parseSchemaElement(element.trim());
  }

  private void parseSchemaElement(String element) throws ArgsException, IllegalAccessException, InstantiationException {
    char elementId = element.charAt(0);
    String elementTail = element.substring(1);
    validateSchemaElementId(elementId);

    if (marshalers.containsKey(elementTail)) {
      marshalersElements.put(elementId, (ArgumentMarshaler) marshalers.get(elementTail).newInstance());
    } else {
      throw new ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail);
    }
  }

  private void validateSchemaElementId(char elementId) throws ArgsException {
    if (!Character.isLetter(elementId)){
      throw new ArgsException(INVALID_ARGUMENT_NAME, elementId, null);
    }
  }

  private void parseArgumentStrings(List<String> argsList) throws ArgsException {
    for (currentArgument = argsList.listIterator(); currentArgument.hasNext();) {
      String argString = currentArgument.next();
      if (argString.startsWith("-")) {
        parseArgumentCharacters(argString.substring(1));
      } else {
        currentArgument.previous();
        break;
      }
    }
  }

  private void parseArgumentCharacters(String argChars) throws ArgsException {
    for (int i = 0; i < argChars.length(); i++)
      parseArgumentCharacter(argChars.charAt(i));
  }

  private void parseArgumentCharacter(char argChar) throws ArgsException {
    ArgumentMarshaler m = marshalersElements.get(argChar);
    if (m == null) {
      throw new ArgsException(UNEXPECTED_ARGUMENT, argChar, null);
    } else {
      argsFound.add(argChar);
      try {
        m.set(currentArgument);
      } catch (ArgsException e) {
        e.setErrorArgumentId(argChar);
        throw e;
      }
    }
  }

  public boolean has(char arg) {
    return argsFound.contains(arg);
  }

  public int nextArgument() {
    return currentArgument.nextIndex();
  }

  public boolean getBoolean(char arg) {
    return BooleanArgumentMarshaler.getValue(marshalersElements.get(arg));
  }

  public String getString(char arg) {
    return StringArgumentMarshaler.getValue(marshalersElements.get(arg));
  }

  public int getInt(char arg) {
    return IntegerArgumentMarshaler.getValue(marshalersElements.get(arg));
  }

  public double getDouble(char arg) {
    return DoubleArgumentMarshaler.getValue(marshalersElements.get(arg));
  }

  public String[] getStringArray(char arg) {
    return StringArrayArgumentMarshaler.getValue(marshalersElements.get(arg));
  }

  public Map<String, String> getMap(char arg) {
    return MapArgumentMarshaler.getValue(marshalersElements.get(arg));
  }
}