# tommon-lib
Java monitoring library. Does not monitor anything by itself, it needs to load plugins to monitor something.
## Plugins
Plugins are implemented using custom annotations. Currently supported types:
- JMX
- "Key: value" resource defined by URL (separator of key and value can and even has to be defined)
