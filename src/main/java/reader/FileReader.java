package reader;

import parser.Parser;

import java.io.*;

/**
 * Created by Jaszczynski.Rafal on 21.02.2018.
 */
public class FileReader {

    private static final String fileToReadFrom = "D:\\Programowanko\\jaszc\\Intelij_projects\\doit\\files\\flashCode.txt";
    private static final String fileToWrite = "D:\\Programowanko\\jaszc\\Intelij_projects\\doit\\files\\javaCodeByRJ.txt";

    private static StringBuilder stringBuilder = new StringBuilder();

    public static void readTextFileWithActionScript() throws IOException {

        // Open the file
        FileInputStream fstream = new FileInputStream(fileToReadFrom);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String line;

//Read File Line By Line
        while ((line = br.readLine()) != null)   {
            // Print the content on the console
            System.out.println (line);
            //changes
        }
//Close the input stream
        br.close();
    }

    public static void saveFile() throws IOException {

        BufferedWriter writer = null;
        try  {
//            writer = new BufferedWriter(new FileWriter(fileToWrite));
//            writer.write(stringBuilder.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        Parser.logicForLines("var componentId:String = event.target[\"id\"];");
    }
}