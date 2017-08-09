package edu.cmu.cs.mvelezce.format;

import java.io.*;

public class SourceFormatter {

    private static final String SOURCE_IMPORT = "import edu.cmu.cs.mvelezce.analysis.option.Source;";
    private static final String SOURCE_METHOD_PREFIX = "Source.getOption";
    private static final String OPTION_PREFIX = "Boolean.valueOf(args";

    public static void format(String fileName, String classDir) throws IOException, InterruptedException {
        SourceFormatter.formatSources(fileName);
        SourceFormatter.compile(fileName, classDir);
    }

    public static void formatSources(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        boolean formatted = false;
        String strLine;

        while ((strLine = br.readLine()) != null) {
            if(strLine.contains("Boolean.valueOf(")) {
                formatted = true;
                break;
            }
        }

        in.close();

        if(!formatted) {
            System.out.println(filePath + " was not formatted");
            return;
        }

        file = new File(filePath);
        fstream = new FileInputStream(file);
        in = new DataInputStream(fstream);
        br = new BufferedReader(new InputStreamReader(in));
        StringBuilder formattedFile = new StringBuilder();

        while ((strLine = br.readLine()) != null) {
            String newLine = strLine;

            if(strLine.contains(SourceFormatter.OPTION_PREFIX)) {
                String option = strLine.substring(0, strLine.indexOf("=")).trim();
                StringBuilder sourceLine = new StringBuilder();
                sourceLine.append(option);
                sourceLine.append(" = ");
                sourceLine.append(SourceFormatter.SOURCE_METHOD_PREFIX);
                sourceLine.append(option);
                sourceLine.append("(");

                String original = strLine.substring(strLine.indexOf("=") + 1, strLine.indexOf(";")).trim();
                sourceLine.append(original);
                sourceLine.append(");");

                newLine = sourceLine.toString();
            }

            formattedFile.append(newLine);
            formattedFile.append("\n");

            if(strLine.startsWith("package ")) {
                formattedFile.append(SourceFormatter.SOURCE_IMPORT);
            }
        }

        in.close();

        PrintWriter out = new PrintWriter(filePath);
        out.print(formattedFile.toString());
        out.close();
        out.flush();

        System.out.println(filePath + " was formatted");
    }

    public static void compile(String srcFile, String classDir) throws IOException, InterruptedException {
//        String[] command = {"find", srcDir.substring(0, srcDir.length() - 1), "-name", "*.java"};
//        Process process = Runtime.getRuntime().exec(command);
//
//        try (final BufferedReader b = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//            String line;
//
//            while ((line = b.readLine()) != null) {
//                System.out.println(line);
//            }
//        }
//
//        process.waitFor();
//
////        process.waitFor();
////
////        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
////        BufferedWriter writer = new BufferedWriter(new FileWriter(srcDir + "sources.txt"));
////        String line;
////
////        while ((line = reader.readLine()) != null) {
////            writer.write(line);
////            writer.write("\n");
////        }
////
////        writer.close();
//
        String[] command = new String[]{"javac", "-cp", "./target/classes", "-d", classDir, srcFile};
        Process process = Runtime.getRuntime().exec(command);

        try (final BufferedReader b = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;

            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }
        }

        try (final BufferedReader b = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;

            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }
        }

        process.waitFor();


    }
}
