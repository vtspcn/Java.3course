import java.io.IOException;
import java.util.List;

public class JsonRead {
    public static void main(String[] args) throws IOException {
        List<Project> projects = new JsonReader("projects.json").execute();

        for (Project p:projects) {
            System.out.println(p);
        }
    }
}
