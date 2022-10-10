package menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu {
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Забирает данные из файла practices.json с практиками
     * @return
     * @throws IOException
     */
    public InlineKeyboardMarkup menuBuilder() throws IOException {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<MenuPracticeItem> items = getPracticesFromJson();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            if (i % 2 == 0) {
                rowList.add(List.of(
                        InlineKeyboardButton.builder()
                                .text(items.get(i).name)
                                .callbackData("practice-"+items.get(i).id)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(items.get(i+1).name)
                                .callbackData("practice-"+items.get(i+1).id)
                                .build()
                ));
            } else {
                rowList.add(List.of(
                        InlineKeyboardButton.builder()
                                .text(items.get(i).name)
                                .callbackData("practice-"+items.get(i).id)
                                .build()
                ));
            }
            i++;
        }
        rowList.add(List.of(
                InlineKeyboardButton.builder()
                        .text("<---- Назад")
                        .callbackData("back")
                        .build()));
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public List<MenuPracticeItem> getPracticesFromJson() throws IOException {
        return Arrays.asList(mapper.readValue(Paths.get("src/main/resources/practices.json").toFile(), MenuPracticeItem[].class));
    }

    public MenuPracticeItem getPracticeById(int id) throws IOException {
        return Arrays.stream(mapper.readValue(Paths.get("src/main/resources/practices.json").toFile(), MenuPracticeItem[].class)).filter(e->e.id == id).findFirst().get();
    }
}
