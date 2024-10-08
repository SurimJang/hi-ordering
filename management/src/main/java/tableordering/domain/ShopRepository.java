package tableordering.domain;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tableordering.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "shops", path = "shops")
public interface ShopRepository
    extends PagingAndSortingRepository<Shop, Long> {}
