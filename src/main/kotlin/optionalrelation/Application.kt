package optionalrelation

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.Join
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.repeatable.JoinSpecifications
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.micronaut.runtime.Micronaut
import java.util.Optional

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .packages("optionalrelation")
            .mainClass(Application.javaClass)
            .start()
    }
}

@MappedEntity("session_a")
class SessionA(
    @field:Id
    @MappedProperty("id")
    val id: Int = 0,

    @MappedProperty("fk_account_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    val account: Account,

    @MappedProperty("fk_maybe_account")
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    val maybeAnotherAccount: Account?
)

@MappedEntity("session_b")
class SessionB(
    @field:Id
    @MappedProperty("id")
    val id: Int = 0,

    @MappedProperty("account_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    val account: Account,

    @MappedProperty("maybe_account_id")
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    val maybeAnotherAccount: Account?
)

@MappedEntity("account")
class Account(
    @field:Id
    @MappedProperty("id")
    val id: Int = 0,
    @MappedProperty("name")
    val name: String
)

@JdbcRepository(dialect = Dialect.MYSQL)
abstract class SessionARepository : CrudRepository<SessionA, Int> {
    @JoinSpecifications(
        Join(value = "account", type = Join.Type.LEFT_FETCH),
        Join(value = "maybeAnotherAccount", type = Join.Type.LEFT_FETCH)
    )
    abstract override fun findById(id: Int): Optional<SessionA>
}

@JdbcRepository(dialect = Dialect.MYSQL)
abstract class SessionBRepository : CrudRepository<SessionB, Int> {
    @JoinSpecifications(
        Join(value = "account", type = Join.Type.LEFT_FETCH),
        Join(value = "maybeAnotherAccount", type = Join.Type.LEFT_FETCH)
    )
    abstract override fun findById(id: Int): Optional<SessionB>
}
