package eventticketsystem.booking.mapper;

import eventticketsystem.booking.dto.Booking;
import eventticketsystem.booking.dto.BookingCreatedMessage;
import eventticketsystem.booking.entity.BookingEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper
public interface BookingMapper {
    @Mapping(target = "receiver", source = "receiver")
    Booking toModel(BookingEntity bookingEntity, String receiver);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<Booking> mapBookings(List<BookingEntity> bookingEntities);

    @Mapping(target = "messageId", source = "id")
    @Mapping(target = "timestamp", source = "createdAt")
    BookingCreatedMessage toMessage(Booking booking);
}
