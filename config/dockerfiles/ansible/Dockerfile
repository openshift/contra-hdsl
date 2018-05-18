FROM fedora:27

LABEL maintainer="rnester@redhat.com, cmays@redhat.com"
LABEL version="0.0.1"
LABEL description="A container to run test scripts"

RUN dnf install -y ansible && \
    dnf install -y fedpkg libselinux-python standard-test-roles

VOLUME /ansible

COPY ansible.cfg /root/.ansible.cfg

WORKDIR /ansible

CMD ["tail", "-f", "/dev/null"]
