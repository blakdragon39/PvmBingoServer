package resources

import models.Drop
import models.DropMapper
import org.jdbi.v3.core.Jdbi
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/get-drops")
@Produces(MediaType.APPLICATION_JSON)
class DropsResource {

    @GET
    fun getAllDrops(): List<Drop> {
        val jdbi = Jdbi.create("jdbc:mysql://localhost:3306/pvmbingo", "root", "password")
        return jdbi.withHandle<List<Drop>, Exception> { handle ->
            handle.createQuery("select * from drops")
                    .map(DropMapper())
                    .list()
        }
    }
}