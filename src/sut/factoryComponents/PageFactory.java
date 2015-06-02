package sut.factoryComponents;

import webdriver.ExtendedWebDriver;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class PageFactory {
    protected final ExtendedWebDriver webDriver;

    public PageFactory(ExtendedWebDriver webDriver) {
        this.webDriver = webDriver;
    }

    protected <T> T getPage(Class<T> pageClassToProxy)  {
        try {
            try {
                Constructor<T> constructor = pageClassToProxy.getConstructor(ExtendedWebDriver.class);
                return constructor.newInstance(webDriver);
            } catch (NoSuchMethodException e) {
                return pageClassToProxy.newInstance();
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}