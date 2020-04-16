package ar.edu.unq.eperdemic.spring.configuration

import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Bean
    fun patogenoDAO(): PatogenoDAO {
        return JDBCPatogenoDAO()
    }

    @Bean
    fun patogenoService(patogenoDAO: PatogenoDAO): PatogenoService {
        return PatogenoServiceImpl(patogenoDAO)
    }

}
