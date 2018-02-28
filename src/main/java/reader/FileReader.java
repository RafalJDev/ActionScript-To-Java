package reader;

import parser.Parser;

import java.io.*;

/**
 * Created by Jaszczynski.Rafal on 21.02.2018.
 */
public class FileReader {

    private static final String fileToReadFrom = System.getProperty("user.dir") + "\\ActionScript-To-Java\\files\\flashCodeJustActionScript.txt";
    private static final String fileToWrite = System.getProperty("user.dir") + "\\ActionScript-To-Java\\files\\javaCodeByRJ.txt";

    private static StringBuilder stringBuilder = new StringBuilder();

    public static void readTextFileWithActionScript() throws IOException {

        // Open the file
        FileInputStream fstream = new FileInputStream(fileToReadFrom);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));


        String line;
        int countEmptyLines = 0;
//Read File Line By Line
        Parser.initializeReplaceMap();
        while ((line = br.readLine()) != null) {
            line = Parser.logicForLines(line);

            if (line.isEmpty()) {
                countEmptyLines++;
            } else {
                countEmptyLines = 0;
            }
            if (countEmptyLines > 1) {
                continue;
            }
            if (line.equals("AddMethods")) {
                String methods = Parser.addDbQueryAndElementFieldsMethods();
                stringBuilder = stringBuilder.replace(stringBuilder.lastIndexOf("}"), stringBuilder.length(),
                        methods +"\n  }\n\n");
            }

            stringBuilder.append(line + "\n");
        }
        stringBuilder.append("}");

        stringBuilder = Parser.secondParsingForAddingComponents(stringBuilder, "HANYS");

        System.out.println(stringBuilder);
//Close the input stream
        br.close();
    }

    public static void saveFile() throws IOException {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileToWrite));
            writer.write(stringBuilder.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}