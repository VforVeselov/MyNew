
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PracticeController {

    public List<Asana> getPractice(Integer[] asanasID) throws IOException {
        DataController d = new DataController();
        List<Asana> asanas = d.getAsanas();
        //охуеть. это было прямо взрыв мозга ) но это не то что нужно) ахахах
        //asanas.stream().filter(e -> Arrays.stream(asanasID).anyMatch(k -> k.equals(e.id))).collect(Collectors.toList()).stream().map(e->e.sanskrit).forEach(System.out::println);
        List<Asana> pracriceList= new ArrayList<Asana>();
        for (Integer aId: asanasID) {
            pracriceList.add(asanas.stream().filter(e -> e.id==aId).findFirst().get());
        }
        //pracriceList.stream().map(e->e.sanskrit).collect(Collectors.toList());
        return pracriceList;
////        Thread run = new Thread(() -> {
////            try {
////                for (Asana asana: pracriceList) {
////                    practiceMessage.setText(asana.sanskrit);
////                    practicePhoto.setPhoto(new InputFile(asana.img));
////                    Thread.sleep(1000);
////                    //System.out.println(asana.sanskrit);
////                }
////
////
////            } catch (InterruptedException e) {
////                throw new RuntimeException(e);
////            }
//        });
//        run.start();
    }
    //TODO сделать меню для выбора практики
    // меню должно содержать много вариантов практик и настраиваться через файл, чтобы каждый раз не править код

    // Практика
    //      Сурья
    //          Длительность
    //      Общая
    //          Длительность 1
    //          Длительность 2
    //      От Маши
    //          На руки
    //
    // получаем три уровня в меню
    public void practiceMenu() {


    }
}
