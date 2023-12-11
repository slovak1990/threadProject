package threadQa.myappmanager;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.files.FileFilters;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static java.util.stream.Collectors.toList;

public class BaseObject {
    public MyAppManager app;

    public BaseObject(MyAppManager app) {
        this.app = app;
    }

    //*
    //* Получение и работа с селенид элементами и коллекциями
    //*
    @SafeVarargs
    @Step("Получить селенид элемент")
    public final SelenideElement getSelenideElement(String locator, Object... value) {
        return $(byXpath(new Formatter().format(locator, value).toString()));
    }

    @SafeVarargs
    @Step("Получить коллекцию селенид элементов")
    public final ElementsCollection getSelenideCollection(String locator, Object... value) {
        return $$(byXpath(new Formatter().format(locator, value).toString()));
    }

    @Step("Получить количество элементов в коллекции с ожидаемым размером")
    public int getCountOfCollection(ElementsCollection collection, int expectSize) {
        sleep(2000);
        return collection.shouldHave(CollectionCondition.size(expectSize)).size();
    }

    @Step("Получить количество элементов в коллекции без ожидаемого размера")
    public int getCountOfCollection(ElementsCollection collection) {
        return collection.size();
    }

    @Step("Получить индекс элемента коллекции по значению")
    public int getIndexOfElementInCollection(ElementsCollection locator, String value) {
        return locator.texts().indexOf(value);
    }

    @Step("Получить селенид элемент из коллекции по индексу {index}")
    public SelenideElement getSelenideElementFromCollection(ElementsCollection locator, int index) {
        return locator.get(index).shouldBe(visible);
    }

    @SafeVarargs
    @Step("Получить селенид элемент из коллекции по индексу {index}")
    public final SelenideElement getSelenideElementFromCollection(String locator, int index, Object... value) {
        return getSelenideCollection(locator, value).get(index).shouldBe(visible);
    }

    //*
    //* Проверки на доступность и недоступность
    //*
    @Step("Проверка элемента на доступность")
    public Boolean isEnableElement(SelenideElement locator) {
        return locator.shouldBe(visible).is(enabled);
    }

    @Step("Проверка элемента на доступность")
    public Boolean isEnableElement(String locator, String value) {
        return getSelenideElement(locator, value).shouldBe(visible).is(enabled);
    }

    @Step("Проверка элемента на редактируемость")
    public Boolean isShouldEditableElement(SelenideElement locator) {
        return locator.isEnabled();
    }

    @Step("Проверка элемента на недоступность")
    public Boolean isDisabledElement(SelenideElement locator) {
        return locator.shouldBe(visible).is(disabled);
    }

    //*
    //* Проверки на наличие и отсутствие
    //*
    @Step("Проверка элемента на наличие")
    public Boolean isExistElement(SelenideElement locator) {
        return locator.exists();
    }

    @Step("Проверка элемента на наличие")
    public Boolean isExistElement(String locator, String value) {
        return getSelenideElement(locator, value).exists();
    }

    @Step("Проверка элемента на наличие в коллекции")
    public boolean isElementExistInCollection(ElementsCollection locator, String value) {
        return locator.texts().contains(value);
    }

    @Step("Проверка элемента на наличие с заданным ожиданием в {wait} сек")
    public Boolean isExistElementWithWait(SelenideElement locator, int wait) {
        return locator.should(exist, Duration.ofSeconds(wait)).exists();
    }

    @Step("Проверка элемента на отсутствие с заданным ожиданием в {wait} сек")
    public Boolean isNotExistElementWithWait(SelenideElement locator, int wait) {
        return locator.shouldNotBe(visible, Duration.ofSeconds(wait)).is(not(visible));
    }

    //*
    //* Проверки на содержание атрибута
    //*
    @Step("Проверить, что элемент коллекции содержит атрибут {atr}")
    public boolean isElementContainsAttribute(ElementsCollection locator, String atr, int index) {
        return locator.get(index).has(Condition.attribute(atr));
    }

