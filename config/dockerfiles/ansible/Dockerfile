FROM alpine:latest

LABEL maintainer="rnester@redhat.com, cmays@redhat.com"
LABEL version="0.0.1"
LABEL description="A container to run test scripts"

RUN apk update && \
    apk add ansible && \
    mkdir /etc/ansible

VOLUME /ansible

COPY ansible.cfg /root/.ansible.cfg

WORKDIR /ansible

CMD ["tail", "-f", "/dev/null"]
