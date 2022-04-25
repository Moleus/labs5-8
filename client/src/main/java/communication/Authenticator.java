package communication;

import exceptions.InvalidCredentialsException;
import exceptions.UserAlreadyExistsException;
import user.User;

import java.io.IOException;

/**
 * Работа аутентификации:
 * 1. При входе в приложение у него открывается консоль/окно.
 * Поле User user = null;
 * 2. С сервера подгружается коллекция, без запроса на авторизацию
 * Не проверяется поле user в Request.
 * 2. Доступны все локальные команды для просмотра коллекции, фильтрации.
 * 3. После отправки команды на изменение коллекции на сервере проверяется наличие в БД пользователя
 * из значения user (Request).
 * 1) Если user = null или пользователя нет в БД - возвращается ответ
 * "Пользователь не зарегистрирован и не может изменять коллекцию".
 * <p>
 * 4. Команды login и register:
 * execute запускается локально.
 * Создаётся объект User из введённых логина и пароля.
 * Отправляется Request без payload, c значением User и Purpose.LOGIN/REGISTER.
 * Ждём ответ в виде сообщения -> login: "нет пользователя"/"вы вошли", register: "с таким логином уже существует"/"зарегистрирован"
 * <p>
 * 5. Обработка на стороне сервера:
 * Разделение запросов по типам.
 * CHANGE_COLLECTION -> проверка на наличие пользователя в базе, выполнение команды, возврат execResult
 * LOGIN -> проверка на наличие пользователя в базе, возврат execResult с сообщением
 * REGISTER -> проверка на отсутствие пользователя в базе, возврат execResult с сообщением
 */
public interface Authenticator {
  void register(User user) throws UserAlreadyExistsException, InvalidCredentialsException, IOException;

  void login(User user) throws InvalidCredentialsException, IOException;
}
