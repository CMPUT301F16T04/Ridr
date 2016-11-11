package ca.ualberta.ridr;

import org.junit.Test;

/**
 * Created by mackenzie on 10/11/16.
 */
public class randoTest {

    @Test
    public void testy(){
        System.out.println(new asyncOperations().get("user","id", "769086de-0304-4ee2-be6d-ac788f0ba6cf"));
    }
}
