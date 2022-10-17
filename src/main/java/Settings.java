import lombok.Value;
import lombok.experimental.UtilityClass;
//
//docker tag bodhi:latest nikiteveselovu/bodhi:latest
// docker push nikiteveselovu/bodhi:latest
// docker run -d --rm -p 80:3000 username/helloworld-with-docker:0.1.0.
import java.util.Arrays;
import java.util.List;
@Value
public class Settings {
    public static String botName = "Bodhi";
    // export botToken = {YOUR_BOT_TOKEN}
    //public static String botToken = System.getenv("BOT_TOKEN");
    public static String token = "1591576699:AAF_E5hPn_BLQJ6K4WC0CnxSC8-3iGnjmSw";
    public static List<String> rightAnswer = Arrays.asList("Круто!", "Молодец", "Умница");
    public static List<String> wrongMessage = Arrays.asList("Не-а", "Подумай еще", "Не верно", "Ну нет");
}
