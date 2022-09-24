import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class MyAmazingBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "Bodhi";
    }

    @Override
    public String getBotToken() {
        return "1591576699:AAF_E5hPn_BLQJ6K4WC0CnxSC8-3iGnjmSw";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            //System.out.println("User:" + update.getChatMember().toString());
            if (update.getMessage().getText().equals("sss")) {
                System.out.println("start");
                try {
                    DataController dataController = new DataController();
                    //dataController.getAsanas().stream().map(e->e.s).forEach(System.out::println);
                    dataController.getAsanas().stream().map(e->e.s).forEach(message::setText);

                } catch (IOException e) {
                    System.out.println("error");
                    throw new RuntimeException(e);
                }

            }
            //message.setText(update.getMessage().getText());
//
            try {
                DataController dataController = new DataController();
                String[] s = (String[]) dataController.getAsanas().stream().map(e->e.s).toArray();
                for (String string : s) {
                    message.setText(string);
                    System.out.println("s");
                    execute(message);
                }
                //execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
