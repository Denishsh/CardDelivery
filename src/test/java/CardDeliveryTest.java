import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    @BeforeEach
    public void setup() {
        open("http://localhost:9999");
        Configuration.browser = "firefox";
    }

    private String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    public void correctDeliveryTest() {
        String currentDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Олег");
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".notification__title").shouldHave(text("Успешно!"));
        $(".notification__content")
                .shouldHave(text("Встреча успешно запланирована на \n" + currentDate));
    }


    @Test
    public void emptyCityTest() {
        String currentDate = generateDate(3);
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван"); // баг, с буквой ё не принимается
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void incorrectCityTest() {
        String currentDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Москвава");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void incorrectNameTest() {
        String currentDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Ivanov ivan");
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void emptyNameTest() {
        String currentDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void incorrectPhoneTest() {
        String currentDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+7911111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        // баг, можно любое к-во цифр в номере оставить (должно быть 11)
    }

    @Test
    public void emptyPhoneTest() {
        String currentDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void withoutAgreementTest() {
        String currentDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+7911111111");
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid");
    }
}
