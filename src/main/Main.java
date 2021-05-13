package main;

import exceptions.InvalidArgumentException;

import java.io.IOException;
import java.util.List;

import static main.Command.Mode.HELP;

public class Main {

    public static void main(String[] args) {
        var command = new Command();
        try {
            command = new ArgumentParser(args).parse();
        } catch (InvalidArgumentException | IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println(helpText());
            return;
        }

        if (HELP.equals(command.getMode())) {
            System.out.println(helpText());
            return;
        }

        System.out.println(command);

        if (command.getUrls().isEmpty()) {
            System.out.println("URL list is empty.");
            return;
        }

        command.execute();
    }

    private static String helpText() {
        return "help me!";
    }

    private static String formatOptions(List<String> options) {
        return options.toString().replace("[", "").replace("]", "");
    }

    /*  (For the record, I'm aware code comments is not a good method of
        project planning in general. But it will do for small solo projects.)

    TODO / further work
        -
     */
}