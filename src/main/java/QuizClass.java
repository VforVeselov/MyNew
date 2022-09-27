import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class QuizClass {
    public QuizClass() {
    }

    public void sendQuestion(SendPhoto sendPhoto, DataController dataController, Long chatId) {
        try {

            Map<Integer, Asana> asanaMap = dataController.getAsanasForQuiz();
            List<String> s = dataController.getAsanas().stream().map(e->e.sanskrit).collect(Collectors.toList());

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> inlineKeyboardRow1 = new ArrayList<>();
            List<InlineKeyboardButton> inlineKeyboardRow2 = new ArrayList<>();

            Integer rightAnswer = new Random().nextInt(4);
            System.out.println(" -- Правильный id - "+ rightAnswer);
            String image = "";

            for (int i = 0; i < 4; i++) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(asanaMap.get(i).sanskrit);
                if (i == rightAnswer) {
                    image = asanaMap.get(i).img;
                    inlineKeyboardButton.setCallbackData(String.valueOf(i));
                } else {
                    inlineKeyboardButton.setCallbackData("-");
                }

                if (i == 0 || i == 1) {
                    inlineKeyboardRow1.add(inlineKeyboardButton);
                } else {
                    inlineKeyboardRow2.add(inlineKeyboardButton);
                }

            }

            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(inlineKeyboardRow1);
            rowList.add(inlineKeyboardRow2);
            inlineKeyboardMarkup.setKeyboard(rowList);

            //message.setReplyMarkup(inlineKeyboardMarkup);
            //message.setText(image);


            sendPhoto.setChatId(chatId);
            sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
            sendPhoto.setPhoto(new InputFile(image));
        } catch (IOException e) {
            System.out.println("error" + e);
            throw new RuntimeException(e);
        }
    }
}