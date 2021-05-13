package main;

import exceptions.InvalidArgumentException;

import java.io.IOException;

import static main.ArgumentParser.HELP_FLAG;
import static main.ArgumentParser.OUTPUT_DIRECTORY_FLAG;
import static main.ArgumentParser.URL_LIST_FLAG;
import static main.ArgumentParser.VERBOSE_FALSE;
import static main.ArgumentParser.VERBOSE_FLAG;
import static main.ArgumentParser.VERBOSE_TRUE;
import static main.Command.DEFAULT_URL_LIST;
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

        if (command.getUrls().isEmpty()) {
            System.out.println("No urls to download found.");
            return;
        }

        System.out.println(command);


        command.execute();
    }

    private static String helpText() {
        return "Usage: fileDownloader " + OUTPUT_DIRECTORY_FLAG + " " + URL_LIST_FLAG + " " + VERBOSE_FLAG +"\n\n"
                + "Options:\n"
                + "\t" + OUTPUT_DIRECTORY_FLAG + "\n\t\tDirectory to download to. Default: [pwd].\n"
                + "\t" + URL_LIST_FLAG + "\n\t\tPath to text file containing urls to download. Default: [pwd]/"+ DEFAULT_URL_LIST + ".\n"
                + "\t" + URL_LIST_FLAG + "\n\t\tDirectory to download to. Default: present working directory.\n"
                + "\t" + VERBOSE_FLAG + "\n\t\t" + VERBOSE_TRUE + "/" + VERBOSE_FALSE + "\n"
                + "\t" + HELP_FLAG + "\n\t\tDisplay this message.";
    }

    /*  (For the record, I'm aware code comments is not a good method of
        project planning in general. But it will do for small solo projects.)

    TODO / further work
        -
     */
}