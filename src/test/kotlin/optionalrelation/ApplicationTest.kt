package optionalrelation

import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.ext.ScriptUtils
import org.testcontainers.jdbc.JdbcDatabaseDelegate
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.inject.Inject

@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTest : TestPropertyProvider {
    companion object {
        @Container
        @JvmStatic
        val database = MariaDBContainer<Nothing>()
    }

    @Inject
    lateinit var sessionARepository: SessionARepository;
    @Inject
    lateinit var sessionBRepository: SessionBRepository;

    override fun getProperties(): MutableMap<String, String> {
        database.start()

        return mutableMapOf(
            "application.datasources.default.url" to "jdbc:mariadb://127.0.0.1:${database.firstMappedPort}/${database.databaseName}",
            "application.datasources.default.username" to database.username,
            "application.datasources.default.password" to database.password
        )
    }

    @BeforeEach
    fun resetDatabase() {
        val delegate = JdbcDatabaseDelegate(database, "")
        ScriptUtils.runInitScript(delegate, "schema.sql")
        ScriptUtils.runInitScript(delegate, "test-data.sql")
    }

    @Test
    fun `Session A findById() should fetch a Session with a prefixed relation column`() {
        val session = sessionARepository.findById(3)

        assertThat(session).get()
            .usingRecursiveComparison()
            .isEqualTo(
                SessionA(
                    3,
                    Account(2, "Bar"),
                    null
                )
            )
    }

    @Test
    fun `Session A findById() should fetch a Session with a nullable association`() {
        val session = sessionARepository.findById(4)

        assertThat(session).get()
            .usingRecursiveComparison()
            .isEqualTo(
                SessionA(
                    4,
                    Account(2, "Bar"),
                    Account(3, "Baz")
                )
            )
    }

    @Test
    fun `Session B findById() should fetch a Session with a non-prefixed relation column`() {
        val session = sessionBRepository.findById(3)

        assertThat(session).get()
            .usingRecursiveComparison()
            .isEqualTo(
                SessionB(
                    3,
                    Account(2, "Bar"),
                    null
                )
            )
    }

    @Test
    fun `Session B findById() should fetch a Session with a nullable association`() {
        val session = sessionBRepository.findById(4)

        assertThat(session).get()
            .usingRecursiveComparison()
            .isEqualTo(
                SessionB(
                    4,
                    Account(2, "Bar"),
                    Account(3, "Baz")
                )
            )
    }
}
