package models

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class Drop(val itemName: String,
           val itemFile: String,
           val bossName: String,
           val bossFile: String,
           val dropRate: Int)

class DropMapper : RowMapper<Drop> {
    override fun map(rs: ResultSet, ctx: StatementContext): Drop {
        return Drop(
                rs.getString("item_name"),
                rs.getString("item_file"),
                rs.getString("boss_name"),
                rs.getString("boss_file"),
                rs.getInt("drop_rate"))
    }
}