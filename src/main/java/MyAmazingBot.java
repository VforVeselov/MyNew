import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MyAmazingBot extends TelegramLongPollingBot {
    private DataController dataController = new DataController();

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

        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            SendMessage message = new SendMessage();
            SendPhoto sendPhoto = new SendPhoto();
            message.setChatId(update.getMessage().getChatId().toString());

            if (update.getMessage().getText().equals("/memorize")) {
                    System.out.println("memorize");
                QuizClass quizClass = new QuizClass();
                quizClass.sendQuestion(sendPhoto,dataController,update.getMessage().getChatId());

                try {
                    execute(sendPhoto);
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (update.getMessage().getText().equals("/practice")) {
                    System.out.println("practice");
                PracticeController practice = new PracticeController();
                try {
                    LinkedList<Asana> practiceList = practice.getPractice(new Integer[]{3, 6, 7, 9,10,9,7,6,3});
                    startPractice(practiceList,update,7000);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if (update.hasCallbackQuery()) {

            if (!update.getCallbackQuery().getData().equals("-")) { // правильный ответ
                if (update.getCallbackQuery().getData().equals("yestomorequiz")) {
                    System.out.println("ssssss");
                    SendMessage message = new SendMessage();
                    SendPhoto sendPhoto = new SendPhoto();
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());

                    QuizClass quizClass = new QuizClass();
                    quizClass.sendQuestion(sendPhoto,dataController,update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(sendPhoto);
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                SendMessage new_message = new SendMessage();
                new_message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                new_message.setText(Settings.rightAnswer.get(new Random().nextInt(Settings.rightAnswer.size()))); // один из вариантов верного ответа

                SendMessage infoMessage = new SendMessage();
                infoMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                try {
                    infoMessage.setText(dataController.getAsaaInfo(Integer.valueOf(update.getCallbackQuery().getData())));

                    Message response = execute(new_message);
                    // удаляем к херам
                    this.deleteMessage(response, update, 1000);

                    Message info = execute(infoMessage);
                    this.deleteMessage(info, update, 7000);

                    Message quizMessage = update.getCallbackQuery().getMessage();
                    deleteMessage(quizMessage, update, 7000);
                    // что-то не работает ((((
                    deleteMessage(
                            execute(
                                    SendMessage.builder()
                                    .text("Еще?")
                                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                                            .replyMarkup(InlineKeyboardMarkup.builder()
                                                    .keyboardRow(List.of(InlineKeyboardButton.builder()
                                                            .text("Yes")
                                                            .callbackData("yestomorequiz")
                                                            .build()
                                                    )).build()
                                            )
                                    .build()
                            ),
                            update, 7000);

                    ///execute(more);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }  else {
                System.out.println("YEEEEEES1");
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

    private void startPractice(LinkedList<Asana> asanaLinkedList,Update update, int time) {
        Thread run = new Thread(() -> {
            try {
                SendMessage practiceMessage = new SendMessage();
                SendPhoto practicePhoto = new SendPhoto();

                practiceMessage.setChatId(update.getMessage().getChatId());
                practicePhoto.setChatId(update.getMessage().getChatId());

                for (Asana asana: asanaLinkedList) {
                    practiceMessage.setText(asana.sanskrit);
                    practicePhoto.setPhoto(new InputFile(asana.img));
                    execute(practicePhoto);
                    execute(practiceMessage);
                    Thread.sleep(time);
                    //System.out.println(asana.sanskrit);
                }
            } catch (InterruptedException | TelegramApiException e) {
                throw new RuntimeException(e);
            }

        });
        run.start();
    }
}
