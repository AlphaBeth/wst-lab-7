package ru.ifmo.wst.lab1.command;

import lombok.RequiredArgsConstructor;
import ru.ifmo.wst.lab1.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class CommandInterpreter {
    private static final String COMMAND_SEPARATOR = "==============";
    private final Supplier<String> lineProducer;
    private final Consumer<String> messageConsumer;
    private final List<Command<?>> commands;
    private final String noCommandMessage;
    private final String commandPrompt;
    private final String argPrompt;

    public void info() {
        newLineMessage("List of commands: ");
        newLineMessage("");
        commands.forEach(command -> {
            newLineMessage(command.getName() + " - " + command.getDescription());
            newLineMessage("Command arguments:");
            command.getArgs().forEach(arg -> {
                newLineMessage(arg.getName() + " - " + arg.getDescription());
            });
            newLineMessage(COMMAND_SEPARATOR);
        });
    }

    public Pair<Command, Object> readCommand() {
        prompt(commandPrompt);
        String line = nextLine();
        Command<?> matchedCommand = null;
        for (Command command : commands) {
            if (line.trim().equals(command.getName())) {
                matchedCommand = command;
            }
        }
        if (matchedCommand == null) {
            newLineMessage(noCommandMessage);
            return null;
        }
        Object o = matchedCommand.newValue();
        for (CommandArg arg : matchedCommand.getArgs()) {
            while (true) {
                prompt(arg.getDescription());
                String currLine = nextLine();
                try {
                    Object converted = arg.convertFromString(currLine);
                    arg.acceptConverted(converted, o);
                    break;
                } catch (ConvertException exc) {
                    newLineMessage(arg.getErrorMessage());
                }
            }
        }
        return new Pair<>(matchedCommand, o);
    }

    private void message(String message) {
        messageConsumer.accept(message);
    }

    private void newLineMessage(String message) {
        message(message + "\n");
    }

    private void prompt(String promptMessage) {
        newLineMessage(promptMessage);
        message(argPrompt);
    }

    private String nextLine() {
        String line = lineProducer.get();
        if (line == null) {
            throw new NoLineFoundException();
        }
        return line;
    }
}
