package com;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

public class FileDownloader {

    private static final int BUFFER_SIZE = 1024;
    private final File outputDirectory;
    private final List<URL> urls;

    public FileDownloader(List<URL> urls, File outputDirectory) {
        this.urls = urls;
        this.outputDirectory = outputDirectory;
    }

    public void downloadFiles() throws IOException {
        if (outputDirectory.mkdir()) {
            System.out.println("creating new output directory:" + outputDirectory.getAbsolutePath());
        }

        for (var url : urls) {
            var outFile = new File(outputDirectory, getCrossPlatformSafeFilename(url));
            if (!outFile.createNewFile()) {
                System.out.println("Skipping download, already exists: " + url);
                continue;
            }
            try (var in = new BufferedInputStream(url.openStream());
                 var fileOutputStream = new FileOutputStream(outFile)) {
                var dataBuffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, BUFFER_SIZE)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (UnknownHostException ex) {
                outFile.delete();
                System.out.println("Skipping file, failed to connect to: " + url);
            } catch (IOException ex) {
                outFile.delete();
                System.out.println("Downloading url failed:" + url + ".\n" +ex.getMessage());
            }
        }
    }

    // Strip protocol, keep it to alpha-numeric, just to be sure.
    private String getCrossPlatformSafeFilename(URL url){
        var urlString = url.getFile();

        int extIndex = urlString.lastIndexOf(".");
        if (extIndex < 0) {
            return safeCharacters(urlString);
        }
        return safeCharacters(urlString.substring(0, extIndex))
                + "." + safeCharacters(urlString.substring(extIndex + 1));
    }

    private String safeCharacters(String str) {
        return str.replaceAll("[^A-Za-z0-9]","_");
    }
}
