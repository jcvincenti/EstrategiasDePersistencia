package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.dto.VectorFrontendDTO
import org.junit.Assert
import org.junit.jupiter.api.Test

class VectorFrontendDTOTest {

    @Test
    fun aModeloTest(){
        var vectorDTO = VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Persona, "Quilmes")
        var vector = vectorDTO.aModelo()

        Assert.assertEquals(vectorDTO.nombreDeUbicacionPresente, vector.nombreDeLocacionActual)
        Assert.assertEquals(vectorDTO.tipoDeVector.name, vector.tipo)
    }
}