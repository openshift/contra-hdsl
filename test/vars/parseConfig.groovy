import spock.lang.Specification
//import vars.parseConfig

class ParseConfigSpec extends Specification {
/*
    def 'parse config' () {
        def p = new parseConfig()
        def config = [filename : 'test/contra-sample.yml']
        when:
            p(config)
        
        then:
            noExceptionThrown()
    }
*/
    def 'simple test' () {
        when:
            def sum = 1+1
        then:
            sum == 2
    }
}

