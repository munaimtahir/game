# MCP Setup Guide

**Date**: May 24, 2026

## Overview
Model Context Protocol (MCP) helps connect an AI agent to the repository securely. This setup provides read-only repository context, Gradle command assistance, and structured planning boundaries.

## Installation
1. Install an MCP-compatible client (e.g., Claude Desktop, Cursor).
2. Configure the client to point to this repository directory.
3. Allow `read` access to the full repository.
4. Allow `write` access ONLY to `docs/` or specific source directories when working on an approved gameplay issue.

## Running in Safe Mode
To inspect without risk of accidental changes:
- Limit the agent tool set to `read_file`, `list_directory`, `glob`, `grep_search`.
- Disable `run_shell_command` except for `./gradlew test` and `./gradlew lint`.

## Agent Instructions
Provide the agent with `MCP_AGENT_WORKFLOW.md` as context before starting.
