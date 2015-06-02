package webdriver.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import sut.factoryComponents.Page;

/*
 * Created by demidovskiy-r on 02.06.2015.
 */
public class Edit extends Control {

    public Edit(Page parent, WebElement webElement) {
        super(parent, webElement);
    }

    public Edit(Page parent, By locator) {
        super(parent, locator);
    }

    public void type(String value) {
        init().clear();
        init().sendKeys(value);
    }
}