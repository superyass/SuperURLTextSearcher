package com.superyass.superUrlTextSearcher;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author SuperYass
 */
public class Main {

    @Parameter(names = {"-input", "-in"}, description = "Input folder :")
    private static String inputFolder = "C:\\";

    @Parameter(names = {"-search", "-s"}, description = "comma separated search keywords")
    private static List<String> search;

    @Parameter(names = {"-ExcludedSearch", "-xs"}, description = "comma separated keywords to be excluded")
    private static List<String> ExcludedSearch;

    @Parameter(names = {"-addExtensions", "-addExt", "-ae"}, description = "comma separated extensions to be added")
    private static List<String> addedExtensions;

    @Parameter(names = {"-excludExtensions", "-execExt", "-exce"}, description = "comma separated extensions to be excluded")
    private static List<String> excludedExtensions;

    @Parameter(names = {"-exclExtensions", "-exclExt", "-exle"}, description = "comma separated extensions to be exclusively looked for")
    private static List<String> exclusifExtensions;

    @Parameter(names = "-debug", description = "Debug mode")
    private static boolean debug = false;

    @Parameter(names = {"-h", "-help", "--help"}, description = "help mode")
    private static boolean help = false;

    public static final String[] defaultExtensions = {"txt", "md", "java", "php", "py", "c", "pl", "rb", "cs","json","smali"};

    public static String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";

    public static void main(String[] args) {

        final String[] argv = {"-s", "chil3ba,chil3ba2"};
        final Main options = new Main();
        final JCommander jcmdr = new JCommander(options, args);

        //args management
        jcmdr.setProgramName("SuperURLTextSearcher");
        jcmdr.setAllowAbbreviatedOptions(true);

        if (help) {
            jcmdr.usage();
        }

        if (debug) {
            Logger.getGlobal().setLevel(Level.INFO);
        } else {
            Logger.getGlobal().setLevel(Level.SEVERE);
        }

        File dir = new File(inputFolder);
        if (dir.isDirectory()) {
            getFiles(dir);
        } else {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Input path not a valid folder");
        }
    }

    public static List<File> getFiles(File f) {
        List<File> out = new ArrayList<>();
        List<File> files = Arrays.asList(f.listFiles());
        files.stream().forEach((aFile) -> {
            if (aFile.isDirectory()) {
                getFiles(aFile);
            } else {
                String extension = getFileExtension(aFile);
                if (isExtensionSupported(extension)) {
                    try (Scanner scanner = new Scanner(aFile)) {
                        int lineNumber = 0;
                        while (scanner.hasNext()) {
                            lineNumber++;
                            List<String> urls = extractUrls(scanner.nextLine());
                            if (urls != null && !urls.isEmpty()) {
                                System.out.print("[" + aFile.getCanonicalPath().replace(inputFolder, "") + "](line:" + lineNumber + "): ");
                                for (String url : urls) {
                                    System.out.print(url + "  ");
                                }
                                System.out.println("");
                            }
                        }

                    } catch (IOException e) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage());
                    }
                }
            }
        });
        return out;
    }

    public static boolean isExtensionSupported(String ext) {
        //if exclusif search is empty test for the default extensions
        if (exclusifExtensions==null || exclusifExtensions.isEmpty()) {
            //Logger.getLogger(Main.class.getName()).log(Level.INFO, "Extensions looked for: " + defaultExtensions);
            for (String extension : defaultExtensions) {
                if (extension.equalsIgnoreCase(ext)) {
                    return true;
                }
            }
        } else {
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Extensions looked for: " + exclusifExtensions);
            for (String extension : exclusifExtensions) {
                if (extension.equalsIgnoreCase(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<>();

        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

}
