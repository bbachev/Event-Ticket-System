# Event Ticket System — Context

## Project
Spring Boot 4.x microservices:
- API Gateway (JWT)
- Notification Service (Kafka + idempotency)
- Booking Service
- Preference Service
- Event Service

## Currently being added
Redis distributed lock for booking:
- Granularity: per seat/ticket-type (not per event)
- Redis down → fail-closed (booking is rejected, never proceeds without a lock)
- Release: pure TTL (auto-expire), no explicit release/fencing token —
  a deliberate simplification, not an oversight
- Learning note in the code at the lock-release logic: TTL-only release
  carries a risk of the lock expiring mid-critical-section on a slow
  operation → a second request acquires the lock → overselling.
  Production fix = fencing token (unique value per acquire, release
  only if it matches).

## Planned next additions
- Elasticsearch — search read-model for the Event Service
- Sync REST/OpenFeign between Booking → Event → Preference (an
  explicit sync example)
- Docker Compose locally → Kubernetes (minikube → EKS) → Terraform for
  AWS infra (VPC, EKS/ECS, RDS, ElastiCache) → deploy

## Context for the whole prep effort
The Nexo offer has already been accepted — this is NOT interview prep,
it's familiarization with technologies from the Nexo stack that have
gaps (Kafka async patterns, Redis, Elasticsearch, Kubernetes, Terraform,
AWS deploy). One month until the start date, available time per day
varies.

Priority: **breadth over depth** — cover more concepts with a
reasonable implementation, not production-grade depth on every single
component. Short learning-note comments in the code wherever a
simpler approach was deliberately chosen over a production-grade
solution, to leave a trail for going deeper later.

## Working style — read before making changes
This is a learning project. I need to understand each piece, not just
get code that compiles.

- Before implementing a new concept (Kafka consumer groups, Redis
  locking, K8s manifests, Terraform modules, etc.): explain the
  approach and trade-offs in 3-5 sentences first, then wait for me
  to confirm before writing code.
- Prefer small diffs over multi-file generation in one shot.
- For genuinely new-to-me tech: show a minimal skeleton, let me
  extend it myself, don't hand me the finished implementation.
- Boilerplate/repetitive code (DTOs, standard CRUD, getters) —
  go ahead and generate directly, no need to slow down there.
- If I ask "how do I do X": answer with guidance or pseudocode
  first. Only write the full solution if I explicitly ask for it.