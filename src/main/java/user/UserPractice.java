package user;

import lombok.Data;

/**
 * Класс для записиси практик, которые сделал себе пользователь через сайт
 *
 */
@Data
public class UserPractice {

    public int userId;

    public String name;

    public Integer[] practice;
}
