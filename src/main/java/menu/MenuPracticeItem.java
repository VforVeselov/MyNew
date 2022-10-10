package menu;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuPracticeItem {
    public int id;
    public String name;
    public Integer[] asanas;
    public String desc;
    public int asanaDelay;
}
