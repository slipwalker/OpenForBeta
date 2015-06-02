package webdriver.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import sut.factoryComponents.Page;

/*
 * Created by demidovskiy-r on 02.06.2015.
 */
public class Button extends Control {

    public Button(Page parent, WebElement webElement) {
        super(parent, webElement);
    }

    public Button(Page parent, By locator) {
        super(parent, locator);
    }
}