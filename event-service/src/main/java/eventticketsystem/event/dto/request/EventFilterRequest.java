package eventticketsystem.event.dto.request;

import eventticketsystem.event.dto.EventStatus;
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
        EventStatus status,
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
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    public String toCacheKey() {
        return "events:" +
                keyPart(category) + ":" +
                keyPart(location) + ":" +
                keyPart(dateFrom) + ":" +
                keyPart(dateTo) + ":" +
                keyPart(priceFrom) + ":" +
                keyPart(priceTo) + ":" +
                keyPart(sortBy) + ":" +
                keyPart(sortDir) + ":" +
                keyPart(status) + ":" +
                keyPart(page) + ":" +
                keyPart(size);
    }

    private String keyPart(Object value) {
        if (value == null) return "";
        String str = value.toString().trim().toLowerCase();
        return str.equals("null") ? "" : str;
    }
}
