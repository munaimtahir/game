# MCP Setup

This repository is designed for a local, agent-safe MCP workflow. The goal is to let an assistant inspect the repo, read git history, and run explicit commands without exposing secrets or giving broad remote control.

## Recommended Client Setup
- Point your MCP client at the repository root only.
- Prefer read-only access by default.
- Allow write access only when you are actively working on a scoped branch and only for the files that branch needs.
- Keep secrets outside the MCP surface.

## Safe Local Workflow
1. Open the repo in your MCP-capable client.
2. Load [`MCP_AGENT_WORKFLOW.md`](./MCP_AGENT_WORKFLOW.md) first.
3. Keep the first session read-only.
4. Only enable write mode when the task has a narrow scope and a clear file list.
5. Use GitHub Actions artifacts and local Gradle outputs as evidence sources, not untrusted issue text.

## Suggested Tool Boundaries
- Read-only:
  - file listing
  - file reads
  - git status / diff / log
  - artifact summarization
- Controlled write:
  - docs updates
  - scoped source changes for one game at a time
  - workflow/script updates for CI hardening
- Avoid:
  - unrestricted shell access
  - secret access
  - unreviewed remote commands

## Client Examples
- Codex / Copilot-style local agent:
  - mount the repository
  - expose file reads
  - explicitly gate shell execution
- Claude Desktop / Cursor-style MCP clients:
  - keep the repo root as the only filesystem root
  - disable external command execution unless the task explicitly needs it
- Gemini CLI style workflows:
  - use the repo as the only workspace and keep commands explicit

## Dependencies
- Java 17
- Android SDK with API 35 available
- Gradle wrapper already included in the repo

## What MCP Is For Here
- One-game-at-a-time repair planning
- Git diff review before edits
- Gradle task orchestration when the command is explicit
- Artifact and log summarization after GitHub Actions runs
