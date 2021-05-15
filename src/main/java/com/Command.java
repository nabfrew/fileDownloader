package com;

import dlexceptions.InvalidArgumentException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ArgumentParser.OUTPUT_DIRECTORY_FLAG;
import static com.ArgumentParser.URL_LIST_FLAG;
import static com.ArgumentParser.VERBOSE_FALSE;
import static com.ArgumentParser.VERBOSE_FLAG;
import static com.ArgumentParser.VERBOSE_TRUE;
import static java.lang.Boolean.TRUE;

public class Command {
    public static final String DEFAULT_URL_LIST = "urlList.txt";
    private Mode mode;
    private File outputDirectory;
    private String urlListFile;
    private final List<String> invalidUrls = new ArrayList<>();
    private final List<URL> urls = new ArrayList<>();
    private Boolean verbose;

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public Boolean getVerbose() {
        return verbose;
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

    public void setProperty(String propertyFlag, String propertyValue) throws InvalidArgumentException, IOException {
        if (OUTPUT_DIRECTORY_FLAG.contains(propertyFlag)) {
            verifyDuplicate(outputDirectory);
            outputDirectory = new File(propertyValue);
        } else if (URL_LIST_FLAG.contains(propertyFlag)) {
            verifyDuplicate(urlListFile);
            urlListFile = propertyValue;
            setUrls();
        } else if (VERBOSE_FLAG.contains(propertyFlag)) {
            verifyDuplicate(verbose);
            setVerbose(propertyValue);
        } else {
            throw new InvalidArgumentException("Unrecognised argument: " + propertyFlag + ".");
        }
    }

    private void setVerbose(String verbose) throws InvalidArgumentException {
        if (VERBOSE_TRUE.contains(verbose)) {
            this.verbose = true;
            return;
        } else if (VERBOSE_FALSE.contains(verbose)) {
            this.verbose = false;
            return;
        }
        throw new InvalidArgumentException("Invalid verbose flag.");
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    private <T> void verifyDuplicate(T current) throws InvalidArgumentException {
        if (current != null) {
            throw new InvalidArgumentException("Duplicate argument.");
        }
    }

    public void execute() throws IOException {
        new FileDownloader(urls, outputDirectory).downloadFiles();
    }

    private void setUrls() throws IOException {
        try (var reader = new FileReader(urlListFile);
             var bufferReader = new BufferedReader(reader)) {
            String url;
            while ((url = bufferReader.readLine()) != null) {
                // Allow for some commenting and formatting.
                if (!url.startsWith("#") && !url.equals("")) {
                    addUrl(url);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Url list file, " + urlListFile + " not found.");
        }
    }

    private void addUrl(String urlString) {
        try {
            var url = new URL(urlString);
            urls.add(url);
        } catch (MalformedURLException ex) {
            invalidUrls.add(urlString);
        }
    }

    public void setDefaultUrlListFile() throws IOException {
        var pwd = new File("").getAbsoluteFile();
        urlListFile = new File(pwd, DEFAULT_URL_LIST).toString();
        setUrls();
    }

    public void setDefaultOutputDirectory() {
        outputDirectory = new File("").getAbsoluteFile();
    }

    public String getUrlListFile() {
        return urlListFile;
    }

    public String toString() {
        if (!TRUE.equals(verbose)) {
            return "";
        }
        var string = new StringBuilder();
        if (urlListFile != null) {
            string.append("\nDownloading from urls in:\n\t").append(urlListFile);
        }
        if (!urls.isEmpty()) {
            string.append("\nurls to be downloaded:");
            for (URL url : urls) {
                string.append("\n\t").append(url);
            }
        }
        if (!invalidUrls.isEmpty()) {
            string.append("\ninvalid urls:");
            for (String url : invalidUrls) {
                string.append("\n\t").append(url);
            }
        }
        if (outputDirectory != null) {
            string.append("\nTo directory:\n\t").append(outputDirectory.getAbsolutePath());
        }

        return string.toString();
    }
}