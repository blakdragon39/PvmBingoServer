package resources

import models.Card
import models.CardItem
import models.Drop
import models.DropMapper
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/new-card")
@Produces(MediaType.APPLICATION_JSON)
class NewCardResource {

    @GET
    fun newCard(@QueryParam("username") username: String): Card {
        val drops = HelloWorldApplication.jdbi.withHandle<MutableList<Drop>, Exception> { handle ->
            handle.createQuery("select * from drops")
                    .map(DropMapper())
                    .toMutableList()
        }

        val cardItems = mutableListOf<CardItem>()

        for (i in 0 until 25) {
            val drop = drops.removeAt(HelloWorldApplication.random.nextInt(drops.size - 1))
            cardItems.add(CardItem(drop, null))
        }

        val card = Card(username, cardItems)
        saveCard(card)

        return card
    }

    private fun saveCard(card: Card) {
        val handler = HelloWorldApplication.jdbi.transactionHandler
        handler
    }
}

@Path("/get-drops")
@Produces(MediaType.APPLICATION_JSON)
class DropsResource {

    @GET
    fun getAllDrops(): List<Drop> {
        return HelloWorldApplication.jdbi.withHandle<List<Drop>, Exception> { handle ->
            handle.createQuery("select * from drops")
                    .map(DropMapper())
                    .list()
        }
    }
}