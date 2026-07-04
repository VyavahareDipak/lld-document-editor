package model;
import interfaces.DocumentElement ;

public class TextElement implements DocumentElement{
    String text ;

    public TextElement(String text){
        this.text = text ;
    }

    public String render(){
        return this.text + "\n" ;
    }


}