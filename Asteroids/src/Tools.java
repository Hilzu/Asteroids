
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hilzu
 */
class Tools {

    static String readStringFromFile(String path) throws IOException {
        File fileHandle = new File(path);
        BufferedReader inputReader = null;
        try {
            inputReader = new BufferedReader(new FileReader(fileHandle));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            System.exit(1);
        }

        String line = null;
        StringBuilder string = new StringBuilder();

        try {
            while (true) {

                line = inputReader.readLine();

                if (line == null) {
                    break;
                }

                string.append(line);
                string.append(System.getProperty("line.separator"));
            }
        } finally {
            inputReader.close();
        }

        return string.toString();
    }
}
