package eventticketsystem.booking.dto;

import java.util.UUID;

public record TicketInventory(UUID eventId, Integer totalTickets, Long ticketPrice) {}
