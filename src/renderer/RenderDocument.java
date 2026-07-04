package renderer ;
import model.Document ;

public class RenderDocument {

    public static String render(Document doc){
        StringBuilder result = new StringBuilder() ;
        doc.getDoc().forEach(ele -> {
           result.append( ele.render()) ;
        });

        return result.toString() ;

    }
}