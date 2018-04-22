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

package uk.co.drcooke.commandapi.command.registry;

import uk.co.drcooke.commandapi.command.namespace.CommandNamespace;
import uk.co.drcooke.commandapi.command.scanning.CommandScanner;

import java.util.HashMap;
import java.util.Map;

public class SimpleCommandNamespaceRegistry implements CommandNamespaceRegistry {

    private final Map<String, CommandNamespace> COMMAND_NAMESPACE_MAP;
    private final Map<Object, String> OBJECT_NAMESPACE_MAP;
    private final CommandScanner COMMAND_SCANNER;

    public SimpleCommandNamespaceRegistry(CommandScanner commandScanner) {
        COMMAND_NAMESPACE_MAP = new HashMap<>();
        OBJECT_NAMESPACE_MAP = new HashMap<>();
        COMMAND_SCANNER = commandScanner;
    }

    @Override
    public CommandNamespace getCommandNamespace(String command) {
        return COMMAND_NAMESPACE_MAP.get(command);
    }

    @Override
    public void register(Object o) {
        CommandNamespace[] namespaces = COMMAND_SCANNER.getCommands(o);
        for(CommandNamespace namespace : namespaces) {
            COMMAND_NAMESPACE_MAP.put(namespace.getName(), namespace);
            OBJECT_NAMESPACE_MAP.put(o, namespace.getName());
        }
    }

    @Override
    public void unregister(Object o) {
        COMMAND_NAMESPACE_MAP.remove(OBJECT_NAMESPACE_MAP.get(o));
    }

}
