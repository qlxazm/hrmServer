import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestUtil {

    public static void main(String[] args) {
        String password = "123456";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("=====" + passwordEncoder.encode(password));
    }
}
