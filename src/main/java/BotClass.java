import lombok.extern.slf4j.Slf4j;
import menu.Menu;
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

import java.beans.XMLEncoder;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class BotClass  extends TelegramLongPollingBot {

    private final DataController dataController = new DataController();
    private Random random = new Random();
    private SendMessage message = new SendMessage();
    private SendPhoto sendPhoto = new SendPhoto();


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


        // Отлавливаем все сообщения
        if (update.hasMessage() && update.getMessage().hasText()) {
            message.setChatId(update.getMessage().getChatId().toString());
            // start
            if (update.getMessage().getText().equals("/start")) {
                message.setText("Привет! \n Этот бот поможет тебе выучить асаны в форме теста \n жмякни /memorize и поехали!");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            // memorize
            if (update.getMessage().getText().equals("/memorize")) {
                log.info("User {} запустил memorize",update.getMessage().getFrom().getUserName());
                QuizClass quizClass = new QuizClass();
                quizClass.sendQuestion(sendPhoto,dataController,update.getMessage().getChatId());
                    try {
                        execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
            }
            // practice
            if (update.getMessage().getText().equals("/practice")) {
                log.trace("User {} запустил practice",update.getMessage().getFrom().getUserName());
                //dataController.getAsaaInfo()
                //dataController.getPractice()
//                PracticeController practice = new PracticeController();
//                try {
//                    LinkedList<Asana> practiceList = practice.getPractice(new Integer[]{3, 12, 6, 13, 14, 11, 9, 15, 14, 11, 9, 15, 14, 11, 9, 13, 6, 12, 3});
//                    startPractice(practiceList,update,7000);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
                // TODO чота все поломалось )
                message.setText("Выбери практику");
                try {
                    message.setReplyMarkup(new Menu().menuBuilder());
                    execute(message);
                } catch (IOException | TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        // Отлавливаем коллбеки из клаиауры
        else if (update.hasCallbackQuery()) {
            message.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
            // если нужно продолжить квиз
            if (update.getCallbackQuery().getData().equals("yestomorequiz")) {
                QuizClass quizClass = new QuizClass();
                quizClass.sendQuestion(sendPhoto,dataController,update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
            }
            // если НЕправильный ответ
            if (update.getCallbackQuery().getData().equals("-")) {
                log.trace("ответ неверный");
                SendMessage newMessage = new SendMessage();
                newMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                newMessage.setText(Settings.errorMessage);
                try {
                    Message response = execute(newMessage);
                    this.deleteMessage(response, update, 1000);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            // Если ответ правильный ra{id}
            if (update.getCallbackQuery().getData().startsWith("ra")) {
                message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                message.setText(Settings.rightAnswer.get(random.nextInt(Settings.rightAnswer.size()))); // один из вариантов верного ответа

                SendMessage infoMessage = new SendMessage();
                infoMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                try {
                    Integer asanaId = null;
                    asanaId = Integer.parseInt(update.getCallbackQuery().getData().substring(2)); //обрезаем ra
                    infoMessage.setText(dataController.getAsaaInfo(asanaId));

                    Message response = execute(message);
                    // удаляем к херам
                    this.deleteMessage(response, update, 1000);

                    Message info = execute(infoMessage);
                    this.deleteMessage(info, update, 7000);

                    Message quizMessage = update.getCallbackQuery().getMessage();
                    deleteMessage(quizMessage, update, 7000);

                    deleteMessage(
                            execute(
                                    SendMessage.builder()
                                            .text("Еще?")
                                            .chatId(update.getCallbackQuery().getMessage().getChatId())
                                            .replyMarkup(InlineKeyboardMarkup.builder()
                                                    .keyboardRow(List.of(InlineKeyboardButton.builder()
                                                            .text("Да-а-а")
                                                            .callbackData("yestomorequiz")
                                                            .build()
                                                    )).build()
                                            )
                                            .build()
                            ),
                            update, 7000);

                } catch (TelegramApiException | NumberFormatException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (update.getCallbackQuery().getData().startsWith("practice-")) {
                log.info("Запущена практика номер: {}", update.getCallbackQuery().getData());
                int practiceId = Integer.parseInt(update.getCallbackQuery().getData().substring(9));
                Menu menu = new Menu();
                try {
                    List<Asana> practiceList = dataController.getPractice(menu.getPracticeById(practiceId).asanas);
                    startPractice(practiceList, update.getCallbackQuery().getMessage().getChatId(), 3000);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }


    }

    private void startPractice(List<Asana> asanaList,Long chatId, int time) {
        Thread run = new Thread(() -> {
            try {
                log.trace("Программа {} ", asanaList.stream().map(e -> e.id).collect(Collectors.toList()).toString());
                SendMessage practiceMessage = new SendMessage();
                SendPhoto practicePhoto = new SendPhoto();


                practiceMessage.setChatId(chatId);
                practicePhoto.setChatId(chatId);

                for (Asana asana: asanaList) {
                    practiceMessage.setText(asana.sanskrit);
                    practicePhoto.setPhoto(new InputFile(asana.img));
                    execute(practicePhoto);
                    execute(practiceMessage);
                    Thread.sleep(time);
                }
            } catch (InterruptedException | TelegramApiException e) {
                throw new RuntimeException(e);
            }

        });
        run.start();
    }

    private void deleteMessage(Message response, Update update, int time) {
        Thread run = new Thread(() -> {
            try {
                Thread.sleep(time);
                DeleteMessage del = new DeleteMessage();
                del.setChatId(update.getCallbackQuery().getMessage().getChatId());
                del.setMessageId(response.getMessageId());
                execute(del);
            } catch (InterruptedException | TelegramApiException e) {
                throw new RuntimeException(e);
            }

        });
        run.start();
    }
}
