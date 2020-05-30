package ar.edu.unq.eperdemic.persistencia.dao.neo4j

import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session


object Neo4JConnector {
    val driver: Driver

    init {
        val env = System.getenv()
        val url = env.getOrDefault("URL", "bolt://localhost:7687")
        val username = env.getOrDefault("USER", "neo4j")
        val password = env.getOrDefault("PASSWORD", "root")

        driver = GraphDatabase.driver(url, AuthTokens.basic(username, password))
    }

    fun <T> execute(bloque: (Session) -> T) : T {
        return driver.session().use(bloque)
    }
}