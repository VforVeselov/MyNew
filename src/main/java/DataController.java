import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class DataController {

    private Random random = new Random();

    private ObjectMapper mapper = new ObjectMapper();

    public List<Asana> getAsanas() throws IOException {
        log.trace("readig asanas file: {}", this.getClass().getResource("asanas.json"));
        return Arrays.asList(mapper.readValue(this.getClass().getResource("asanas.json"), Asana[].class));
        //return Arrays.asList(mapper.readValue(Paths.get("src/main/resources/asanas.json").toFile(), Asana[].class));
    }

    // <String(Img), Iteger(ID), Map<id, String(asana name)>>
    public Map<Integer, Asana> getAsanasForQuiz() throws IOException {

        List<Asana> asanaList = this.getAsanas();
        Map<Integer, Asana> asanas = new HashMap<>(); // новый лист с 4 уникальными значениями
        int i = 0;
        while (i < 4) {
            Asana a = asanaList.get(this.random.nextInt(asanaList.size()));
            if (!asanas.containsValue(a)) {
                asanas.put(i, a);
                i++;
            }
        }

        return asanas;
    }

    public String getAsanaInfo(int id) throws IOException {
        if (getAsanas().stream().filter(c -> c.id == id).findFirst().map(e -> e.desc).get().isEmpty()) {
            return "Именно " + getAsanas().stream().filter(c -> c.id == id).findFirst().map(e -> e.sanskrit).get();
        } else {
            return getAsanas().stream().filter(c -> c.id == id).findFirst().map(e -> e.desc).get();
        }
    }

    public List<Asana> getPractice(Integer[] asanasId) throws IOException {
        List<Asana> asanas = this.getAsanas();
        List<Asana> practiceList = new ArrayList<>();
        for (Integer asanaId : asanasId) {
                Asana asana = asanas.stream().filter(e -> e.id == asanaId).findFirst().orElse(null);
                if (asana != null) {
                    practiceList.add(asana);
                }
        }
        return practiceList;
    }

}
