/*
 * Copyright 2018 David Cooke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uk.co.drcooke.commandapi.argument.parsing.bool;

import uk.co.drcooke.commandapi.argument.parsing.ArgumentParser;
import uk.co.drcooke.commandapi.argument.parsing.CommandParameter;
import uk.co.drcooke.commandapi.argument.parsing.IllegalInputException;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public enum SimpleBooleanArgumentParser implements ArgumentParser<Boolean> {
    INSTANCE;

    private static final Map<String, Boolean> STRING_BOOLEAN_MAP;

    static {
        Map<String, Boolean> lookupTable = new HashMap<>();
        lookupTable.put("yes", Boolean.TRUE);
        lookupTable.put("y", Boolean.TRUE);
        lookupTable.put("on", Boolean.TRUE);
        lookupTable.put("true", Boolean.TRUE);
        lookupTable.put("t", Boolean.TRUE);
        lookupTable.put("1", Boolean.TRUE);
        lookupTable.put("enable", Boolean.TRUE);
        lookupTable.put("enabled", Boolean.TRUE);
        lookupTable.put("no", Boolean.FALSE);
        lookupTable.put("n", Boolean.FALSE);
        lookupTable.put("off", Boolean.FALSE);
        lookupTable.put("false", Boolean.FALSE);
        lookupTable.put("f", Boolean.FALSE);
        lookupTable.put("0", Boolean.FALSE);
        lookupTable.put("disable", Boolean.FALSE);
        lookupTable.put("disabled", Boolean.FALSE);
        STRING_BOOLEAN_MAP = lookupTable;
    }

    @Override
    public Boolean parse(Deque<String> arguments, CommandParameter commandParameter) {
        String argument = arguments.pop();
        Boolean value = STRING_BOOLEAN_MAP.get(argument.toLowerCase());
        if (value == null) {
            throw new IllegalInputException(argument + "is not a boolean.");
        }
        return value;
    }

    @Override
    public boolean canParseParameter(CommandParameter commandParameter) {
        return commandParameter.getType() == Boolean.class;
    }
}
