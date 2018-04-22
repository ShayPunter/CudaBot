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

package uk.co.drcooke.commandapi.argument.parsing.text.string;

import uk.co.drcooke.commandapi.annotations.argument.validation.Length;
import uk.co.drcooke.commandapi.argument.parsing.ArgumentParser;
import uk.co.drcooke.commandapi.argument.parsing.CommandParameter;
import uk.co.drcooke.commandapi.argument.parsing.IllegalInputException;

import java.util.Deque;

public class LengthLimitingStringArgumentParserDecorator implements ArgumentParser<String> {

    private ArgumentParser<String> parser;

    public LengthLimitingStringArgumentParserDecorator(ArgumentParser<String> parser) {
        this.parser = parser;
    }

    @Override
    public String parse(Deque<String> arguments, CommandParameter commandParameter) {
        String argument = parser.parse(arguments, commandParameter);
        validateIfNecessary(argument, commandParameter);
        return argument;
    }

    @Override
    public boolean canParseParameter(CommandParameter commandParameter) {
        return parser.canParseParameter(commandParameter);
    }

    public void validateIfNecessary(String argument, CommandParameter commandParameter) {
        Length length = commandParameter.getAnnotation(Length.class);
        if (length != null) {
            int minLength = length.minLength();
            int maxLength = length.maxLength();
            int actualLength = argument.length();
            if (actualLength < minLength) {
                throw new IllegalInputException("Argument " + argument + " is too short");
            }
            if (actualLength > maxLength) {
                throw new IllegalInputException("Argument " + argument + " is too long");
            }
        }
    }

}
