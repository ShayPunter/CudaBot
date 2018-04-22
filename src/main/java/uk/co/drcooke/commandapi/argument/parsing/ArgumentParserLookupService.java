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

package uk.co.drcooke.commandapi.argument.parsing;

import uk.co.drcooke.commandapi.argument.parsing.bool.PrimitiveWrappingBooleanArgumentParserDecorator;
import uk.co.drcooke.commandapi.argument.parsing.bool.SimpleBooleanArgumentParser;
import uk.co.drcooke.commandapi.argument.parsing.number.*;
import uk.co.drcooke.commandapi.argument.parsing.text.character.PrimitiveWrappingCharacterArgumentParserDecorator;
import uk.co.drcooke.commandapi.argument.parsing.text.character.RegularExpressionCharacterArgumentParserDecorator;
import uk.co.drcooke.commandapi.argument.parsing.text.character.SimpleCharacterArgumentParser;
import uk.co.drcooke.commandapi.argument.parsing.text.string.LengthLimitingStringArgumentParserDecorator;
import uk.co.drcooke.commandapi.argument.parsing.text.string.RegularExpressionStringArgumentParserDecorator;
import uk.co.drcooke.commandapi.argument.parsing.text.string.SimpleStringArgumentParser;

import java.util.ArrayList;
import java.util.Collection;

public interface ArgumentParserLookupService {
    static Collection<ArgumentParser<?>> getBuiltinArgumentParsers() {
        ArrayList<ArgumentParser<?>> argumentParsers = new ArrayList<>();
        argumentParsers.add(new PrimitiveWrappingBooleanArgumentParserDecorator(
                SimpleBooleanArgumentParser.INSTANCE
        ));

        argumentParsers.add(new PrimitiveWrappingNumberArgumentParserDecorator(
                        new DecimalValueClampingNumberArgumentParserDecorator(
                                new IntegerValueNumberArgumentParserDecorator(
                                        new PreciseValueClampingNumberArgumentParserDecorator(
                                                SimpleNumberArgumentParser.INSTANCE
                                        )
                                )
                        )
                )
        );

        argumentParsers.add(new PrimitiveWrappingCharacterArgumentParserDecorator(
                        new RegularExpressionCharacterArgumentParserDecorator(
                                SimpleCharacterArgumentParser.INSTANCE
                        )
                )
        );

        argumentParsers.add(new LengthLimitingStringArgumentParserDecorator(
                new RegularExpressionStringArgumentParserDecorator(
                        SimpleStringArgumentParser.INSTANCE
                )
        ));

        return argumentParsers;
    }

    <T> ArgumentParser<T> getArgumentParserForParameter(CommandParameter commandParameter) throws CannotParseArgumentException;

}
