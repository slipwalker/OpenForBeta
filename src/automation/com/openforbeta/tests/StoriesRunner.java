package automation.com.openforbeta.tests;

import automation.com.openforbeta.TestsEmbedder;

/*
 * Created by demidovskiy-r on 31.05.2015.
 */
public class StoriesRunner {
    public static void main(String[] args) throws Throwable {
        new TestsEmbedder(args).run();
    }
}