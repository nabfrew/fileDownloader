package com;

import dlexceptions.InvalidArgumentException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.Command.Mode.DOWNLOAD;
import static com.Command.Mode.HELP;

public record ArgumentParser(String[] args) {

    public static final List<String> HELP_FLAG = Arrays.asList("-h", "-help");
    public static final List<String> OUTPUT_DIRECTORY_FLAG = Arrays.asList("-d", "-directory");
    public static final List<String> VERBOSE_FLAG = Arrays.asList("-v", "-verbose");
    public static final List<String> VERBOSE_FALSE = Arrays.asList("f", "false");
    public static final List<String> VERBOSE_TRUE = Arrays.asList("t", "true");
    public static final List<String> URL_LIST_FLAG = Collections.singletonList("-url");

    public Command parse() throws InvalidArgumentException, IOException {
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

    private void setDefaults(Command command) throws IOException {
        if (command.getMode() == null) {
            command.setMode(DOWNLOAD);
        }
        if (command.getOutputDirectory() == null) {
            command.setDefaultOutputDirectory();
        }
        if (command.getUrlListFile() == null) {
            command.setDefaultUrlListFile();
        }
        if (command.getVerbose() == null) {
            command.setVerbose(true);
        }
    }

    private void parseFlags(String[] args, Command command) throws InvalidArgumentException, IOException {
        for (var i = 0; i < args.length; i += 2) {
            if (i + 2 > args.length) {
                throw new InvalidArgumentException("property value missing for " + args[i]);
            }
            command.setProperty(args[i], args[i + 1]);
        }
    }
}