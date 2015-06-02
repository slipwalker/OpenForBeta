package sut.factoryComponents;

import webdriver.ExtendedWebDriver;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public abstract class Page<T> implements LoadableComponent<T> {
    protected ExtendedWebDriver web;

    public Page(ExtendedWebDriver web) {
        this.web = web;
        get();
        init();
    }

    public abstract void init();

    public T get() {
        try {
            isLoaded();
            return (T) this;
        } catch (Throwable e) {
            load();
        }
        isLoaded();
        return (T) this;
    }

    public ExtendedWebDriver getDriver() {
        return web;
    }
}