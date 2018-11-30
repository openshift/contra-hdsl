Introduction to the Contra HDSL
===============================
Welcome to the Contra HDSL documentation!

The Contra HDSL is a high level DSL shared library for Jenkins. The library is built on `Groovy
<http://groovy-lang.org/>`_ and the core Jenkins pipeline steps. The Contra HDSL seeks to simplify the creation of
`Jenkins pipeline files <https://jenkins.io/doc/book/pipeline/>`_ by presenting building blocks, or primitives, which
can be called from `scripted <https://jenkins.io/doc/book/pipeline/#scripted-pipeline-fundamentals>`_ or
`declarative <https://jenkins.io/doc/book/pipeline/#declarative-pipeline-fundamentals>`_ pipelines.

Why use the Contra HDSL?
========================
Writing and maintaining non-trivial Jenkinsfiles can be an overwhelming task. The Contra HDSL seeks to provide a set of
primitives which match to frequently used aspects of creating CI/CD Jenkinsfiles. These include:

* Deploying infrastructure
* Configuring infrastructure
* Executing tests
* Reporting results
* Saving artifacts
* Tearing down infrastructure

The Contra HDSL attempts to be a flexible set of primitives that don't preclude using any additional Jenkins plugins,
etc.

.. toctree::
   :maxdepth: 1

   getting_started
   docs
   developer_info
   faqs
   community
   glossary

.. note:: Releases are formatted using `semantic versioning <https://semver.org>`_. If the release shown above is a
          pre-release version, the content listed may not be supported. Use `latest </en/latest>`_ for the most
          up-to-date documentation.

.. |link-pre| raw:: html

    <a href="https://github.com/openshift/contra-hdsl/releases/tag/v

.. |link-post| raw:: html

    ">release notes</a>

.. autosummary::
   :toctree: _autosummary

Indices and tables
==================

* :ref:`genindex`
* :ref:`search`