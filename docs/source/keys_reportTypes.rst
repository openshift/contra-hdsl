reportTypes
===========
The ``reportTypes`` key contains subkeys which pertain to the reporting of job results.

email
-----
The email key contains three values, ``recipient_email``, ``cc_email``, and ``bcc_email``.
These are used to define where email will be sent when calling the :doc:`report` step.
The email addresses defined in ``recipient_email``, ``cc_email``, and `bcc_email`` can be appended to as seen
:ref:`here <passing-arbitrary-email-addresses>`

recipient_email
~~~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ========= ======== =======
   Type      Required Purpose
   ========= ======== =======
   List      True     Contains a list of email addresses to send results to
   ========= ======== =======

cc_email
~~~~~~~~
.. table::
   :widths: 10,15,75

   ========= ======== =======
   Type      Required Purpose
   ========= ======== =======
   List      True     Contains a list of email addresses to cc when sending results
   ========= ======== =======

bcc_email
~~~~~~~~~
.. table::
   :widths: 10,15,75

   ========= ======== =======
   Type      Required Purpose
   ========= ======== =======
   List      True     Contains a list of email addresses to bcc when sending results
   ========= ======== =======