    @Step("Проверить, что элемент коллекции содержит атрибут {atr}")
    public boolean isElementContainsAttribute(SelenideElement locator, String atr) {
        return locator.has(Condition.attribute(atr));
    }

    @Step("Проверить, что элемент коллекции содержит атрибут {atr}")
    public boolean isElementContainsAttribute(String locator, String atr) {
        return getSelenideElement(locator).has(Condition.attribute(atr));
    }

    @Step("Получить значение атрибута {atr} элемента коллекции")
    public String getElementValueOfAttribute(ElementsCollection locator, String atr, int index) {
        return locator.get(index).getAttribute(atr);
    }

    @Step("Получить значение атрибута {atr} элемента")
    public String getElementValueOfAttribute(SelenideElement locator, String atr) {
        return locator.getAttribute(atr);
    }

    @Step("Проверить, что элемент с именем содержит атрибут с ожидаемым значением")
    public Boolean isElementHasExpectedAttributeValue(SelenideElement locator, String attrName, String value) {
        return locator.shouldHave(Condition.attribute(attrName, value)).isDisplayed();
    }

    @Step("Проверить, что элемент с именем {name} содержит атрибут {atr}")
    public Boolean isElementContainsAttributeByName(String locator, String name, String atr) {
        return getSelenideElement(locator, name).has(Condition.attribute(atr));
    }

    @Step("Проверить, что элемент с именем {name} содержит атрибут {atr} со значением {value}")
    public Boolean isElementContainsAttributeByNameAndValue(String locator, String name, String icon, String atr, String value) {
        return Objects.requireNonNull(getSelenideElement(locator, name, icon).getAttribute(atr)).contains(value);
    }

    @Step("Проверить, что элемент с именем {name} содержит атрибут {atr} со значением {value}")
    public Boolean isElementContainsAttributeByNameAndValue(String locator, String name, String atr, String value) {
        return Objects.requireNonNull(getSelenideElement(locator, name).getAttribute(atr)).contains(value);
    }

    //*
    //* Проверки на содержание текста элементом.
    //* Получение текста элемента.
    //*
    @Step("Проверить, что элемент содержит текст {str}")
    public Boolean elementShouldHaveText(SelenideElement locator, String str) {
        return locator.is(text(str));
    }

    @Step("Проверить, что элемент содержит текст {str} как условие")
    public void elementShouldHaveTextAsCondition(SelenideElement locator, String str) {
        locator.shouldHave(text(str));
    }


    @Step("Получение текстовое значение элемента")
    public String getTextValueOfElement(SelenideElement locator) {
        locator.scrollTo();
        return locator.shouldBe(visible).getText().trim();
    }

    @Step("Проверка наличия текста в классе элемента")
    public Boolean isElementClassNameHaveText(SelenideElement locator, String containsText) {
        return Arrays.asList(locator.getAttribute("class").split(" ")).contains(containsText);
    }

    @Step("Получить текстовое значение элемента из коллекции по индексу {index}")
    public String getTextValueElementFromCollection(ElementsCollection locator, int index) {
        return locator.get(index).shouldBe(visible).getText();
    }

    //*
    //* Клики и фокус на разные виды элементов
    //*
    @Step("Нажать на элемент")
    public void clickOnElement(SelenideElement locator) {
        locator.scrollTo();
        locator.shouldBe(enabled).click();
    }

    @Step("Нажать на элемент")
    public void clickOnElement(String locator, Object... value) {
        getSelenideElement(locator, value).scrollTo();
        getSelenideElement(locator, value).click();
    }

    @Step("Нажать на элемент с JS")
    public void clickOnElementWithJS(SelenideElement locator) {
        locator.scrollTo();
        locator.shouldBe(enabled).click(usingJavaScript());
    }

    @Step("Нажать на элемент с фокусом")
    public void clickOnElementWithHover(SelenideElement locator) {
        locator.scrollTo().hover();
        locator.shouldBe(enabled).click();
    }


    @Step("Сделать фокус и нажать на элемент")
    public void hoverAndClickOnElement(SelenideElement locator) {
        locator.hover().click();
    }

