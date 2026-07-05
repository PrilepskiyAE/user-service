import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Простой тест")
@Feature("Простая проверка")
public class SimpleTest {
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Простая проверка")
    @Description("Простая проверка")
    @Story("Простая проверка")
    @Owner("Prilepskiy AE")
    void test1() {
        int result = 2+3;
        assertEquals(5, result);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Простая проверка номер 2")
    @Description("Простая проверка номер 3")
    @Story("Простая проверка номер 2")
    @Owner("Prilepskiy AE")
    void test2() {
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "A");

        assertThat(map.containsKey("a")).isTrue();
        assertThat(map.containsKey("b")).isFalse();

    }


}
