package com;

import dlexceptions.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static com.ArgumentParser.HELP_FLAG;
import static com.ArgumentParser.OUTPUT_DIRECTORY_FLAG;
import static com.ArgumentParser.URL_LIST_FLAG;
import static com.ArgumentParser.VERBOSE_FALSE;
import static com.ArgumentParser.VERBOSE_FLAG;
import static com.Command.DEFAULT_URL_LIST;
import static com.Command.Mode.DOWNLOAD;
import static com.Command.Mode.HELP;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentParserTest {

    private static final String HELP_FLAG_TERSE = HELP_FLAG.get(0);
    private static final String DIRECTORY_FLAG_TERSE = OUTPUT_DIRECTORY_FLAG.get(0);
    private static final String VERBOSE_FLAG_TERSE = VERBOSE_FLAG.get(0);
    private static final String VERBOSE_FALSE_TERSE = VERBOSE_FALSE.get(0);
    private static final String URL_LIST_FLAG_TERSE = URL_LIST_FLAG.get(0);
    private static final String[] URLS = {"www.url.com", "www.url2.com"};
    private static File tmpDir;

    @BeforeEach
    void setUp(@TempDir Path path) {
        tmpDir = path.toAbsolutePath().toFile();
    }

    // Valid inputs:

    @Test
    void defaultOptions() throws InvalidArgumentException, IOException {
        String[] args = {};
        File pwd = new File("").getAbsoluteFile();
        var command = new ArgumentParser(args).parse();
        assertEquals(DOWNLOAD, command.getMode());
        assertEquals(new File("").getAbsoluteFile(), command.getOutputDirectory());
        assertEquals(new File(pwd, DEFAULT_URL_LIST).getAbsolutePath(), command.getUrlListFile());
        assertEquals(TRUE, command.getVerbose());
    }

    @Test
    void setsDirectory() throws InvalidArgumentException, IOException {
        String downloadDir = "downloaded";

        String[] args = {DIRECTORY_FLAG_TERSE, new File(tmpDir, "downloaded").getAbsolutePath()};

        var command = new ArgumentParser(args).parse();
        assertEquals(new File(tmpDir, downloadDir).getAbsolutePath(), command.getOutputDirectory().toString());
    }

    @Test
    void setsVerbose() throws InvalidArgumentException, IOException {
        String[] args = {VERBOSE_FLAG_TERSE, VERBOSE_FALSE_TERSE};

        var command = new ArgumentParser(args).parse();
        assertEquals(FALSE, command.getVerbose());
    }

    @Test
    void setsFile() throws InvalidArgumentException, IOException {
        String filename = "file.txt";
        File file = new File(tmpDir, filename).getAbsoluteFile();
        writeFile(filename);

        String[] args = {URL_LIST_FLAG_TERSE, file.getAbsolutePath()};

        var command = new ArgumentParser(args).parse();
        assertEquals(new File(tmpDir, filename).getAbsolutePath(), command.getUrlListFile());
        assertArrayEquals(URLS, command.getUrls().toArray());
    }

    @Test
    void setsHelp() throws InvalidArgumentException, IOException {
        String[] args = {HELP_FLAG_TERSE};

        var command = new ArgumentParser(args).parse();
        assertEquals(HELP, command.getMode());
    }

    // Invalid inputs

    @Test
    void duplicateFlag() {
        String[] args = {DIRECTORY_FLAG_TERSE, "a", DIRECTORY_FLAG_TERSE, "b"};
        assertThrows(InvalidArgumentException.class, () -> new ArgumentParser(args).parse());
    }

    @Test
    void unrecognisedFlag() {
        String[] args = {"-f", "a"};
        assertThrows(InvalidArgumentException.class, () -> new ArgumentParser(args).parse());
    }
    // Helper methods

    private void writeFile(String file) throws IOException {
        try(var writer = new FileWriter(new File(tmpDir, file)); var bufferedWriter = new BufferedWriter(writer)) {
            for (var url : ArgumentParserTest.URLS) {
                bufferedWriter.write(url);
                bufferedWriter.newLine();
            }
        }
    }

    /*
    TODO paramaterise for terse/verbose args?
     */
}