Event Ticket System with Microservices

Goal

Build a backend system where users can browse events, filter them, book tickets, and receive asynchronous notifications for successful bookings and newly created events matching their preferences.

Functional Requirements

Event Management
•	System can create a new event
•	System can update event details
•	System can delete an event
•	System can return all available events
•	System can return event details by id
•	System can filter events by:
•	category
•	location
•	date range
•	price range

Ticket Booking
•	User can book a ticket for an event
•	System must validate ticket availability before confirming booking
•	System must prevent overselling / double booking
•	System can return all bookings for a given user id
•	System can cancel a booking if business rules allow it

Notifications
•	System sends a notification when a ticket is booked successfully
•	System sends a notification when a new event is created for a user’s preference categories

Non-Functional Requirements
•	Booking flow must be consistent under concurrent traffic
•	Event search should be fast
•	Notifications must be asynchronous
•	Services should be independently deployable
•	System should be scalable horizontally
•	System should expose logs and metrics for monitoring
•	Eventual consistency is acceptable for notifications
•	Strong consistency is required for ticket inventory

Required Technologies
•	Spring Boot for backend microservices
•	PostgreSQL as the primary database
•	ORM required: Spring Data JPA with Hibernate
•	Redis required: for caching filtered event searches and hot event data
•	Kafka required: for asynchronous event-driven communication


Booking Consistency Requirements
•	System must not allow more bookings than available tickets
•	Booking logic must be concurrency-safe
•	Use either:
•	pessimistic locking
•	optimistic locking with retry
•	atomic inventory update pattern

Business Rules
•	A booking succeeds only if ticket stock is available
•	Notification sending must not block booking response
•	New event notifications are sent only to users whose preferences match the event category
•	Search/filter queries should use Redis cache for repeated requests


Event Ticket System with Microservices

Goal

Build a backend system where users can browse events, filter them, book tickets, and receive asynchronous notifications for successful bookings and newly created events matching their preferences.

Functional Requirements

Event Management
•	System can create a new event
•	System can update event details
•	System can delete an event
•	System can return all available events
•	System can return event details by id
•	System can filter events by:
•	category
•	location
•	date range
•	price range

Ticket Booking
•	User can book a ticket for an event
•	System must validate ticket availability before confirming booking
•	System must prevent overselling / double booking
•	System can return all bookings for a given user id
•	System can cancel a booking if business rules allow it

Notifications
•	System sends a notification when a ticket is booked successfully
•	System sends a notification when a new event is created for a user’s preference categories

Non-Functional Requirements
•	Booking flow must be consistent under concurrent traffic
•	Event search should be fast
•	Notifications must be asynchronous
•	Services should be independently deployable
•	System should be scalable horizontally
•	System should expose logs and metrics for monitoring
•	Eventual consistency is acceptable for notifications
•	Strong consistency is required for ticket inventory

Required Technologies
•	Spring Boot for backend microservices
•	PostgreSQL as the primary database
•	ORM required: Spring Data JPA with Hibernate
•	Redis required: for caching filtered event searches and hot event data
•	Kafka required: for asynchronous event-driven communication

Booking Consistency Requirements
•	System must not allow more bookings than available tickets
•	Booking logic must be concurrency-safe
•	Use either:
•	pessimistic locking
•	optimistic locking with retry
•	atomic inventory update pattern

Business Rules
•	A booking succeeds only if ticket stock is available
•	Notification sending must not block booking response
•	New event notifications are sent only to users whose preferences match the event category
•	Search/filter queries should use Redis cache for repeated requests



-----------------------------------------------------------------------------------------------


Core API Requirements

Event Service
•	POST /events
•	PUT /events/{id}
•	DELETE /events/{id}
•	GET /events
•	GET /events/{id}

Booking Service
•	POST /bookings
•	GET /bookings?userId=...
•	DELETE /bookings/{id}

Preference Service
•	GET /preferences/{userId}
•	PUT /preferences/{userId}

Notification Service
•	GET /notifications/{userId}


-----------------------------------------------------------------------------------------------


![Architecture Diagram](./docs/architecture.png)