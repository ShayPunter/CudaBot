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

import uk.co.drcooke.commandapi.annotations.argument.numeric.PreciseClamp;
import uk.co.drcooke.commandapi.argument.parsing.ArgumentParser;
import uk.co.drcooke.commandapi.argument.parsing.CommandParameter;
import uk.co.drcooke.commandapi.argument.parsing.IllegalInputException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class PreciseValueClampingNumberArgumentParserDecorator implements ArgumentParser<Number> {

    private static final Map<Class<? extends Number>, BiFunction<PreciseClamp, Function<String, Number>, Consumer<Number>>> PRECISE_CLAMP_VALIDATING_FUNCTIONS;
    private static final Map<Class<? extends Number>, BiFunction<PreciseClamp, Function<String, Number>, UnaryOperator<Number>>> PRECISE_CLAMP_LIMITING_FUNCTIONS;

    static {
        Map<Class<? extends Number>, BiFunction<PreciseClamp, Function<String, Number>, Consumer<Number>>> validators = new HashMap<>();
        validators.put(Byte.class, (preciseClamp, parser) -> number -> validateNumberWithinClamp(preciseClamp, number.byteValue(), parser, Number::byteValue));
        validators.put(Short.class, (preciseClamp, parser) -> number -> validateNumberWithinClamp(preciseClamp, number.shortValue(), parser, Number::shortValue));
        validators.put(Integer.class, (preciseClamp, parser) -> number -> validateNumberWithinClamp(preciseClamp, number.intValue(), parser, Number::intValue));
        validators.put(Long.class, (preciseClamp, parser) -> number -> validateNumberWithinClamp(preciseClamp, number.longValue(), parser, Number::longValue));
        validators.put(Double.class, (preciseClamp, parser) -> number -> validateNumberWithinClamp(preciseClamp, number.doubleValue(), parser, Number::doubleValue));
        validators.put(BigInteger.class, (preciseClamp, parser) -> number -> validateNumberWithinClamp(preciseClamp, (BigInteger) number, parser, BigInteger.class::cast));
        validators.put(BigDecimal.class, (preciseClamp, parser) -> number -> validateNumberWithinClamp(preciseClamp, (BigDecimal) number, parser, BigDecimal.class::cast));
        PRECISE_CLAMP_VALIDATING_FUNCTIONS = validators;
        Map<Class<? extends Number>, BiFunction<PreciseClamp, Function<String, Number>, UnaryOperator<Number>>> limiters = new HashMap<>();
        limiters.put(Byte.class, (preciseClamp, parser) -> number -> {
            int num = number.byteValue();
            if (!preciseClamp.min().isEmpty())
                num = Math.max(num, parser.apply(preciseClamp.min()).byteValue());
            if (!preciseClamp.max().isEmpty())
                num = Math.min(num, parser.apply(preciseClamp.max()).byteValue());
            return (byte) num;
        });
        limiters.put(Short.class, (preciseClamp, parser) -> number -> {
            int num = number.shortValue();
            if (!preciseClamp.min().isEmpty())
                num = Math.max(num, parser.apply(preciseClamp.min()).shortValue());
            if (!preciseClamp.max().isEmpty())
                num = Math.min(num, parser.apply(preciseClamp.max()).shortValue());
            return (short) num;
        });
        limiters.put(Integer.class, (preciseClamp, parser) -> number -> {
            int num = number.intValue();
            if (!preciseClamp.min().isEmpty())
                num = Math.max(num, parser.apply(preciseClamp.min()).intValue());
            if (!preciseClamp.max().isEmpty())
                num = Math.min(num, parser.apply(preciseClamp.max()).intValue());
            return num;
        });
        limiters.put(Long.class, (preciseClamp, parser) -> number -> {
            long num = number.longValue();
            if (!preciseClamp.min().isEmpty())
                num = Math.max(num, parser.apply(preciseClamp.min()).longValue());
            if (!preciseClamp.max().isEmpty())
                num = Math.min(num, parser.apply(preciseClamp.max()).longValue());
            return num;
        });
        limiters.put(Double.class, (preciseClamp, parser) -> number -> {
            double num = number.doubleValue();
            if (!preciseClamp.min().isEmpty())
                num = Math.max(num, parser.apply(preciseClamp.min()).doubleValue());
            if (!preciseClamp.max().isEmpty())
                num = Math.min(num, parser.apply(preciseClamp.max()).doubleValue());
            return num;
        });
        limiters.put(Float.class, (preciseClamp, parser) -> number -> {
            float num = number.floatValue();
            if (!preciseClamp.min().isEmpty())
                num = Math.max(num, parser.apply(preciseClamp.min()).floatValue());
            if (!preciseClamp.max().isEmpty())
                num = Math.min(num, parser.apply(preciseClamp.max()).floatValue());
            return num;
        });
        limiters.put(BigInteger.class, (preciseClamp, parser) -> number -> {
            BigInteger num = BigInteger.valueOf(number.longValue());
            if (!preciseClamp.min().isEmpty()) num = num.max(new BigInteger(preciseClamp.min()));
            if (!preciseClamp.max().isEmpty()) num = num.min(new BigInteger(preciseClamp.max()));
            return num;
        });
        limiters.put(BigDecimal.class, (preciseClamp, parser) -> number -> {
            BigDecimal num = BigDecimal.valueOf(number.doubleValue());
            if (!preciseClamp.min().isEmpty()) num = num.max(new BigDecimal(preciseClamp.min()));
            if (!preciseClamp.max().isEmpty()) num = num.min(new BigDecimal(preciseClamp.max()));
            return num;
        });
        PRECISE_CLAMP_LIMITING_FUNCTIONS = limiters;
    }

    private final ArgumentParser<Number> parser;

    public PreciseValueClampingNumberArgumentParserDecorator(ArgumentParser<Number> parser) {
        this.parser = parser;
    }

    public static <T extends Number & Comparable<? super T>> void validateNumberWithinClamp(PreciseClamp preciseClamp,
                                                                                            T number, Function<String, Number> parser, Function<Number, T> normaliser) {
        Number min = preciseClamp.min().isEmpty() ? null : parser.apply(preciseClamp.min());
        Number max = preciseClamp.max().isEmpty() ? null : parser.apply(preciseClamp.min());
        ensureNumberInBounds(normaliser.apply(number), normaliser.apply(min), normaliser.apply(max));
    }

    public static <T extends Number & Comparable<? super T>> void ensureNumberInBounds(T number, T min, T max) {
        if (max != null && number.compareTo(max) > 0) {
            throw new ArgumentOutOfBoundsException("Number " + number + " must be less than or equal to " + max);
        }
        if (min != null && number.compareTo(min) < 0) {
            throw new ArgumentOutOfBoundsException("Number " + number + " must be more than or equal to " + min);
        }
    }

    public static Number clamp(Number number, PreciseClamp clamp, Function<String, Number> normaliser) {
        switch (clamp.method()) {
            case LIMIT: {
                return limitNumberToClamp(number, clamp, normaliser);
            }
            case VALIDATE: {
                validateNumberWithinClamp(number, clamp, normaliser);
            }
            default: {
                return number;
            }
        }
    }

    public static Number limitNumberToClamp(Number number, PreciseClamp clamp, Function<String, Number> normaliser) {
        BiFunction<PreciseClamp, Function<String, Number>, UnaryOperator<Number>> limiter = PRECISE_CLAMP_LIMITING_FUNCTIONS.get(number.getClass());
        if (limiter == null) {
            throw new IllegalInputException("Unknown number type " + number.getClass().getCanonicalName());
        } else {
            return limiter.apply(clamp, normaliser).apply(number);
        }
    }

    public static void validateNumberWithinClamp(Number number, PreciseClamp clamp, Function<String, Number> normaliser) {
        BiFunction<PreciseClamp, Function<String, Number>, Consumer<Number>> validator = PRECISE_CLAMP_VALIDATING_FUNCTIONS.get(number.getClass());
        if (validator == null) {
            throw new IllegalInputException("Unknown number type " + number.getClass().getCanonicalName());
        } else {
            validator.apply(clamp, normaliser).accept(number);
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

    public Number clampIfNecessary(Number number, CommandParameter commandParameter) {
        if (commandParameter.isAnnotationPresent(PreciseClamp.class)) {
            return clamp(number, commandParameter.getAnnotation(PreciseClamp.class),
                    n -> parser.parse(new ArrayDeque<>(Collections.singleton(n)), commandParameter));
        } else {
            return number;
        }
    }

}
