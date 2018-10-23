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
    fun newCard(@QueryParam("username") @NotEmpty username: String): Card {
        val existingCardItems = BingoApplication.jdbi.withHandle<List<CardItem>, Exception> { handle ->
            handle.select("SELECT * FROM card_items WHERE user_name = ?", username)
                    .map(CardItemMapper())
                    .toList()
        }

        if (existingCardItems.isNotEmpty()) {
            throw WebApplicationException("a card already exists for this username", Response.Status.BAD_REQUEST);
        }

        val cardItems = generateRandomCard(username)
        val card = Card(username, cardItems)
        saveCard(card)

        return card
    }

    private fun generateRandomCard(username: String): List<CardItem> {
        val cardItems = mutableListOf<CardItem>()

        val drops = BingoApplication.jdbi.withHandle<MutableList<Drop>, Exception> { handle ->
            handle.select("select * from drops")
                    .concurrentUpdatable()
                    .map(DropMapper())
                    .toMutableList()
        }

        for (i in 0 until 25) {
            val drop = drops.removeAt(BingoApplication.random.nextInt(drops.size - 1))
            cardItems.add(CardItem(drop, drop.id, username, null))
        }

        return cardItems
    }

    private fun saveCard(card: Card) {
        BingoApplication.jdbi.useHandle<Nothing> { handle ->
            card.cardItems.forEach { item ->
                handle.execute("INSERT INTO card_items(user_name, drop_id) VALUES(?, ?)", card.username, item.drop?.id)
            }
        }
    }
}

@Path("/get-card")
@Produces(MediaType.APPLICATION_JSON)
class GetCardResource {

    @GET
    fun getCard(@QueryParam("username") @NotEmpty username: String): Card {
        val existingCardItems = BingoApplication.jdbi.withHandle<List<CardItem>, Exception> { handle ->
            handle.select("SELECT * FROM card_items WHERE user_name = ?", username)
                    .map(CardItemMapper())
                    .toList()
        }

        val dropIds = existingCardItems.map { item -> item.dropId }

        val drops = BingoApplication.jdbi.withHandle<List<Drop>, Exception> { handle ->
            handle.select("SELECT * FROM drops WHERE id IN (<dropIds>)")
                    .bindList("dropIds", dropIds)
                    .map(DropMapper())
                    .toList()
        }

        existingCardItems.forEach { cardItem ->
            cardItem.drop = drops.first { drop -> cardItem.dropId == drop.id }
        }

        return Card(username, existingCardItems)
    }
}

@Path("/get-all-cards")
@Produces(MediaType.APPLICATION_JSON)
class GetAllCardsResource {

    @GET
    fun getAllCards() : List<String> {
        val cardItems = BingoApplication.jdbi.withHandle<List<CardItem>, Exception> { handle ->
            handle.select("SELECT * FROM card_items")
                    .map(CardItemMapper())
                    .list()
        }

        return cardItems.map { item -> item.username }.distinct()
    }
}

@Path("/update-card")
@Produces(MediaType.APPLICATION_JSON)
class UpdateCardResource {

    @GET
    fun updateCard(@QueryParam("username") @NotEmpty username: String?,
                   @QueryParam("proof") @NotEmpty proof: String?,
                   @QueryParam("dropId") dropId: Int?) {
        BingoApplication.jdbi.useHandle<Nothing> { handle ->
            handle.createUpdate("UPDATE card_items SET proof = :proof WHERE user_name = :username AND drop_id = :dropId")
                    .bind("proof", proof)
                    .bind("username", username)
                    .bind("dropId", dropId)
                    .execute()
        }
    }
}

@Path("/delete-card")
@Produces(MediaType.APPLICATION_JSON)
class DeleteCardResource {

    @GET
    fun deleteCard(@QueryParam("username") @NotEmpty username: String?) {
        BingoApplication.jdbi.useHandle<Nothing> { handle ->
            handle.createUpdate("DELETE FROM card_items WHERE USER_NAME = :username")
                    .bind("username", username)
                    .execute()
        }
    }
}

@Path("/get-drops")
@Produces(MediaType.APPLICATION_JSON)
class DropsResource {

    @GET
    fun getAllDrops(): List<Drop> {
        return BingoApplication.jdbi.withHandle<List<Drop>, Exception> { handle ->
            handle.createQuery("SELECT * FROM drops")
                    .map(DropMapper())
                    .list()
        }
    }
}