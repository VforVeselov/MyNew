import org.glassfish.grizzly.streams.StreamInput;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MyAmazingBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return Settings.botName;
    }

    @Override
    public String getBotToken() {
        return Settings.token;
        //return "1591576699:AAF_E5hPn_BLQJ6K4WC0CnxSC8-3iGnjmSw";
    }

    @Override
    public void onUpdateReceived(Update update) {

        DataController dataController = new DataController();


        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            SendMessage message = new SendMessage();
            SendPhoto sendPhoto = new SendPhoto();
            message.setChatId(update.getMessage().getChatId().toString());

            if (update.getMessage().getText().equals("/memorize")) {
                QuizClass quizClass = new QuizClass();
                quizClass.sendQuestion(sendPhoto,dataController,update.getMessage().getChatId());

            }
            try {
                //execute(message);
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {

            if (!update.getCallbackQuery().getData().equals("-")) { //
                SendMessage new_message = new SendMessage();
                System.out.println("new message");
                new_message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                new_message.setText("Верно!");
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
                }
            } else {
                SendMessage new_message = new SendMessage();
                new_message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                new_message.setText("Лошара!");
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
                }
            }

            System.out.println(update.getCallbackQuery().getData());
//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
            //System.out.println(update.getCallbackQuery().getData() + " 1");
            //execute(message);
        }

    }
}
