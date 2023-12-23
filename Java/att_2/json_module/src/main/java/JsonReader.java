import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class JsonReader {
    private String filename;

    public JsonReader(String filename) {
        this.filename = filename;
    }

    public List<Project> execute() throws IOException {
        return new ObjectMapper().readValue(new FileInputStream(filename),
                new TypeReference<List<Project>>() {});
    }


}
