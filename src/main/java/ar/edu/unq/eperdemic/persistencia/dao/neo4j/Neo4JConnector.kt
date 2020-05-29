package ar.edu.unq.eperdemic.persistencia.dao.neo4j
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector
import org.neo4j.driver.v1.*
import java.sql.Connection

object Neo4JConnector {
    val driver: Driver

    init {
        val env = System.getenv()
        val url = env.getOrDefault("URL", "bolt://localhost:7687")
        val username = env.getOrDefault("USER", "neo4j")
        val password = env.getOrDefault("PASSWORD", "root")

        driver = GraphDatabase.driver(url, AuthTokens.basic(username, password),
                Config.build().withLogging(Logging.slf4j()).toConfig()
        )
    }

    fun <T> execute(bloque: (Session) -> T) : T {
        return driver.session().use(bloque)
    }
}