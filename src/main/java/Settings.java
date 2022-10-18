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
    // ./bashrc
    // export BOT_TOKEN={YOUR_BOT_TOKEN}
    //
    public static String token = System.getenv("BOT_TOKEN").substring(10);
    public static List<String> rightAnswer = Arrays.asList("Круто!", "Молодец", "Умница");
    public static List<String> wrongMessage = Arrays.asList("Не-а", "Подумай еще", "Не верно", "Ну нет");
}
