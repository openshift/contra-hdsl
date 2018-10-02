def call(Map<String, ?> config = [:]) {
    String reportingType = config.reportingType ?: 'email'

    Map<String, String> configData = readJSON text: env.configJSON

    if (reportingType == 'email'){
        ArrayList recipientEmails = []
        ArrayList ccEmails = []
        ArrayList bccEmails = []

        if ( configData.recipientEmail ) { combineEmailAddresses(recipientEmails, configData.recepientEmail) }
        if ( config.recipientEmail ){ combineEmailAddresses(recipientEmails, config.recipientEmail) }
        if ( configData.ccEmail ) { combineEmailAddresses(ccEmails, configData.ccEmail) }
        if ( config.ccEmail ){ combineEmailAddresses(ccEmails, config.ccEmail) }
        if ( configData.bccEmail ) { combineEmailAddresses(bccEmails, configData.bccEmail) }
        if ( config.bccEmail ){ combineEmailAddresses(bccEmails, config.bccEmail) }

        // subject: JOB NAME - BUILD - RESULT
        String subject = "${env.JOB_NAME} - ${env.BUILD_NUMBER} - ${currentBuild.currentResult}"
        String body = """Hi, here's some info about your job!

Job Name: ${env.JOB_NAME}
Job Build #: ${env.BUILD_NUMBER}
Jobs result: ${currentBuild.currentResult}
Job URL: <a href="${BUILD_URL}">${BUILD_URL}</a>

Thank you,
Contra Productization Automation"""

        HashMap<String, ?> mailValues=[
                to: config.recipientEmail,
                from: config.fromEmail ?: 'ContraProductizationAutomation',
                replyTo: config.replyTo ?: 'noreply@redhat.com',
                subject: subject,
                body: body
        ]

        if (config.ccEmail){ mailValues.ccEmail = config.ccEmail }

        if (config.bccEmail){ mailValues.bccEmail = config.bccEmail }

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