    @Step("Сделать фокус на элемент")
    public void hoverOnElement(SelenideElement locator) {
        locator.hover();
    }

    @Step("Скролл к элементу с помощью JS")
    public void scrollToElementWithJS(SelenideElement locator) {
        executeJavaScript("arguments[0].scrollLeft = arguments[0].scrollWidth", locator);
    }


    @Step("Двойной клик на элемент")
    public void doubleClickOnElement(SelenideElement locator) {
        locator.shouldBe(enabled).doubleClick();
    }

    @Step("Клик правой кнопки мышки на элемент")
    public void clickRightButtonMouse(SelenideElement locator) {
        locator.shouldBe(enabled).contextClick();
    }

    @Step("Нажать на элемент по индексу")
    public void clickOnElementByIndex(ElementsCollection locator, int index) {
        locator.get(index).shouldBe(enabled, visible).click();
    }

    @Step("Нажать на элемент по индексу")
    public void clickOnElementByIndex(String locator, int index) {
        getSelenideCollection(locator).get(index).shouldBe(enabled, visible).click();
    }

    @Step("Нажать на элемент по индексу без видимости элемента")
    public void clickOnElementByIndexWithoutVisible(ElementsCollection locator, int index) {
        locator.get(index).shouldBe(enabled).click();
    }


    @Step("Скролл к элементу")
    public void scrollIntoViewElement(SelenideElement locator) {
        locator.scrollIntoView("{block: \"end\", inline: \"end\"}");
    }


    //*
    //* Работа с полем ввода
    //*
    @Step("Ввод текстового поля")
    public void inputField(SelenideElement locator, String str) {
        locator.hover().shouldBe(enabled).setValue(str);
    }

    @Step("Заполнить поле ввода")
    public void setValueInput(SelenideElement locator, String value) {
        locator.shouldBe(enabled).setValue(value);
    }

    @Step("Очистить и заполнить поле ввода")
    public void cleanAndSetValueInput(SelenideElement locator, String value) {
        locator.sendKeys(Keys.CONTROL + "a");
        locator.sendKeys(Keys.BACK_SPACE);
        locator.setValue(value);
    }

    @Step("Получение значения поля")
    public String getFieldValue(SelenideElement locator) {
        return locator.getValue();
    }

    @Step("Ввод текстового поля значением и нажатие Enter")
    public void inputFieldAndEnter(SelenideElement locator, String str) {
        locator.hover().shouldBe(enabled).setValue(str).pressEnter();
    }

    @Step("Ввод значения в текстовое поле с помощью Actions")
    public void setValueInputWithActions(SelenideElement locator, String value) {
        actions()
                .doubleClick(locator)
                .click(locator)
                .sendKeys(Keys.BACK_SPACE)
                .sendKeys(value)
                .sendKeys(Keys.ENTER)
                .build()
                .perform();
    }

    @Step("Проверить сортировку списка с значениями")
    public boolean isListSorted(List<String> list, boolean sortedByDESC) {
        if (!sortedByDESC) {
            list.sort(null);
            return list.equals(list.stream().sorted().collect(toList()));
        } else {
            list.sort(Collections.reverseOrder());
            return list.equals(list.stream().sorted(Comparator.reverseOrder()).collect(toList()));
        }
    }


    @Step("Получить коллекцию селенид элементов столбца по индексу столбца {indexCol}")
    public ElementsCollection getSelenideCollection(String locator, int indexCol) {
        return $$(byXpath(String.format(locator, indexCol)));
    }


    //*
    //* Работа с файлами
    //*
    @Step("Скачать файл с расширением {extension}")
    public File downloadFile(SelenideElement locator, String extension) throws FileNotFoundException {
        return locator.download(FileFilters.withExtension(extension));
    }

    @Step("Получение текстовое значение элемента")
    public String getTextValueOfElementByCondition(SelenideElement locator, String condition) {
        locator.scrollTo();
        Condition condType = exist;
        switch (condition) {
            case "enabled":
                condType = enabled;
                break;
            case "visible":
                condType = visible;
                break;
        }
        return locator.shouldBe(condType).getText().trim();
    }
}