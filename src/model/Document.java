package model;
import model.ImageElement ;
import model.TextElement ;
import interfaces.DocumentElement ;
import java.util.* ;

public class Document{
    List<DocumentElement>elements ;


    public Document(List<DocumentElement>elements){
        this.elements = elements ;
    }

    public Document(){
        this(new ArrayList<>()) ;
    }


    public void addElement(DocumentElement ele){
        elements.add(ele);
    }

    public List<DocumentElement> getDoc(){
        return this.elements ;
    }
}