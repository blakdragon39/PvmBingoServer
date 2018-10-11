
import javax.ws.rs.QueryParam
import com.codahale.metrics.annotation.Timed
import models.AddResult
import models.Saying
import javax.ws.rs.GET
import java.util.concurrent.atomic.AtomicLong
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
class HelloWorldResource(private val template: String,
                         private val defaultName: String) {

    private val counter: AtomicLong = AtomicLong()

    @GET
    @Timed
    fun sayHello(@QueryParam("name") name: String?): Saying {
        val value = String.format(template, name ?: defaultName)
        return Saying(counter.incrementAndGet(), value)
    }
}

@Path("/add")
@Produces(MediaType.APPLICATION_JSON)
class AddResource(private val amountToAdd: Int) {

    @GET
    fun add(@QueryParam("number") number: Int): AddResult {
        return AddResult(amountToAdd, number, number + amountToAdd)
    }
}
