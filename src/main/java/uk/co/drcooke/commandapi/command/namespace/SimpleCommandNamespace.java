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

package uk.co.drcooke.commandapi.command.namespace;

import uk.co.drcooke.commandapi.execution.executable.CommandExecutable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCommandNamespace implements CommandNamespace {

    private String name;
    private String description = "";
    private String usage = "";
    private Map<String, CommandExecutable> commandExecutables;
    private CommandExecutable defaultCommandExecutable;
    private List<CommandNamespace> subNamespaces;

    public SimpleCommandNamespace(String name) {
        this.name = name;
        this.commandExecutables = new HashMap<>();
        this.subNamespaces = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public CommandExecutable getSubCommand(String name) {
        return commandExecutables.get(name);
    }

    @Override
    public CommandExecutable getDefaultCommand() {
        return defaultCommandExecutable;
    }

    @Override
    public void setDefaultCommand(CommandExecutable commandExecutable) {
        this.defaultCommandExecutable = commandExecutable;
    }

    @Override
    public void addCommand(CommandExecutable commandExecutable) {
        commandExecutables.put(commandExecutable.getName(), commandExecutable);
    }

    @Override
    public void removeCommand(CommandExecutable commandExecutable) {
        removeCommand(commandExecutable.getName());
    }

    @Override
    public void removeCommand(String name) {
        commandExecutables.remove(name);
    }

    @Override
    public List<CommandNamespace> getSubNamespaces() {
        return subNamespaces;
    }

    @Override
    public void addSubNamespace(CommandNamespace commandNamespace) {
        subNamespaces.add(commandNamespace);
    }

    @Override
    public CommandNamespace getSubNamespace(String namespace) {
        for (CommandNamespace commandNamespace : subNamespaces) {
            if (commandNamespace.getName().equals(namespace)) {
                return commandNamespace;
            }
        }
        return null;
    }

}
