package editor;

import model.* ;

public class DocumentEditor{
    Document doc ;

    public DocumentEditor(Document doc){
        this.doc =doc ;
    }

    public void addText(String text){
        doc.addElement(new TextElement(text)) ;
    }

    public void addImage(String path){
        doc.addElement(new ImageElement(path)) ;
    }
}