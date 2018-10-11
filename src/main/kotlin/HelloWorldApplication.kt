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
        val helloResource = HelloWorldResource(
                configuration.template,
                configuration.defaultName
        )
        val addResource = AddResource(configuration.amountToAdd)


        val healthCheck = TemplateHealthCheck(configuration.template)

        environment.healthChecks().register("template", healthCheck)
        environment.jersey().register(helloResource)
        environment.jersey().register(addResource)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            HelloWorldApplication().run(*args)
        }
    }

}