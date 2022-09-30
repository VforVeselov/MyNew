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
        Map<Integer, Asana> asanas = new HashMap<>();// новый лист с 4 значениями

        for (int i = 0; i < 4; i++) {
            asanas.put(i, asanaList.get(new Random().nextInt(asanaList.size())));
        }

        return asanas;
    }

    public String getAsaaInfo(Integer id) throws IOException {
        System.out.println(id);
        return getAsanas().stream().filter( c->c.id == id).findFirst().map(e->e.desc).get();
    }
}
