package resources

import models.*
import org.hibernate.validator.constraints.NotEmpty
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/new-card")
@Produces(MediaType.APPLICATION_JSON)
class NewCardResource {

    @GET
    fun newCard(@QueryParam("username") @NotEmpty username: String?): Card {
        //todo username nullable and validate
        //todo make sure this user doesn't already have a card
        if (username == null) {
            throw WebApplicationException("username must not be null", Response.Status.BAD_REQUEST)
        }

        val existingCardItems = HelloWorldApplication.jdbi.withHandle<List<CardItem>, Exception> { handle ->
            handle.createQuery("SELECT * FROM card_items")
                    .map(CardItemMapper())
                    .toList()
        }

        if (existingCardItems.isNotEmpty()) {
            throw WebApplicationException("a card already exists for this username", Response.Status.BAD_REQUEST);
        }

        val drops = HelloWorldApplication.jdbi.withHandle<MutableList<Drop>, Exception> { handle ->
            handle.createQuery("select * from drops")
                    .map(DropMapper())
                    .toMutableList()
        }

        val cardItems = mutableListOf<CardItem>()

        for (i in 0 until 25) {
            val drop = drops.removeAt(HelloWorldApplication.random.nextInt(drops.size - 1))
            cardItems.add(CardItem(drop, drop.id, username, null))
        }

        val card = Card(username, cardItems)
        saveCard(card)

        return card
    }

    private fun saveCard(card: Card) {
        HelloWorldApplication.jdbi.useHandle<Nothing> { handle ->
            card.cardItems.forEach { item ->
                handle.execute("INSERT INTO card_items(user_name, drop_id) VALUES(?, ?)", card.username, item.drop?.id)
            }
        }
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