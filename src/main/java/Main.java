import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try {

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyAmazingBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        //DataController asanas = new DataController();
        //List<Asana> asanasList = asanas.getAsanas();
        //asanasList.stream().map(e -> e.s).forEach(System.out::println);
        //Asana[] m = mapper.readValue(Paths.get("src/main/resources/asanas.json").toFile(),Asana[].class);
       // Arrays.stream(m).map(e -> e.e + e.s).forEach(System.out::println);
       // Arrays.asList(m).stream().map(e -> e.s).forEach(System.out::println);

    }
}
