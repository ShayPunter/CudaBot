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

import uk.co.drcooke.commandapi.annotations.argument.validation.Match;
import uk.co.drcooke.commandapi.argument.parsing.ArgumentParser;
import uk.co.drcooke.commandapi.argument.parsing.CommandParameter;
import uk.co.drcooke.commandapi.argument.parsing.IllegalInputException;

import java.util.Deque;
import java.util.regex.Pattern;

public class RegularExpressionStringArgumentParserDecorator implements ArgumentParser<String> {

    private ArgumentParser<String> parser;

    public RegularExpressionStringArgumentParserDecorator(ArgumentParser<String> parser) {
        this.parser = parser;
    }

    private static void validateIfNecessary(String argument, CommandParameter commandParameter) {
        Match match = commandParameter.getAnnotation(Match.class);
        if (match != null) {
            switch (match.matchType()) {
                case EQUALITY: {
                    validateEquality(argument, match.match());
                    break;
                }
                case CASE_INSENSITIVE: {
                    validateEqualityIgnoreCase(argument, match.match());
                    break;
                }
                case PARTIAL_REGEX: {
                    validatePartialRegex(argument, match.match());
                    break;
                }
                case FULL_REGEX: {
                    validateFullRegex(argument, match.match());
                    break;
                }
            }
        }
    }

    private static void validateEquality(String argument, String target) {
        if (!argument.equals(target)) {
            throw new IllegalInputException("Input " + argument + " is not equal to " + target);
        }
    }

    private static void validateEqualityIgnoreCase(String argument, String target) {
        if (!argument.equalsIgnoreCase(target)) {
            throw new IllegalInputException("Input " + argument + " is not case-insensitive equal to " + target);
        }
    }

    private static void validatePartialRegex(String argument, String target) {
        if (!Pattern.compile(target).matcher(argument).find()) {
            throw new IllegalInputException("Input " + argument + " does not contain regex " + target);
        }
    }

    private static void validateFullRegex(String argument, String target) {
        if (!Pattern.compile(target).matcher(argument).matches()) {
            throw new IllegalInputException("Input " + argument + " does not match regex " + target);
        }
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

}
