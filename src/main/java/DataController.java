import com.fasterxml.jackson.databind.ObjectMapper;
import menu.MenuPracticeItem;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class DataController {

    private Random random = new Random();
    private ObjectMapper mapper = new ObjectMapper();

    public List<Asana> getAsanas() throws IOException {
        //ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(Paths.get("src/main/resources/asanas.json").toFile(), Asana[].class));
    }

    // <String(Img), Iteger(ID), Map<id, String(asana name)>>
    public Map<Integer, Asana> getAsanasForQuiz() throws IOException {

        List<Asana> asanaList = this.getAsanas();
        Map<Integer, Asana> asanas = new HashMap<>();// новый лист с 4  уникальными значениями
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

    public String getAsaaInfo(int id) throws IOException {
        return getAsanas().stream().filter( c->c.id == id).findFirst().map(e->e.desc).get();
    }


    public List<Asana> getPractice(Integer[] asanasID) throws IOException {
        List<Asana> asanas = this.getAsanas();
                //охуеть. это было прямо взрыв мозга ) но это не то что нужно) ахахах
                //asanas.stream().filter(e -> Arrays.stream(asanasID).anyMatch(k -> k.equals(e.id))).collect(Collectors.toList()).stream().map(e->e.sanskrit).forEach(System.out::println);
        List<Asana> practiceList = new ArrayList<>();
        for (Integer aId : asanasID) {
            practiceList.add(asanas.stream().filter(e -> e.id == aId).findFirst().get());
        }
        return practiceList;
    }

    public List<MenuPracticeItem> getPracticesFromJson() throws IOException {
        return Arrays.asList(mapper.readValue(Paths.get("src/main/resources/practices.json").toFile(), MenuPracticeItem[].class));
    }

    public MenuPracticeItem getPracticeById(int id) throws IOException {
        return Arrays.stream(mapper.readValue(Paths.get("src/main/resources/practices.json").toFile(), MenuPracticeItem[].class)).filter(e->e.id == id).findFirst().get();
    }
}
