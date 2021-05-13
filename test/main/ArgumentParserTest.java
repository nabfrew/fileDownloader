package main;

import exceptions.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static main.ArgumentParser.HELP_FLAG;
import static main.ArgumentParser.OUTPUT_DIRECTORY_FLAG;
import static main.ArgumentParser.URL_LIST_FLAG;
import static main.Command.DEFAULT_URL_LIST;
import static main.Command.Mode.DOWNLOAD;
import static main.Command.Mode.HELP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentParserTest {

    private static final String HELP_FLAG_TERSE = HELP_FLAG.get(0);
    private static final String DIRECTORY_FLAG_TERSE = OUTPUT_DIRECTORY_FLAG.get(0);
    private static final String URL_LIST_FLAG_TERSE = URL_LIST_FLAG.get(0);
    private static File tmpDir;

    @BeforeEach
    void setUp(@TempDir Path path) {
        tmpDir = path.toAbsolutePath().toFile();
    }

    // Valid inputs:

    @Test
    void defaultOptions() throws InvalidArgumentException {
        String[] args = {};
        var command = new ArgumentParser(args).parse();
        assertEquals(DOWNLOAD, command.getMode());
        assertEquals(new File("").getAbsoluteFile(), command.getOutputDirectory());
        assertEquals(new File("", DEFAULT_URL_LIST).getAbsoluteFile(), command.getUrlListFile());
    }

    @Test
    void setsDirectory() throws InvalidArgumentException {
        String downloadDir = "downloaded";

        String[] args = {DIRECTORY_FLAG_TERSE, new File(tmpDir, "downloaded").getAbsolutePath()};

        var command = new ArgumentParser(args).parse();
        assertEquals(new File(tmpDir, downloadDir).getAbsolutePath(), command.getOutputDirectory().toString());
    }

    @Test
    void setsFile() throws InvalidArgumentException {
        String filename = "file.txt";

        String[] args = {URL_LIST_FLAG_TERSE, new File(tmpDir, filename).getAbsolutePath()};

        var command = new ArgumentParser(args).parse();
        assertEquals(new File(tmpDir, filename).getAbsolutePath(), command.getUrlListFile().getPath());
    }

    @Test
    void setsHelp() throws InvalidArgumentException {
        String[] args = {HELP_FLAG_TERSE};

        var command = new ArgumentParser(args).parse();
        assertEquals(HELP, command.getMode());
    }

    // Invalid inputs

    @Test
    void duplicateFlag() {
        String[] args = {URL_LIST_FLAG_TERSE, "a", URL_LIST_FLAG_TERSE, "b"};
        assertThrows(InvalidArgumentException.class, () -> new ArgumentParser(args).parse());
    }

    /*
    TODO paramaterise for terse/verbose args?
     */
}