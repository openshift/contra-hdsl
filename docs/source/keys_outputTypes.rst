outputTypes
===========
The ``outputTypes`` key contains subkeys which pertain to the handling of job artifacts.

artifacts
---------
The artifacts key contains two values, ``filesToSave`` and ``filesToExclude``. These are used in combination to create a
set of files which can be stored by using the :doc:`saveArtifacts` step. The files defined in ``filesToSave`` and
``filesToExclude`` can be overridden as seen :ref:`here <passing-arbitrary-configuration-parameters>`

filesToSave
~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ========= ======== =======
   Type      Required Purpose
   ========= ======== =======
   List      True     Contains a list of files to save.
   ========= ======== =======

filesToExclude
~~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ========= ======== =======
   Type      Required Purpose
   ========= ======== =======
   List      True     Contains a list of files to exclude.
   ========= ======== =======
