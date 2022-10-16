package menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Забирает данные из файла practices.json с практиками
     * @return InlineKeyboardMarkup
     * @throws IOException
     */
    public InlineKeyboardMarkup menuBuilder(int parentId) throws IOException {
        List<MenuPracticeItem> items = getPracticesFromJson();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        items = items.stream().filter(e -> e.parentId == parentId).collect(Collectors.toList());
        String menuSalt = parentId == -1 ? "submenu-" : "practice-";

        for (int i = 0; i < items.size(); i++) {
            if (i % 2 == 0) {
                rowList.add(List.of(
                        InlineKeyboardButton.builder()
                                .text(items.get(i).name)
                                .callbackData(menuSalt + items.get(i).id)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(items.get(i + 1).name)
                                .callbackData(menuSalt + items.get(i + 1).id)
                                .build()
                ));
            } else {
                rowList.add(List.of(
                        InlineKeyboardButton.builder()
                                .text(items.get(i).name)
                                .callbackData(menuSalt + items.get(i).id)
                                .build()
                ));
            }
            i++;
        }
        if (parentId > -1) {
            int menuItem = parentId - 1;
            rowList.add(List.of(
                    InlineKeyboardButton.builder()
                            .text("<---- Назад")
                            .callbackData("submenu-" + menuItem)
                            .build()));
        }
        return InlineKeyboardMarkup.builder().keyboard(rowList).build();
    }

    public List<MenuPracticeItem> getPracticesFromJson() throws IOException {
        return Arrays.asList(mapper.readValue(Paths.get("src/main/resources/practices.json").toFile(), MenuPracticeItem[].class));
    }

    public MenuPracticeItem getPracticeById(int id) throws IOException {
        return Arrays.stream(mapper.readValue(Paths.get("src/main/resources/practices.json").toFile(), MenuPracticeItem[].class)).filter(e -> e.id == id).findFirst().get();
    }
}
