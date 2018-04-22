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

package uk.co.drcooke.commandapi.command.lookup;

import uk.co.drcooke.commandapi.command.namespace.CommandNamespace;
import uk.co.drcooke.commandapi.command.registry.CommandNamespaceRegistry;
import uk.co.drcooke.commandapi.execution.executable.CommandExecutable;

import java.util.ArrayList;
import java.util.Deque;

public class CommandNamespaceSearchingCommandLookup implements CommandLookup {

    private CommandNamespaceRegistry commandNamespaceRegistry;

    public CommandNamespaceSearchingCommandLookup(CommandNamespaceRegistry commandNamespaceRegistry) {
        this.commandNamespaceRegistry = commandNamespaceRegistry;
    }

    @Override
    public CommandExecutable getCommand(Deque<String> tokens) throws CommandNotFoundException {
        CommandNamespace currentNamespace = null;
        CommandExecutable commandExecutable = null;
        for (String token : new ArrayList<>(tokens)) {
            if (currentNamespace == null) {
                currentNamespace = commandNamespaceRegistry.getCommandNamespace(token);
                tokens.pop();
            }
            if (currentNamespace.getSubNamespace(token) != null) {
                currentNamespace = currentNamespace.getSubNamespace(token);
                tokens.pop();
            } else {
                commandExecutable = currentNamespace.getSubCommand(token);
                if (commandExecutable == null) {
                    commandExecutable = currentNamespace.getDefaultCommand();
                }else if(currentNamespace.getSubNamespace(token) == null){
                    tokens.pop();
                    return commandExecutable;
                }
            }
        }
        if (commandExecutable == null) {
            throw new CommandNotFoundException();
        }
        return commandExecutable;
    }

}
