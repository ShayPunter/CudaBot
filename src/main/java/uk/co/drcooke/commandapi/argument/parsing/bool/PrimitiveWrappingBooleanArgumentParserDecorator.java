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
import uk.co.drcooke.commandapi.argument.parsing.SimpleCommandParameter;

import java.util.Deque;

public class PrimitiveWrappingBooleanArgumentParserDecorator implements ArgumentParser<Boolean> {

    private final ArgumentParser<Boolean> parser;

    public PrimitiveWrappingBooleanArgumentParserDecorator(ArgumentParser<Boolean> parser) {
        this.parser = parser;
    }

    @Override
    public Boolean parse(Deque<String> arguments, CommandParameter commandParameter) {
        return parser.parse(arguments, wrapCommandParameter(commandParameter));
    }

    @Override
    public boolean canParseParameter(CommandParameter commandParameter) {
        return parser.canParseParameter(wrapCommandParameter(commandParameter));
    }

    private CommandParameter wrapCommandParameter(CommandParameter commandParameter) {
        return new SimpleCommandParameter(commandParameter.getType() == Boolean.TYPE
                ? Boolean.class : commandParameter.getType(), commandParameter.getAnnotations());
    }

}
