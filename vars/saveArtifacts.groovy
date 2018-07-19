/**
 * A method to store artifacts within the job on the Master.
 * @param config: An optional map that holds configuration parameters. The following are used keys in config:
 *                  @filesToSave - either a string of comma separated filenames or masks, or a list of strings of
 *                      filenames or masks to save. These are appended to any filenames or masks to save in the
 *                      contra.yml file.
 *                  @filesToExclude - either a string of comma separated filenames or masks, or a list of strings of
 *                      filenames or masks to save. These are appended to any filenames or masks to exclude in the
 *                      contra.yml file.
 *                  @allowEmptyArchive - Boolean indicating whether to allow for an empty archive. Default: true
 *                  @fingerprint - Boolean indicating whether to fingerprint files. Default: false
 *                  @onlyIfSuccessful - Boolean indicating whether archiving should occur only when the job succeeds.
 *                      Default: false
 *                  @defaultExcludes - Boolean indicating whether to exclude the default excludes in Jenkins.
 *                      See http://ant.apache.org/manual/dirtasks.html#defaultexcludes for a list of default excludes.
 * @return
 */
def call(Map <String, ?> config=[:]) {

    def configData = readJSON text: env.configJSON

    ArrayList filesToSave = []
    ArrayList filesToExclude = []

    Boolean allowEmptyArchive = true
    Boolean fingerprint = false
    Boolean onlyIfSuccessful = false
    Boolean defaultExcludes = true

    try {
        // Handle the case of nothing in the config yaml for results->outputTypes->artifacts.
        // The idea here is that we check for configData, then a key of "results", etc.
        // If we reach a NullPointerException, we log it, and move forward, as there may be
        // parameters passed in via the Jenkinsfile call.

        configData
        key="configData"
        configData.get('results')
        key="results"
        configData.results.get('outputTypes')
        key="outputTypes"
        configData.results.outputTypes.get('artifacts')
        key="artifacts"

        Map artifacts = configData.results.outputTypes.artifacts
        filesToSave = artifacts.filesToSave ? formatFileSet(artifacts.filesToSave) : filesToSave
        filesToExclude = artifacts.filesToExclude ? formatFileSet(artifacts.filesToExclude) : filesToExclude

        allowEmptyArchive = artifacts.allowEmptyArchive ?: allowEmptyArchive
        fingerprint = artifacts.fingerprint ?: fingerprint
        onlyIfSuccessful = artifacts.onlyIfSuccessful ?: onlyIfSuccessful
        defaultExcludes = artifacts.defaultExcludes ?: defaultExcludes

    } catch (NullPointerException ignore) {
        println("No data under the ${key} section of the config file")
    }

    if ( config.filesToSave ){
        filesToSave.addAll(formatFileSet(config.filesToSave))
    }

    if ( config.filesToExclude ){
        filesToExclude.addAll(formatFileSet(config.filesToExclude))
    }

    if ( filesToSave ){
        allowEmptyArchive = config.allowEmptyArchive ?: allowEmptyArchive
        fingerprint = config.fingerprint ?: fingerprint
        onlyIfSuccessful = config.onlyIfSuccessful ?: onlyIfSuccessful
        defaultExcludes = config.defaultExcludes ?: defaultExcludes

        Map command = [
                allowEmptyArchive: allowEmptyArchive,
                artifacts: filesToSave.join(','),
                defaultExcludes: defaultExcludes,
                fingerprint: fingerprint,
                onlyIfSuccessful: onlyIfSuccessful,
        ]

        if ( filesToExclude ){
            command['excludes'] = filesToExclude.join(',')
        }

        archiveArtifacts(command)
        
    } else {
        println("No files specified to archive, skipping artifact archiving.")
    }
}


static formatFileSet(fileSet){
    if ( fileSet == null || fileSet.empty ){
        return []
    } else if (fileSet instanceof String){
        return fileSet.tokenize(",").collect {it.trim()}
    } else if (fileSet instanceof List){
        return fileSet.collect { it.trim() }
    }
}
