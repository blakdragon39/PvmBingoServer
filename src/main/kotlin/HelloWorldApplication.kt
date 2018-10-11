import health.TemplateHealthCheck
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class HelloWorldApplication : Application<HelloWorldConfiguration>() {

    override fun getName(): String {
        return "hello-world"
    }

    override fun initialize(bootstrap: Bootstrap<HelloWorldConfiguration>) {
        // nothing to do yet
    }

    override fun run(configuration: HelloWorldConfiguration, environment: Environment) {
        val resource = HelloWorldResource(
                configuration.template,
                configuration.defaultName
        )
        val healthCheck = TemplateHealthCheck(configuration.template)

        environment.healthChecks().register("template", healthCheck)
        environment.jersey().register(resource)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            HelloWorldApplication().run(*args)
        }
    }

}