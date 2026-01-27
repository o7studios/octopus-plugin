<img width="1024" height="220" alt="image" src="https://github.com/user-attachments/assets/00c872f3-836f-4403-81d1-c750c96bd2b2" />

# Octopus Paper plugin

**Paper plugin with API for wrapper around Octopus SDK for o7studios product Octopus**

- Runs on [Paper](https://papermc.io/software/paper)

## Usage

Add dependency to plugin:

```kotlin
dependencies {
    compileOnly("studio.o7:octopus-plugin-api:X.Y.Z")
}
```

Add _depend on_ inside `plugin.yml`:

```yaml
depend:
  - Octopus
```

### Config

Make sure this is inside of the `/plugins/octopus/config.yml`

```yml
# Configuration of Octopus-Service
octopus:
  # Host of Octopus-gRPC Server
  host: "127.0.0.1"
  # Port of Octopus-gRPC Server
  port: 50051
  # Replace to Octopus-API token
  token: "development"
```