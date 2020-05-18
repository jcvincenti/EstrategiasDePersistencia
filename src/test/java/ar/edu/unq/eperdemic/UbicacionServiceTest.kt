package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.TipoDeVectorEnum
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException
import ar.edu.unq.eperdemic.services.exceptions.EmptyPropertyException
import ar.edu.unq.eperdemic.services.exceptions.EntityAlreadyExistsException
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class UbicacionServiceTest {

    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())
    @Spy
    val vectorService = VectorServiceImpl(HibernateVectorDAO())
    val dataService = DataServiceHibernate()
    lateinit var portenho: Vector
    lateinit var cordobes: Vector
    lateinit var insecto: Vector

    @BeforeEach
    fun crearSetDeDatosIniciales() {
        MockitoAnnotations.initMocks(this);
        dataService.crearSetDeDatosIniciales()
        portenho = vectorService.recuperarVector(1)
        insecto = vectorService.recuperarVector(3)
        cordobes = vectorService.recuperarVector(4)
    }

    @AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
    }

    @Test
    fun crearUbicacionTest(){
        val ubicacion = ubicacionService.crearUbicacion("Adrogue")
        Assert.assertEquals("Adrogue", ubicacion.nombreUbicacion)
    }

    @Test
    fun crearUbicacionExistenteTest(){
        val exception = assertThrows<EntityAlreadyExistsException>{ubicacionService.crearUbicacion("Bariloche")}
        Assert.assertEquals("La entidad Ubicacion con id Bariloche ya existe", exception.message)
    }

    @Test
    fun crearUbicacionSinNombreTest() {
        val exception = assertThrows<EmptyPropertyException> {ubicacionService.crearUbicacion("")}
        Assert.assertEquals("La propiedad nombreUbicacion esta vacia", exception.message)

    }

    @Test
    fun recuperarUbicacionTest() {
        val ubicacion = ubicacionService.recuperarUbicacion("Entre Rios")
        Assert.assertEquals(ubicacion!!.nombreUbicacion, "Entre Rios")
    }

    @Test
    fun recuperarUbicacionInexistenteTest() {
        val exception = assertThrows<EntityNotFoundException> { ubicacionService.recuperarUbicacion("Tucuman") }
        assertEquals("La entidad Ubicacion con id Tucuman no existe", exception.message)
    }

    @Test
    fun moverConVectorInfectadoTest(){
        Assert.assertEquals("Buenos Aires", portenho.nombreDeLocacionActual)
        Assert.assertTrue(portenho.estaInfectado())
        Assert.assertFalse(cordobes.estaInfectado())

        ubicacionService.mover(1, "Cordoba")
        portenho = vectorService.recuperarVector(1)
        cordobes = vectorService.recuperarVector(4)

        //El portenho cambio su ubicacion a "Cordoba" y el Cordobes ahora esta infectado
        Assert.assertEquals("Cordoba", portenho.nombreDeLocacionActual)
        Assert.assertTrue(cordobes.estaInfectado())
    }

    @Test
    fun moverConVectorNoInfectadoTest(){
        Assert.assertEquals("Bariloche", insecto.nombreDeLocacionActual)
        Assert.assertFalse(insecto.estaInfectado())
        Assert.assertFalse(cordobes.estaInfectado())

        ubicacionService.mover(3, "Cordoba")
        insecto = vectorService.recuperarVector(3)
        cordobes = vectorService.recuperarVector(4)

        //El Insecto cambio su ubicacion a "Cordoba" pero el cordobes no se infecto
        Assert.assertEquals("Cordoba", insecto.nombreDeLocacionActual)
        Assert.assertFalse(cordobes.estaInfectado())
    }

    @Test
    fun moverAUbicacionInexistenteTest() {
        val exception = assertThrows<EntityNotFoundException> {ubicacionService.mover(1, "Chaco")}
        Assert.assertEquals("La entidad Ubicacion con id Chaco no existe", exception.message )
    }

    @ExperimentalStdlibApi
    @Test
    fun expandirVectorInfectadoTest(){
        val buenosAires = ubicacionService.recuperarUbicacion("Buenos Aires")
        val carlos = Vector(buenosAires!!.nombreUbicacion)
        carlos.tipo = TipoDeVectorEnum.Persona
        val carlosId = vectorService.crearVector(carlos).id

        Assert.assertFalse(carlos.estaInfectado())

        ubicacionService.expandir("Buenos Aires")

        Assert.assertTrue(vectorService.recuperarVector(carlosId!!).estaInfectado())
    }

    @ExperimentalStdlibApi
    @Test
    fun expandirVectorNoInfectadoTest(){
        val cordoba = ubicacionService.recuperarUbicacion("Cordoba")
        val jorge = Vector(cordoba!!.nombreUbicacion)
        jorge.tipo = TipoDeVectorEnum.Persona
        val jorgeId = vectorService.crearVector(jorge).id

        Assert.assertFalse(cordobes.estaInfectado())
        Assert.assertFalse(jorge.estaInfectado())

        ubicacionService.expandir("Cordoba")

        Assert.assertFalse(vectorService.recuperarVector(jorgeId!!).estaInfectado())
    }

    @ExperimentalStdlibApi
    @Test
    fun expandirUbicacionInexistenteTest(){
        val exception = assertThrows<EntityNotFoundException> { ubicacionService.expandir("Bernal") }
        assertEquals("La entidad Ubicacion con id Bernal no existe", exception.message)
    }
}
