package core;

import java.util.List;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;

public class Matchers {
    public static void checkLists(List actual, List expected) {
        assertThat(actual, everyItem(is(in(expected))));
        assertThat(expected, everyItem(is(in(actual))));
    }
}
