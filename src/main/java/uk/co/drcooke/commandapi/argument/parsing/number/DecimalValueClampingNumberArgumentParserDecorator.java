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

import uk.co.drcooke.commandapi.annotations.argument.numeric.DecimalClamp;
import uk.co.drcooke.commandapi.argument.parsing.ArgumentParser;
import uk.co.drcooke.commandapi.argument.parsing.CommandParameter;
import uk.co.drcooke.commandapi.argument.parsing.IllegalInputException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class DecimalValueClampingNumberArgumentParserDecorator implements ArgumentParser<Number> {

    private static final Map<Class<? extends Number>, Function<DecimalClamp, Consumer<Number>>> DECIMAL_CLAMP_VALIDATION_FUNCTIONS;
    private static final Map<Class<? extends Number>, Function<DecimalClamp, UnaryOperator<Number>>> DECIMAL_CLAMP_LIMITING_FUNCTIONS;

    static {
        Map<Class<? extends Number>, Function<DecimalClamp, Consumer<Number>>> validators = new HashMap<>();
        validators.put(Byte.class, decimalClamp -> number -> ensureNumberInBounds(number.byteValue(), (byte) decimalClamp.min(), (byte) decimalClamp.max()));
        validators.put(Short.class, decimalClamp -> number -> ensureNumberInBounds(number.shortValue(), (short) decimalClamp.min(), (short) decimalClamp.max()));
        validators.put(Integer.class, decimalClamp -> number -> ensureNumberInBounds(number.intValue(), (int) decimalClamp.min(), (int) decimalClamp.max()));
        validators.put(Long.class, decimalClamp -> number -> ensureNumberInBounds(number.longValue(), (long) decimalClamp.min(), (long) decimalClamp.max()));
        validators.put(Float.class, decimalClamp -> number -> ensureNumberInBounds(number.floatValue(), (float) decimalClamp.min(), (float) decimalClamp.max()));
        validators.put(Double.class, decimalClamp -> number -> ensureNumberInBounds(number.doubleValue(), decimalClamp.min(), decimalClamp.max()));
        validators.put(BigInteger.class, decimalClamp -> number -> ensureNumberInBounds(BigInteger.valueOf(number.longValue()), BigInteger.valueOf((long) decimalClamp.min()), BigInteger.valueOf((long) decimalClamp.max())));
        validators.put(BigDecimal.class, decimalClamp -> number -> ensureNumberInBounds((BigDecimal) number, BigDecimal.valueOf(decimalClamp.min()), BigDecimal.valueOf(decimalClamp.max())));
        DECIMAL_CLAMP_VALIDATION_FUNCTIONS = Collections.unmodifiableMap(validators);
        Map<Class<? extends Number>, Function<DecimalClamp, UnaryOperator<Number>>> limiters = new HashMap<>();
        limiters.put(Byte.class, decimalClamp -> number -> (byte) Math.max(Math.min(number.byteValue(), decimalClamp.max()), decimalClamp.min()));
        limiters.put(Short.class, decimalClamp -> number -> (short) Math.max(Math.min(number.shortValue(), decimalClamp.max()), decimalClamp.min()));
        limiters.put(Integer.class, decimalClamp -> number -> (int) Math.max(Math.min(number.intValue(), decimalClamp.max()), decimalClamp.min()));
        limiters.put(Long.class, decimalClamp -> number -> (long) Math.max(Math.min(number.longValue(), decimalClamp.max()), decimalClamp.min()));
        limiters.put(Float.class, decimalClamp -> number -> (float) Math.max(Math.min(number.floatValue(), decimalClamp.max()), decimalClamp.min()));
        limiters.put(Double.class, decimalClamp -> number -> (double) Math.max(Math.min(number.doubleValue(), decimalClamp.max()), decimalClamp.min()));
        limiters.put(BigInteger.class, decimalClamp -> number -> ((BigInteger) number).min(BigInteger.valueOf((long) decimalClamp.max())).max(BigInteger.valueOf((long) decimalClamp.min())));
        limiters.put(BigDecimal.class, decimalClamp -> number -> ((BigDecimal) number).min(BigDecimal.valueOf(decimalClamp.max())).max(BigDecimal.valueOf(decimalClamp.min())));
        DECIMAL_CLAMP_LIMITING_FUNCTIONS = Collections.unmodifiableMap(limiters);

    }

    private final ArgumentParser<Number> parser;

    public DecimalValueClampingNumberArgumentParserDecorator(ArgumentParser<Number> parser) {
        this.parser = parser;
    }

    public static <T extends Number & Comparable<T>> void ensureNumberInBounds(T number, T min, T max) {
        if (max != null && number.compareTo(max) > 0) {
            throw new ArgumentOutOfBoundsException("Number " + number + " must be less than or equal to " + max);
        }
        if (min != null && number.compareTo(min) < 0) {
            throw new ArgumentOutOfBoundsException("Number " + number + " must be more than or equal to " + min);
        }
    }

    public static Number clampIfNecessary(Number number, CommandParameter commandParameter) {
        if (commandParameter.isAnnotationPresent(DecimalClamp.class)) {
            return clamp(number, commandParameter.getAnnotation(DecimalClamp.class));
        } else {
            return number;
        }
    }

    public static Number clamp(Number number, DecimalClamp clamp) {
        switch (clamp.method()) {
            case LIMIT: {
                return limitNumberToClamp(number, clamp);
            }
            case VALIDATE: {
                validateNumberWithinClamp(number, clamp);
            }
            default: {
                return number;
            }
        }
    }

    public static Number limitNumberToClamp(Number number, DecimalClamp clamp) {
        Function<DecimalClamp, UnaryOperator<Number>> limiter = DECIMAL_CLAMP_LIMITING_FUNCTIONS.get(number.getClass());
        if (limiter == null) {
            throw new IllegalInputException("Unknown number type " + number.getClass().getCanonicalName());
        } else {
            return limiter.apply(clamp).apply(number);
        }
    }

    public static void validateNumberWithinClamp(Number number, DecimalClamp clamp) {
        Function<DecimalClamp, Consumer<Number>> validator = DECIMAL_CLAMP_VALIDATION_FUNCTIONS.get(number.getClass());
        if (validator == null) {
            throw new IllegalInputException("Unknown number type " + number.getClass().getCanonicalName());
        } else {
            validator.apply(clamp).accept(number);
        }
    }

    @Override
    public Number parse(Deque<String> arguments, CommandParameter commandParameter) {
        return clampIfNecessary(parser.parse(arguments, commandParameter), commandParameter);
    }

    @Override
    public boolean canParseParameter(CommandParameter commandParameter) {
        return parser.canParseParameter(commandParameter);
    }

}
