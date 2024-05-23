package com.moieen.zlint.jwrapper;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ZLinter {
    private static final Logger LOGGER = Logger.getLogger(ZLinter.class.getName());
    private static String ZLINT_PATH;
    /**
     * Contains the list of predefined lints
     */
    private static List<Lint> lintDetails;

    /**
     * Set the zLint executable directory that contains zLint.exe or zLint
     *
     * @param zlintPath
     */
    public static void setZlintPath(String zlintPath) {
        ZLINT_PATH = zlintPath;
    }

    /**
     * run the lint command and return {@link LintResult}
     *
     * @param command
     * @return
     * @throws ZLintException
     */
    private static LintResult lint(String command) throws ZLintException {
        String s = runCommand(command);
        LinkedTreeMap<String, LinkedTreeMap<String, String>> linkedTreeMap = new Gson().fromJson(s, LinkedTreeMap.class);
        Collection<Lint> allLintDetails = getAvailableLints();
        Collection<Lint> appliedLints = new ArrayList<>();
        for (Lint lint : allLintDetails) {
            LinkedTreeMap<String, String> objectResult = linkedTreeMap.get(lint.getName());
            if (objectResult != null) {
                String result = objectResult.get("result");
                if (result != null) {
                    lint.setResult(result);
                } else {
                    lint.setResult("NA");
                }
                appliedLints.add(lint);
            }
        }
        return new LintResult(appliedLints);
    }

    /**
     * Lint the given file the file must be
     *
     * @param fileToLint
     * @param format     specify the file format
     * @return {@link LintResult}. never return null
     * @throws ZLintException
     */
    public static LintResult lint(String fileToLint, Format format) throws ZLintException {
        String command = "zlint -pretty -format " + format.name() + " \"" + fileToLint + "\"";
        return lint(command);
    }

    /**
     * Lint the given file the file must be
     *
     * @param fileToLint
     * @param format         specify the file format
     * @param includeSources list of lint {@link Source} to include
     * @return {@link LintResult}. never return null
     * @throws ZLintException
     */
    public static LintResult lint(String fileToLint, Format format, Source... includeSources) throws ZLintException {
        if (includeSources.length < 0) {
            return lint(fileToLint, format);
        }
        String command = "zlint -pretty -includeSources " + Arrays.stream(includeSources).map(Enum::name).collect(Collectors.joining(",")) + " -format " + format.name() + " \"" + fileToLint + "\"";
        return lint(command);
    }

    /**
     * Lint the given object i.e a Certificate, CRL or OCSP response bytes
     *
     * @param objectToLint Lint the given object i.e a Certificate, CRL or OCSP response bytes
     * @return {@link LintResult}. never return null
     * @throws ZLintException
     */
    public static LintResult lint(byte[] objectToLint) throws ZLintException {
        Objects.requireNonNull(objectToLint, "Object to lint cannot be null.");
        Path tempFile = null;
        try {
            // Create a temporary file
            tempFile = Files.createTempFile("tempfile", ".tmp");
            LOGGER.log(Level.INFO, "Temporary file created: " + tempFile.toString());

            // Write object bytes to the temporary file
            Files.write(tempFile, objectToLint, StandardOpenOption.WRITE);

            return lint(tempFile.toString(), Format.der);
        } catch (IOException e) {
            throw new ZLintException("Failed to Lint. Error: " + e.getMessage(), e);
        } finally {
            if (tempFile != null) {
                // Delete the temporary file
                try {
                    Files.delete(tempFile);
                    LOGGER.log(Level.INFO, "Temporary file deleted.");
                } catch (IOException e) {
                    System.out.println();
                }
            }
        }
    }

    /**
     * Runs the command
     *
     * @param inputCommand
     * @return
     * @throws ZLintException
     */
    private static String runCommand(String inputCommand) throws ZLintException {
        String os = System.getProperty("os.name").toLowerCase();
        String command;
        if (os.contains("win")) {
            // Command for Windows
            command = "cmd /c " + inputCommand;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Command for Linux/Unix/Mac
            command = "/bin/sh " + inputCommand;
        } else {
            throw new ZLintException("Unsupported OS to Lint");
        }

        try {
            // Create a ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            if (ZLINT_PATH != null) {
                // Set the working directory
                processBuilder.directory(new File(ZLINT_PATH));
            }

            Process process = processBuilder.start();

            String line;
            //check any errors
            //read inputstream
            StringBuilder lintResult = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null) {
                lintResult.append(line).append("\n");
            }
            reader.close();
            if (!lintResult.toString().isEmpty()) {
                return lintResult.toString().trim();

            }

            StringBuilder error = new StringBuilder();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                error.append(line).append("\n");
            }
            errorReader.close();
            if (error.toString().contains("'zlint' is not recognized as an internal or external command")) {
                throw new ZLintException("'zlint' is not found in classpath. Set the zlint executable directory first using setZlintPath() method");
            }
            throw new ZLintException("Lint Error. " + error.toString().trim());

        } catch (IOException e) {
            throw new ZLintException("Failed to Lint. Error: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the unmodifiable collection of Lints available to perform
     *
     * @return
     */
    public static Collection<Lint> getAvailableLints() throws ZLintException {
        if (lintDetails == null) {
            String command = "zlint -list-lints-json";
            String result = runCommand(command);
            lintDetails = new ArrayList<>();
            String[] split = result.split("\n");
            for (String s : split) {
                if (!Objects.equals(s, "")) {
                    lintDetails.add(Lint.fromJson(s));
                }
            }
        }
        return Collections.unmodifiableCollection(lintDetails);
    }

    /**
     * format of the file to be lint
     */
    public enum Format {
        pem, der, base64
    }
}
