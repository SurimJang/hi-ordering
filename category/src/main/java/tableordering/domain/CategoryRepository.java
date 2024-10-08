package tableordering.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tableordering.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "categories",
    path = "categories"
)
public interface CategoryRepository
    extends PagingAndSortingRepository<Category, Long> {}
