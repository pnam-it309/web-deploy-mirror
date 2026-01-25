# FPL UDPM Catalog - Python SDK

Python client library for the FPL UDPM Catalog API.

## Installation

```bash
pip install fpl-catalog-sdk
```

## Usage

```python
from fpl_catalog import FPLCatalogClient

client = FPLCatalogClient(
    base_url='https://api.fpl-catalog.com',
    api_key='your-api-key'  # optional
)

# Fetch apps
apps = client.apps.list(domain_id='domain-id', page=1, size=10)

# Get app details
app = client.apps.get('app-id')

# Search apps
results = client.apps.search(
    query='React',
    technologies=['react', 'typescript']
)

# Get domains
domains = client.domains.list()

# Get technologies
technologies = client.technologies.list()
```

## API Reference

### Apps

- `apps.list(**params)` - List all apps with pagination
- `apps.get(id)` - Get app by ID
- `apps.search(**params)` - Search apps
- `apps.related(id)` - Get related apps

### Domains

- `domains.list()` - List all domains
- `domains.get(id)` - Get domain by ID

### Technologies

- `technologies.list()` - List all technologies
- `technologies.get(id)` - Get technology by ID

## Type Hints

This SDK includes full type hints for better IDE support.

## License

MIT

---

**Note:** This is a documentation-only placeholder. For full implementation, create a separate PyPI package with the actual SDK code.

## Implementation Guide

To create the actual SDK:

1. Create new repository: `fpl-catalog-sdk-python`
2. Set up Python project with requests library
3. Define dataclasses/Pydantic models for API responses
4. Implement client class with methods
5. Add tests with pytest
6. Publish to PyPI

Example structure:

```
fpl_catalog/
  __init__.py
  client.py
  resources/
    __init__.py
    apps.py
    domains.py
    technologies.py
  models/
    __init__.py
    app.py
    domain.py
```
