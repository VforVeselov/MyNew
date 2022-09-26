import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.bytecode.Descriptor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class DataController {

    public List<Asana> getAsanas() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Asana> asanaList = Arrays.asList(mapper.readValue(Paths.get("src/main/resources/asanas.json").toFile(), Asana[].class));
        return asanaList;
    }

    // <String(Img), Iteger(ID), Map<id, String(asana name)>>
    public Map<Integer, Asana> getAsanasForQuiz() throws IOException {

        List<Asana> asanaList = this.getAsanas();
        Map<Integer, Asana> asanas = new HashMap<>();// новый лист с 4 значениями

        for (int i = 0; i < 4; i++) {
            asanas.put(i, asanaList.get(new Random().nextInt(asanaList.size())));
        }

        Map<Integer, Asana> result = asanas;
        return result;
    }
}
