FROM contrainfra/linchpin-executor:v1.0.0

LABEL maintainer="rnester@redhat.com, cmays@redhat.com, ari@redhat.com"
LABEL version="0.0.2"
LABEL description="A container to set up infrastructure instances"


ENV APP_ROOT=/linchpin/
ENV PATH=${APP_ROOT}/bin:${PATH} HOME=${APP_ROOT}

COPY ansible.cfg /etc/ansible.cfg
COPY krb5.conf /etc/krb5.conf
COPY beaker.conf /etc/beaker/client.conf
COPY bin/ ${APP_ROOT}/bin/

WORKDIR /etc/yum.repos.d/

RUN curl -L -O http://download.devel.redhat.com/rel-eng/RCMTOOLS/rcm-tools-fedora.repo && \
    dnf install -y krb5-workstation \
        python3-requests-kerberos \
        rhpkg \
        python3-bugzilla \
        brewkoji \
        brewkoji-stage \
        redhat-rpm-config \
        python-krbV && \
    dnf clean all && \
#    useradd -u 1001 -U linchpin && \
    wget https://password.corp.redhat.com/legacy.crt --no-check-certificate --directory-prefix=/etc/pki/ca-trust/source/anchors/ && \
    wget https://password.corp.redhat.com/RH-IT-Root-CA.crt --no-check-certificate --directory-prefix=/etc/pki/ca-trust/source/anchors/ && \
    wget https://engineering.redhat.com/Eng-CA.crt --no-check-certificate --directory-prefix=/etc/pki/ca-trust/source/anchors/ && \
    update-ca-trust extract && \
    pip install git+git://github.com/CentOS-PaaS-SIG/linchpin.git#egg=linchpin[beaker] && \
    mkdir -p ${APP_ROOT}
    chmod -R u+x ${APP_ROOT}/bin && \
    chgrp -R 0 ${APP_ROOT} && \
    chmod -R g=u ${APP_ROOT} /etc/passwd

#WORKDIR /etc/pki/ca-trust/source/anchors/

### Containers should NOT run as root as a good practice
USER 10001
WORKDIR ${APP_ROOT}

### user name recognition at runtime w/ an arbitrary uid - for OpenShift deployments
ENTRYPOINT [ "uid_entrypoint" ]

CMD ["run"]