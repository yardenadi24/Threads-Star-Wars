import bgu.spl.mics.application.passiveObjects.Ewok;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EwokTest {
    Ewok _ewok;
    int serial;

    @BeforeEach
    void setUp() {
        _ewok = new Ewok(serial);
    }

    @Test
    void acquire() {
        boolean ewokState = _ewok.isAvailable();
        //pre: "available" needs to be true
        assertTrue(ewokState);
        _ewok.acquire();
        //post:"available" needs to be false
        assertTrue(!_ewok.isAvailable());
    }

    @Test
    void release() {
        _ewok.acquire();
        boolean ewokState = _ewok.isAvailable();
        //pre: "available" needs to be false
        assertFalse(ewokState);
        _ewok.release();
        //post:"available" needs to be true
        assertTrue(_ewok.isAvailable());
    }
}