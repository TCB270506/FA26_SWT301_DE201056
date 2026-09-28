package lab2.accountTest;

import lab2.accountTest.AccountValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountValidatorTest {

    @ParameterizedTest(name = "[{index}] độ dài {0} -> {1}")
    @MethodSource("usernameLengths")
    void isValidUsername_BoundaryLength(int length, boolean expected) {
        assertEquals(expected, AccountValidator.isValidUsername("a".repeat(length)));
    }

    static Stream<Arguments> usernameLengths() {          // PHẢI là static
        return Stream.of(
                Arguments.of(4, false),
                Arguments.of(5, true),
                Arguments.of(20, true),
                Arguments.of(21, false) /* thêm 6, 19 */
        );
    }

    @ParameterizedTest(name = "[{index}] {3}")
    @CsvSource(delimiter = '|', value = {
            "Secret@123    | alice_01 | true  | hợp lệ",
            "secret@123    | alice_01 | false | thiếu chữ hoa",
            "'Secret @123' | alice_01 | false | chứa khoảng trắng",   // giữ khoảng trắng bằng '...'
            "Xalice_01@1   | alice_01 | false | chứa username",
            "Xalice_01@1   |          | true  | username null -> bỏ qua"  // ô trống = null
    })
    void isValidPassword_Partitions(String pw, String user, boolean expected, String desc) {
        assertEquals(expected, AccountValidator.isValidPassword(pw, user));
    }

    @ParameterizedTest(name = "[{index}] sinh {0}, hôm nay {1} -> {2} tuổi")
    @CsvSource({
            "2008-09-28, 2026-09-28, 18",   // đúng sinh nhật 18
            "2008-09-29, 2026-09-28, 17",   // 18 tuổi trừ 1 ngày
            "2008-02-29, 2026-02-28, 17",   // năm nhuận
            "2008-02-29, 2026-03-01, 18"
    })
    void calculateAge_Boundaries(LocalDate dob, LocalDate today, int expected) {  // JUnit tự đổi String -> LocalDate
        assertEquals(expected, AccountValidator.calculateAge(dob, today));
    }
}