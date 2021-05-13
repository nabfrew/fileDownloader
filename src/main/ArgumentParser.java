package main;

import exceptions.InvalidArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static main.Command.Mode.DOWNLOAD;
import static main.Command.Mode.HELP;

public record ArgumentParser(String[] args) {

    protected static final List<String> HELP_FLAG = Arrays.asList("-h", "-help");
    protected static final List<String> OUTPUT_DIRECTORY_FLAG = Arrays.asList("-d", "-directory");
    protected static final List<String> URL_LIST_FLAG = Collections.singletonList("-url");

    public Command parse() throws InvalidArgumentException {
        Command command = new Command();
        if (args == null || args.length == 0 ) {
            setDefaults(command);
            return command;
        }
        if (HELP_FLAG.contains(args[0])) {
            return new Command(HELP);
        }

        parseFlags(args, command);
        setDefaults(command);
        return command;
    }

    private void setDefaults(Command command) {
        if (command.getMode() == null) {
            command.setMode(DOWNLOAD);
        }
        if (command.getOutputDirectory() == null) {
            command.setDefaultOutputDirectory();
        }
        if (command.getUrlListFile() == null) {
            command.setDefaultUrlListFile();
        }
    }

    private void parseFlags(String[] args, Command command) throws InvalidArgumentException {
        for (var i = 0; i < args.length; i += 2) {
            if (i + 2 > args.length) {
                throw new InvalidArgumentException("property value missing for " + args[i]);
            }
            command.setProperty(args[i], args[i + 1]);
        }
    }
}