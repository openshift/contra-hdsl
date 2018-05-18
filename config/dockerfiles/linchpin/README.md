# Usage of linchpin container


## Build image
From within the linchpin directory:

```bash
docker build --file Dockerfile.linchpin --tag ci-spawner .
```

## AWS credentials

AWS credentials should be passed as environment variables when the container is run, 
named `AWS_SECRET_ACCESS_KEY` and `AWS_ACCESS_KEY_ID`.

## Beaker credentials

`kinit` is installed in the container but must be run within the container after it starts if Beaker is
intended to be used.

## A note about krb5.conf
The default configuration file for krb5 requires privilege escalation. This Dockerfile replaces it with a version that
is modified in two ways:
- `default_ccache_name` is commented out; this eliminates the need for privilege escalation and simply creates a cache
in /tmp.
- `default_realm` is defined; this is needed for some keytab types.
