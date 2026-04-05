package eventticketsystem.event.dto.response;

import java.io.Serializable;
import java.util.List;

public record PageDto<T>(List<T> content, int page, int size, long totalElements) implements Serializable {}