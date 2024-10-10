package tableordering.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import tableordering.UserApplication;

@Entity
@Table(name = "user", schema = "user")
@Data
// <<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private Date createdAt;

    private Date updatedAt;

    @PostPersist
    public void onPostPersist() {
    }

    public static UserRepository repository() {
        UserRepository userRepository = UserApplication.applicationContext.getBean(
                UserRepository.class);
        return userRepository;
    }
}
// >>> DDD / Aggregate Root
