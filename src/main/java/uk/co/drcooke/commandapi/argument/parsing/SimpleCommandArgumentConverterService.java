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

import uk.co.drcooke.commandapi.execution.ArgumentManifest;
import uk.co.drcooke.commandapi.execution.executable.CommandExecutable;

import java.util.ArrayList;
import java.util.Deque;

public class SimpleCommandArgumentConverterService implements CommandArgumentConverterService {

    private ArgumentParserLookupService argumentParserLookupService;

    public SimpleCommandArgumentConverterService(ArgumentParserLookupService argumentParserLookupService) {
        this.argumentParserLookupService = argumentParserLookupService;
    }

    @Override
    public ArgumentManifest getArgumentManifest(CommandExecutable commandExecutable, Deque<String> arguments) {
        ArrayList<Object> parsedArguments = new ArrayList<>();
        if (commandExecutable.getCommandParameters() != null) {
            for (CommandParameter commandParameter : commandExecutable.getCommandParameters()) {
                parsedArguments.add(argumentParserLookupService
                        .getArgumentParserForParameter(commandParameter)
                        .parse(arguments, commandParameter));
            }
        }
        return new ArgumentManifest(parsedArguments);
    }
}
