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

    public InlineKeyboardMarkup menuBuilder() throws IOException {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<MenuPracticeItems> items = getPracticesFromJson();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            if (i % 2 == 0) {
                rowList.add(List.of(
                        InlineKeyboardButton.builder()
                                .text(items.get(i).name)
                                .callbackData(items.get(i).name)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(items.get(i+1).name)
                                .callbackData(items.get(i+1).name)
                                .build()
                ));
            } else {
                rowList.add(List.of(
                        InlineKeyboardButton.builder()
                                .text(items.get(i).name)
                                .callbackData(items.get(i).name)
                                .build()
                ));
            }
            i++;
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public List<MenuPracticeItems> getPracticesFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(Paths.get("src/main/resources/practices.json").toFile(), MenuPracticeItems[].class));
    }
}
