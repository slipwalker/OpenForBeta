package sut.factoryComponents;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public interface LoadableComponent<T> {
    public void load();
    public void unload();
    public void isLoaded() throws java.lang.Error;
    public T get();
}