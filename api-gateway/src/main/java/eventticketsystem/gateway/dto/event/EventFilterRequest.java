package eventticketsystem.gateway.dto.event;

import java.time.LocalDate;

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

}
