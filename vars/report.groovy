def call(Map<String, ?> config = [:]) {
    String reportingType = config.reportingType ?: 'email'

    config = readJSON(text: env.configJSON) << config

    if (reportingType == 'email'){
        ArrayList recipientEmails = []
        ArrayList ccEmails = []
        ArrayList bccEmails = []

        if ( config.recipientEmail ) { combineEmailAddresses(recipientEmails, config.recepientEmail) }
        if ( config.recipientEmail ){ combineEmailAddresses(recipientEmails, config.recipientEmail) }
        if ( config.ccEmail ) { combineEmailAddresses(ccEmails, config.ccEmail) }
        if ( config.ccEmail ){ combineEmailAddresses(ccEmails, config.ccEmail) }
        if ( config.bccEmail ) { combineEmailAddresses(bccEmails, config.bccEmail) }
        if ( config.bccEmail ){ combineEmailAddresses(bccEmails, config.bccEmail) }

        // subject: JOB NAME - BUILD - RESULT
        String subject = "${env.JOB_NAME} - ${env.BUILD_NUMBER} - ${currentBuild.currentResult}"
        String body = """Hi, here's some info about your job!

Job Name: ${env.JOB_NAME}
Job Build #: ${env.BUILD_NUMBER}
Job result: ${currentBuild.currentResult}
Job URL: ${BUILD_URL}

Thank you,
Contra Productization Automation"""

        HashMap<String, ?> mailValues=[
                to: recipientEmails.join(','),
                from: config.fromEmail ?: 'ContraProductizationAutomation',
                replyTo: config.replyTo ?: 'noreply@redhat.com',
                subject: subject,
                body: body
        ]

        if (ccEmails){ mailValues.ccEmail = ccEmails.join(',')}

        if (bccEmails){ mailValues.bccEmail = bccEmails.join(',') }

        mail(mailValues)
    }
}

static combineEmailAddresses(ArrayList addresses, Object entry) {
    if (entry instanceof String) {
        addresses.add(entry)
        return addresses
    }

    if (entry instanceof ArrayList){
        addresses.addAll(entry)
        return addresses
    }
}
