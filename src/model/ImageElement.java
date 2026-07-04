package model;

import interfaces.DocumentElement ;

public class ImageElement implements DocumentElement{

     String imagePath ;

    public ImageElement(String path){
        imagePath = path ;
    }

   @Override
   public String render(){
        return "[Image : " + this.imagePath + " ]\n";
    }

}