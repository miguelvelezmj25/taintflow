package edu.cmu.cs.mvelezce.format;

import java.io.*;

public class Formatter {

    private static final String SOURCE_IMPORT = "import edu.cmu.cs.mvelezce.analysis.option.Source;";
    private static final String SOURCE_METHOD_PREFIX = "Source.getOption";
    private static final String OPTION_PREFIX = "Boolean.valueOf(args";

    public static void format(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        boolean formatted = false;
        String strLine;

        while ((strLine = br.readLine()) != null) {
            if(strLine.contains("Boolean.valueOf(")){
                formatted = true;
                break;
            }
        }

        in.close();

        if(!formatted) {
            System.out.println(filePath + " was not formatted");
            return ;
        }

        file = new File(filePath);
        fstream = new FileInputStream(file);
        in = new DataInputStream(fstream);
        br = new BufferedReader(new InputStreamReader(in));
        StringBuilder formattedFile = new StringBuilder();

        while ((strLine = br.readLine()) != null) {
            String newLine = strLine;

            if(strLine.contains(Formatter.OPTION_PREFIX)){
                String option = strLine.substring(0, strLine.indexOf("=")).trim();
                StringBuilder sourceLine = new StringBuilder();
                sourceLine.append(option);
                sourceLine.append(" = ");
                sourceLine.append(Formatter.SOURCE_METHOD_PREFIX);
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
                formattedFile.append(Formatter.SOURCE_IMPORT);
            }
        }

        in.close();

        PrintWriter out = new PrintWriter(filePath);
        out.print(formattedFile.toString());
        out.close();
        out.flush();

        System.out.println(filePath + " was formatted");
    }
}
