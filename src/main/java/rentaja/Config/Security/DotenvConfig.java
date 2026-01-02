package rentaja.Config.Security;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotenvConfig {

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // Agar tidak error jika di prod (karena pakai env asli)
                .load();

        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }
}