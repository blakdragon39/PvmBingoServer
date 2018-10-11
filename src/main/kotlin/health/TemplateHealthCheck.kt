package health

import com.codahale.metrics.health.HealthCheck

class TemplateHealthCheck(private val template: String) : HealthCheck() {

    @Throws(Exception::class)
    override fun check(): HealthCheck.Result {
        val saying = String.format(template, "TEST")
        return if (!saying.contains("TEST")) HealthCheck.Result.unhealthy("template doesn't include a name")
            else HealthCheck.Result.healthy()
    }
}