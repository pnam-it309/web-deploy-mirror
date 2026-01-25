# FPL UDPM Catalog - JavaScript SDK

JavaScript/TypeScript client library for the FPL UDPM Catalog API.

## Installation

```bash
npm install fpl-catalog-sdk
# or
yarn add fpl-catalog-sdk
```

## Usage

```typescript
import { FPLCatalogClient } from "fpl-catalog-sdk";

const client = new FPLCatalogClient({
  baseUrl: "https://api.fpl-catalog.com",
  apiKey: "your-api-key", // optional
});

// Fetch apps
const apps = await client.apps.list({
  domainId: "domain-id",
  page: 1,
  size: 10,
});

// Get app details
const app = await client.apps.get("app-id");

// Search apps
const searchResults = await client.apps.search({
  query: "React",
  technologies: ["react", "typescript"],
});

// Get domains
const domains = await client.domains.list();

// Get technologies
const technologies = await client.technologies.list();
```

## API Reference

### Apps

- `apps.list(params)` - List all apps with pagination
- `apps.get(id)` - Get app by ID
- `apps.search(params)` - Search apps
- `apps.related(id)` - Get related apps

### Domains

- `domains.list()` - List all domains
- `domains.get(id)` - Get domain by ID

### Technologies

- `technologies.list()` - List all technologies
- `technologies.get(id)` - Get technology by ID

## TypeScript Support

This SDK is written in TypeScript and includes full type definitions.

## License

MIT

---

**Note:** This is a documentation-only placeholder. For full implementation, create a separate npm package with the actual SDK code.

## Implementation Guide

To create the actual SDK:

1. Create new repository: `fpl-catalog-sdk-js`
2. Set up TypeScript project with axios/fetch
3. Define types matching API responses
4. Implement client class with methods
5. Add tests
6. Publish to npm

Example structure:

```
src/
  index.ts
  client.ts
  resources/
    apps.ts
    domains.ts
    technologies.ts
  types/
    index.ts
```
