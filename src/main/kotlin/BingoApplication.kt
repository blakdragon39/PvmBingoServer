import health.TemplateHealthCheck
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.jdbi.v3.core.Jdbi
import resources.*
import java.util.*


fun main(args: Array<String>) {
    BingoApplication().run(*args)
}

class BingoApplication : Application<BingoConfiguration>() {

    companion object {
        lateinit var jdbi: Jdbi
        lateinit var random: Random
    }

    override fun getName(): String {
        return "hello-world"
    }

    override fun initialize(bootstrap: Bootstrap<BingoConfiguration>) {
        super.initialize(bootstrap)
        jdbi = Jdbi.create("jdbc:mysql://localhost:3306/pvmbingo", DB_USERNAME, DB_PASSWORD)
        random = Random()
    }

    override fun run(configuration: BingoConfiguration, environment: Environment) {
        environment.healthChecks().register("template", TemplateHealthCheck(configuration.template))
        environment.jersey().register(DropsResource())
        environment.jersey().register(NewCardResource())
        environment.jersey().register(GetCardResource())
        environment.jersey().register(GetAllCardsResource())
        environment.jersey().register(UpdateCardResource())
    }
}