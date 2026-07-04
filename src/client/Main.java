package client ;

import model.*;
import store.FileStorage ;
import interfaces.Persistance ;
import editor.* ;
import renderer.RenderDocument ;

class Main{
    public static void main(String[] args)
    {
        Document doc = new Document() ;
        DocumentEditor documentEditor = new DocumentEditor(doc);
        Persistance store = new FileStorage() ;

        documentEditor.addText("hi");
        documentEditor.addText("hello");
        documentEditor.addImage("image.png");
        System.out.println(RenderDocument.render(doc)) ;
        store.save(RenderDocument.render(doc)) ;

    }
}