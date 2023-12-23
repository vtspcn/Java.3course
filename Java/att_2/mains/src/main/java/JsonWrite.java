import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonWrite {
    public static void main(String[] args) throws IOException {
        List<Project> projects = new Generator().execute(10);

        new JsonWritener("projects.json").execute(projects);

    }
}
