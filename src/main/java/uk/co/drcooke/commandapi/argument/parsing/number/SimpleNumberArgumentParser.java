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

package uk.co.drcooke.commandapi.argument.parsing.number;

import uk.co.drcooke.commandapi.argument.parsing.ArgumentParser;
import uk.co.drcooke.commandapi.argument.parsing.CommandParameter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public enum SimpleNumberArgumentParser implements ArgumentParser<Number> {
    INSTANCE;

    private static final Predicate<String> OCTAL_PREDICATE;
    private static final Predicate<String> HEX_PREDICATE;
    private static final Map<Class<?>, Function<String, Number>> NUMBER_PARSERS;

    static {
        OCTAL_PREDICATE = Pattern.compile("^([-+])?(0x|0X|#)[a-fA-F0-9]+$").asPredicate();
        HEX_PREDICATE = Pattern.compile("^([-+])?0[1-7]*$").asPredicate();
        NUMBER_PARSERS = new HashMap<>();
        NUMBER_PARSERS.put(Byte.class, Byte::decode);
        NUMBER_PARSERS.put(Short.class, Short::decode);
        NUMBER_PARSERS.put(Integer.class, Integer::decode);
        NUMBER_PARSERS.put(Long.class, Long::decode);
        NUMBER_PARSERS.put(Double.class, SimpleNumberArgumentParser::decodeDouble);
        NUMBER_PARSERS.put(Float.class, SimpleNumberArgumentParser::decodeFloat);
        NUMBER_PARSERS.put(BigInteger.class, SimpleNumberArgumentParser::decodeBigInteger);
        NUMBER_PARSERS.put(BigDecimal.class, SimpleNumberArgumentParser::decodeBigDecimal);
    }

    public static Double decodeDouble(String input) throws NumberFormatException {
        return HEX_PREDICATE.test(input) || OCTAL_PREDICATE.test(input)
                ? Long.decode(input).doubleValue()
                : Double.valueOf(input);
    }

    public static Float decodeFloat(String input) throws NumberFormatException {
        return HEX_PREDICATE.test(input) || OCTAL_PREDICATE.test(input)
                ? Long.decode(input).floatValue()
                : Float.valueOf(input);
    }

    public static BigInteger decodeBigInteger(String input) throws NumberFormatException {
        return HEX_PREDICATE.test(input)
                ? new BigInteger(input.substring(input.charAt(0) == '0' ? 2 : 1), 16)
                : OCTAL_PREDICATE.test(input)
                ? new BigInteger(input, 8)
                : new BigInteger(input);
    }

    public static BigDecimal decodeBigDecimal(String input) throws NumberFormatException {
        return HEX_PREDICATE.test(input)
                ? new BigDecimal(new BigInteger(input.substring(input.charAt(0) == '0' ? 2 : 1), 16))
                : OCTAL_PREDICATE.test(input)
                ? new BigDecimal(new BigInteger(input, 8))
                : new BigDecimal(input);
    }

    @Override
    public Number parse(Deque<String> arguments, CommandParameter commandParameter) {
        Function<String, Number> decoder = NUMBER_PARSERS.get(commandParameter.getType());
        if (decoder != null) {
            String number = arguments.pop();
            try {
                return decoder.apply(number);
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Number is in bad format.", exception);
            }
        } else {
            throw new IllegalArgumentException("Cannot parse a number of type: " + commandParameter.getType().getCanonicalName());
        }
    }

    @Override
    public boolean canParseParameter(CommandParameter commandParameter) {
        return NUMBER_PARSERS.containsKey(commandParameter.getType());
    }

}
