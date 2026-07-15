# Agent Instructions

Always do the following:

1. Read docs/PROGRESS.md.
2. Read docs/architecture/ARCHITECTURE_MEMO.md.
3. Read graphify-out/GRAPH_REPORT.md.
   - If more structural information is required, inspect graphify-out/graph.json.
4. Compare the current Git commit with the Graph Report commit.
5. If the graph is outdated, recommend running `graphify update .`.
6. Identify the specification related to the requested task.
7. If multiple specifications match, ask which one should be used.
8. If no specification is provided, use the Active Module defined in docs/PROGRESS.md.
9. Follow the selected Specification.
10. Update docs/PROGRESS.md after completing the task.
11. Record architectural decisions in docs/architecture/DECISIONS.md.
12. Never finish a task without updating documentation.
13. Prefer Specification-Driven Development.
14. Prefer minimal changes to broad refactors.
15. Preserve existing coding conventions.
16. If the implementation changes architecture, explain why before modifying it.
17. Use GRAPH_REPORT.md to understand:
    - Project modules.
    - Existing architectural boundaries.
    - Community hubs.
    - Existing services/controllers/entities.
    - Possible knowledge gaps.
18. Before implementing new code:
    - Verify whether similar functionality already exists.
    - Prefer extending existing services over creating duplicates.
    - Verify that the selected Specification matches the current architecture and Graph Report.
      - If the Specification conflicts with the current implementation, stop and ask whether the Specification should be updated before continuing.
19. Do not introduce new dependencies unless explicitly requested.