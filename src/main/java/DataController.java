import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DataController {

    public List<Asana> getAsanas() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Asana> asanaList = Arrays.asList(mapper.readValue(Paths.get("src/main/resources/asanas.json").toFile(), Asana[].class));
        return asanaList;
    }
}
