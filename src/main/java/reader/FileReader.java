package reader;

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

    private static String logicForLines(String line) {

        if (line.contains("function")) {
            int funcIndex = line.indexOf("function");
            int lastIndexOfColon = line.lastIndexOf(":");
            String returnTypeWithoutSpace = line.substring(lastIndexOfColon+1,line.length()).trim();
            line = line.substring(0, lastIndexOfColon);
            line = line.replace(" function ", " " + returnTypeWithoutSpace + " ");

            String bracketsWithArguments = line.substring(line.indexOf("("), line.lastIndexOf(")"));
            String[] arguments = bracketsWithArguments.split(":");

            int firstIndexOfColon;
            while ((firstIndexOfColon = line.indexOf(":"))!= -1) {

                String type = line.replaceAll("", "");
            }
        }
        if(line.contains("override")) {
            line ="@Override\n" + line.replace("override ", "");
        }

        System.out.println(line);
        return null;
    }

    public static void saveFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite))) {
            writer.write(stringBuilder.toString());
        }

        logicForLines("override protected function load(event:FlexEvent):void");
    }
}