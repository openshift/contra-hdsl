/**
 * This function attempts to consume an HDSL config and run the test contents in parallel.
 *
 * @param config - A parsed hdsl configuration.
 * @param test   - Closure that is the body of the
 */
def call(Map config, Closure test) {
    // Support single infra use case
    if (config.infra && config.infra instanceof Map) {
        return test(config)
    }

    // Clone each infra into a separate config and run in parallel
    List configs = []
    for (infra in config.infra) {
        Map singleInfraConfig = [:]
        singleInfraConfig << config
        singleInfraConfig.infra = infra
        singleInfraConfig.name = infra.name
        configs.push(singleInfraConfig)
    }

    // Run the new single infra configs in parallel and in separate hdsl pods
    parallelize(configs) {
        singleInfraConfig ->
        node(hdslPodName) {
            test(singleInfraConfig)
        }
    }
}
