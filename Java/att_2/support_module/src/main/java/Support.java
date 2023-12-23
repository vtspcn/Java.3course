import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Support {


    public static String toString(List<String> strings){
        StringBuilder res = new StringBuilder();

        for (String s: strings){
            res.append(" ").append(s).append(" ");
        }

        return res.toString();
    }



}
