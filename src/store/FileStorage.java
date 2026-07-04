package store;
import java.io.FileWriter;
import java.io.IOException;

import interfaces.Persistance ;
public  class FileStorage implements Persistance{
    @Override
    public void save(String doc){
        try {
            FileWriter outFile = new FileWriter("document.txt");
            outFile.write(doc);
            outFile.close();
            System.out.println("Document saved to document.txt");
        } catch (IOException e) {
            System.out.println("Error: Unable to open file for writing.");
        }
    }
}