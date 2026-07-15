# Architecture Decisions

## ADR-001

Decision:

Use DTOs instead of exposing Entities.

Reason:

Avoid coupling persistence with API.

---

## ADR-002

Decision:

Move filtering logic into SQL Server.

Reason:

Reduce network traffic and improve response times.

---

## ADR-003

Decision:

Generate Graphify context before writing Specs.

Reason:

LLM receives complete architectural context.