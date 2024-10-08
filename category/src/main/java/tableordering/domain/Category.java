package tableordering.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import tableordering.CategoryApplication;
import tableordering.domain.CategoryCreatedEvent;
import tableordering.domain.CategoryDeletedEvent;

@Entity
@Table(name = "Category_table")
@Data
//<<< DDD / Aggregate Root
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String categoryName;

    private Long storeId;

    @PostPersist
    public void onPostPersist() {
        CategoryCreatedEvent categoryCreatedEvent = new CategoryCreatedEvent(
            this
        );
        categoryCreatedEvent.publishAfterCommit();

        CategoryDeletedEvent categoryDeletedEvent = new CategoryDeletedEvent(
            this
        );
        categoryDeletedEvent.publishAfterCommit();
    }

    public static CategoryRepository repository() {
        CategoryRepository categoryRepository = CategoryApplication.applicationContext.getBean(
            CategoryRepository.class
        );
        return categoryRepository;
    }
}
//>>> DDD / Aggregate Root
