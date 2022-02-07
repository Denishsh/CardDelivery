import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    @BeforeEach
    public void setup() {
        open("http://localhost:9999");
        Configuration.browser = "firefox";
    }

    @Test
    public void correctDeliveryTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = LocalDate.now().format(formatter);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(currentDate);
//        $$(".input__control[type=text]").last().setValue("Иванов Андрей");
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".notification__title").shouldHave(text("Успешно!"));
//        $$("button").filter(text("Перепланировать")).first().click();
    }

//    @Test
//    public void incorrectDateTest() {
//        //Заказ на выбранную дату невозможен
//        $("[data-test-id=city] input").setValue("Москва");
//        $("[data-test-id=date] input").setValue("01.06.2000");
//        $("[data-test-id=name] input").setValue("Иванов Иван");
//        $("[data-test-id=phone] input").setValue("+79111111111");
//        $(".checkbox__box").click();
//        $$("button").filter(text("Запланировать")).first().click();
//        $(".input_invalid .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
//    }

    @Test
    public void emptyCityTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = LocalDate.now().format(formatter);
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван"); // баг, с буквой ё не принимается
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void incorrectCityTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = LocalDate.now().format(formatter);
        $("[data-test-id=city] input").setValue("Москвава");
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void incorrectNameTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = LocalDate.now().format(formatter);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Ivanov ivan");
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void emptyNameTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = LocalDate.now().format(formatter);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=phone] input").setValue("+79111111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void incorrectPhoneTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = LocalDate.now().format(formatter);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+7911111111");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        // баг, можно любое к-во цифр в номере оставить (должно быть 11)
    }

    @Test
    public void emptyPhoneTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = LocalDate.now().format(formatter);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void withoutAgreementTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = LocalDate.now().format(formatter);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(currentDate);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+7911111111");
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid");
    }
}
