package webdriver.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import sut.factoryComponents.Page;

/*
 * Created by demidovskiy-r on 02.06.2015.
 */
public class Dropdown extends Control {

    public Dropdown(Page parent, WebElement webElement) {
        super(parent, webElement);
    }

    public Dropdown(Page parent, By locator) {
        super(parent, locator);
    }

    public void selectValue(String value) {
        new Select(init()).selectByVisibleText(value);
    }
}