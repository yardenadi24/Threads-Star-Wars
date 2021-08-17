import bgu.spl.mics.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class FutureTest {
    private Future<String> future;


    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    //--test also get and isDone--//.
    @Test
    public void testResolve(){
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get()));
    }

    @Test
    public void testGet() {
        String str = "someResult";
        future.resolve(str);
        assertTrue(str.equals(future.get(1000,TimeUnit.MILLISECONDS)));
    }

}