package eventticketsystem.event.dto.request;

import eventticketsystem.event.entity.EventEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record EventFilterRequest(
        String category,
        String location,
        LocalDate dateFrom,
        LocalDate dateTo,
        Long priceFrom,
        Long priceTo,
        String sortBy,
        String sortDir,
        Integer page,
        Integer size
) {

    public Specification<EventEntity> toSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (location != null) {
                predicates.add(cb.like(root.get("location"), "%" + location + "%"));
            }
            if (dateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), dateFrom));
            }
            if (dateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), dateTo));
            }
            if (priceFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), priceFrom));
            }
            if (priceTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), priceTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
