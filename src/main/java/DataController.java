import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class DataController {

    public List<Asana> getAsanas() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(Paths.get("src/main/resources/asanas.json").toFile(), Asana[].class));
    }

    // <String(Img), Iteger(ID), Map<id, String(asana name)>>
    public Map<Integer, Asana> getAsanasForQuiz() throws IOException {

        List<Asana> asanaList = this.getAsanas();
        Map<Integer, Asana> asanas = new HashMap<>();// новый лист с 4  уникальными значениями
        int i = 0;
        while (i < 4) {
            Asana a = asanaList.get(new Random().nextInt(asanaList.size()));
            if (!asanas.containsValue(a)) {
                asanas.put(i, a);
                i++;
            }
        }

        return asanas;
    }

    public String getAsaaInfo(Integer id) throws IOException {
        return getAsanas().stream().filter( c->c.id == id).findFirst().map(e->e.desc).get();
    }
}
