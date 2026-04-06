package eventticketsystem.booking.mapper;

import eventticketsystem.booking.dto.Booking;
import eventticketsystem.booking.entity.BookingEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper
public interface BookingMapper {
    Booking toModel(BookingEntity bookingEntity);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<Booking> mapBookings(List<BookingEntity> bookingEntities);
}
