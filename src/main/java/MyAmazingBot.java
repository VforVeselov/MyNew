import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;

public class MyAmazingBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return Settings.botName;
    }

    @Override
    public String getBotToken() {
        return Settings.token;
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

                try {
                    execute(sendPhoto);
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {

            if (!update.getCallbackQuery().getData().equals("-")) { // правильный ответ
                SendMessage new_message = new SendMessage();
                new_message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                new_message.setText(Settings.rightAnswer.get(new Random().nextInt(Settings.rightAnswer.size()))); // один из вариантов верного ответа

                SendMessage infoMessage = new SendMessage();
                infoMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                try {
                    infoMessage.setText(dataController.getAsaaInfo(Integer.valueOf(update.getCallbackQuery().getData())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

//                SendMessage more = new SendMessage();
//                more.setChatId(update.getCallbackQuery().getMessage().getChatId());
//                more.setText("More?");
//                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//                        List<InlineKeyboardButton> inlineKeyboardRow1 = new ArrayList<>();
//                            InlineKeyboardButton yesButton = new InlineKeyboardButton();
//                                    yesButton.setText("Да");
//                                    yesButton.setCallbackData("y");
//                            InlineKeyboardButton noButton = new InlineKeyboardButton();
//                                    noButton.setText("Нет");
//                                    noButton.setCallbackData("n");
//                            inlineKeyboardRow1.add(yesButton);
//                            inlineKeyboardRow1.add(noButton);
//                        inlineKeyboardMarkup.setKeyboard(Arrays.asList(inlineKeyboardRow1));
//                        more.setReplyMarkup(inlineKeyboardMarkup);
                try {

                    Message response = execute(new_message);
                    // удаляем к херам
                    this.deleteMessage(response, update, 1000);

                    Message info = execute(infoMessage);
                    this.deleteMessage(info, update, 7000);

                    Message quizMessage = update.getCallbackQuery().getMessage();
                    deleteMessage(quizMessage, update, 7000);

                    ///execute(more);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                SendMessage new_message = new SendMessage();
                new_message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                new_message.setText(Settings.errorMessage);
                try {
                    Message response = execute(new_message);
                    this.deleteMessage(response, update, 1000);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void deleteMessage(Message response, Update update, int time) {
        Thread run = new Thread(() -> {
            try {
                Thread.sleep(time);
                DeleteMessage del = new DeleteMessage();
                del.setChatId(update.getCallbackQuery().getMessage().getChatId());
                //System.out.println(response.getMessageId());
                del.setMessageId(response.getMessageId());
                execute(del);
            } catch (InterruptedException | TelegramApiException e) {
                throw new RuntimeException(e);
            }

        });
        run.start();
    }
}
