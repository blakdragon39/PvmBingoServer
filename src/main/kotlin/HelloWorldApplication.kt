import health.TemplateHealthCheck
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.jdbi.v3.core.Jdbi
import resources.DropsResource
import resources.NewCardResource
import java.util.*


fun main(args: Array<String>) {
    HelloWorldApplication().run(*args)
}

class HelloWorldApplication : Application<HelloWorldConfiguration>() {

    companion object {
        lateinit var jdbi: Jdbi
        lateinit var random: Random
    }

    override fun getName(): String {
        return "hello-world"
    }

    override fun initialize(bootstrap: Bootstrap<HelloWorldConfiguration>) {
        jdbi = Jdbi.create("jdbc:mysql://localhost:3306/pvmbingo", DB_USERNAME, DB_PASSWORD)
        random = Random()
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
        environment.jersey().register(DropsResource())
        environment.jersey().register(NewCardResource())
    }
}