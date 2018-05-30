FROM fedora:27

LABEL maintainer="rnester@redhat.com, cmays@redhat.com"
LABEL version="0.0.1"
LABEL description="A container to set up infrastructure instances"

WORKDIR /etc/yum.repos.d/

RUN curl -L -O http://download.devel.redhat.com/rel-eng/RCMTOOLS/rcm-tools-fedora.repo && \
    dnf install -y git \
        krb5-workstation \
        python3-requests-kerberos \
        rhpkg \
        python3-bugzilla \
        brewkoji \
        brewkoji-stage \
        wget \
        libffi-devel \
        openssl-devel \
        libyaml-devel \
        gmp-devel \
        libselinux-python \
        make \
        gcc \
        redhat-rpm-config \
        libxml2-python \
        libxslt-python \
        python-devel \
        python-krbV \ &&
     dnf clean all

WORKDIR /etc/pki/ca-trust/source/anchors/

RUN wget https://password.corp.redhat.com/legacy.crt --no-check-certificate && \
    wget https://password.corp.redhat.com/RH-IT-Root-CA.crt --no-check-certificate && \
    wget https://engineering.redhat.com/Eng-CA.crt --no-check-certificate && \
    update-ca-trust extract && \
    pip install git+git://github.com/CentOS-PaaS-SIG/linchpin.git#egg=linchpin[beaker]

WORKDIR /root/

COPY ansible.cfg .ansible.cfg

WORKDIR /linchpin/

COPY krb5.conf /etc/krb5.conf
COPY beaker.conf /etc/beaker/client.conf

USER root

CMD ["tail", "-f", "/dev/null"]
