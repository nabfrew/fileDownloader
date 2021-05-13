package main;

import exceptions.InvalidArgumentException;

import java.io.File;

import static main.ArgumentParser.OUTPUT_DIRECTORY_FLAG;
import static main.ArgumentParser.URL_LIST_FLAG;

public class Command {
    public static final String DEFAULT_URL_LIST = "urlList.txt";
    private Mode mode;
    private File outputDirectory;
    private File urlListFile;

    public File getOutputDirectory() {
        return outputDirectory;
    }


    public enum Mode {
        HELP,
        DOWNLOAD;
    }

    protected Command() {
    }

    protected Command(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setProperty(String propertyFlag, String propertyValue) throws InvalidArgumentException {
        if (OUTPUT_DIRECTORY_FLAG.contains(propertyFlag)) {
            verifyDuplicate(outputDirectory);
            outputDirectory = new File(propertyValue);
        } else if (URL_LIST_FLAG.contains(propertyFlag)) {
            verifyDuplicate(urlListFile);
            urlListFile = new File(propertyValue);
        } else {
            throw new InvalidArgumentException("Unrecognised argument: " + propertyFlag + ".");
        }
    }

    private <T> void verifyDuplicate(T current) throws InvalidArgumentException {
        if (current != null) {
            throw new InvalidArgumentException("Duplicate argument.");
        }
    }

    public void execute() {
    }

    public void setDefaultUrlListFile() {
        urlListFile = new File("", DEFAULT_URL_LIST).getAbsoluteFile();
    }

    public void setDefaultOutputDirectory() {
        outputDirectory = new File("").getAbsoluteFile();
    }

    public File getUrlListFile() {
        return urlListFile;
    }

    public String toString() {
        var string = new StringBuilder();
        if (mode != null) {
            string.append("Mode:\n\t").append(mode);
        }
        if (urlListFile != null) {
            string.append("\nOutput directory:\n\t").append(urlListFile);
        }
        if (outputDirectory != null) {
            string.append("\nOutput directory:\n\t").append(outputDirectory);
        }

        return string.toString();
    }
}