package ru.ifmo.wst.lab1.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommandInterpreter {
    private static final String COMMAND_SEPARATOR = "==============";
    private final Supplier<String> lineProducer;
    private final Consumer<String> messageConsumer;
    private final List<Command<?>> commands;
    private final String noCommandMessage;
    private final String commandPrompt;
    private final String argPrompt;

    public CommandInterpreter(Supplier<String> lineProducer, Consumer<String> messageConsumer,
                              List<Command<?>> commands, String noCommandMessage, String commandPrompt, String argPrompt) {
        this.lineProducer = lineProducer;
        this.messageConsumer = messageConsumer;
        this.commands = new ArrayList<>(commands);
        this.noCommandMessage = noCommandMessage;
        this.commandPrompt = commandPrompt;
        this.argPrompt = argPrompt;
    }

    public void info() {
        newLineMessage("List of commands: ");
        newLineMessage("");
        commands.forEach(command -> {
            newLineMessage(command.getName() + " - " + command.getDescription());
            newLineMessage("Command arguments:");
            command.getArgs().forEach(arg -> {
                CommandArgDescription<?> argDescription = arg.getArgDescription();
                newLineMessage(argDescription.getName() + " - " + argDescription.getDescription());
            });
            newLineMessage(COMMAND_SEPARATOR);
        });
    }

    public void readCommand() throws Exception {
        prompt(commandPrompt);
        String line = nextLine();
        Command matchedCommand = null;
        for (Command<?> command : commands) {
            if (line.trim().equals(command.getName())) {
                matchedCommand = command;
            }
        }
        if (matchedCommand == null) {
            newLineMessage(noCommandMessage);
            return;
        }
        Object o = matchedCommand.newValue();
        for (CommandArg arg : (List<CommandArg>) matchedCommand.getArgs()) {
            CommandArgDescription argDescription = arg.getArgDescription();
            while (true) {
                prompt(argDescription.getDescription());
                String currLine = nextLine();
                try {
                    Object converted = argDescription.convertFromString(currLine);
                    arg.acceptArgValue(o, converted);
                    break;
                } catch (ConvertException exc) {
                    newLineMessage(argDescription.getErrorMessage());
                }
            }
        }
        matchedCommand.execute(o);
    }

    public void addCommand(Command<?> command) {
        this.commands.add(command);
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
