package eventticketsystem.gateway.dto.event;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Object> toParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("location", location);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);
        params.put("priceFrom", priceFrom);
        params.put("priceTo", priceTo);
        params.put("status", status);
        params.put("sortBy", sortBy);
        params.put("sortDir", sortDir);
        params.put("page", page);
        params.put("size", size);
        return params;
    }

}



