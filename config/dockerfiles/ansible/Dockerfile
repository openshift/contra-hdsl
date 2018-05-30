FROM fedora:27

LABEL maintainer="rnester@redhat.com, cmays@redhat.com, ari@redhat.com"
LABEL version="0.0.3"
LABEL description="A container to run test scripts"

RUN dnf install -y ansible \
        fedpkg \
        libselinux-python \
        standard-test-roles \
        rsync \ &&
     dnf clean all

VOLUME /ansible

COPY ansible.cfg /root/.ansible.cfg

WORKDIR /ansible

CMD ["tail", "-f", "/dev/null"]