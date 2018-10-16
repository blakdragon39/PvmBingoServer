package models

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class CardItem(var drop: Drop?,
               val dropId: Int,
               val username: String,
               val proof: String?)

class CardItemMapper : RowMapper<CardItem> {

    override fun map(rs: ResultSet, ctx: StatementContext): CardItem {
        return CardItem(
                null,
                rs.getInt("drop_id"),
                rs.getString("user_name"),
                rs.getString("proof"))
    }
}