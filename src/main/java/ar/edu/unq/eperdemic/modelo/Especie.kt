package ar.edu.unq.eperdemic.modelo

import javax.management.AttributeNotFoundException
import javax.persistence.*
import kotlin.random.Random

@Entity
class Especie() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var nombre: String? = null
    @Column(name = "pais_de_origen")
    var paisDeOrigen: String? = null
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var patogeno: Patogeno? = null
    @ManyToMany(mappedBy = "especies")
    var vectores: MutableSet<Vector> = HashSet()

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "especie_mutacion",
            joinColumns = [JoinColumn(name = "especie_id")],
            inverseJoinColumns = [JoinColumn(name = "mutacion_id")])
    var mutaciones: MutableSet<Mutacion> = HashSet();
    var adn = 0;

    constructor(patogeno: Patogeno, nombreEspecie: String, paisDeOrigen: String) : this() {
        this.patogeno = patogeno
        this.nombre = nombreEspecie
        this.paisDeOrigen = paisDeOrigen
    }

    fun getCapacidadDeContagio(tipoVector: TipoDeVectorEnum) : Int? {
        return patogeno!!.getCapacidadDeContagio(tipoVector)
    }

    fun mutar(mutacion: Mutacion) {
        if (mutaciones.containsAll(mutacion.mutacionesRequeridas) && mutacion.adnRequerido <= adn) {
            mutaciones.add(mutacion)
            adn -= mutacion.adnRequerido
            when(mutacion.atributoAIncrementar) {
                "capacidadDeContagio" -> {
                    patogeno!!.setCapacidadDeContagio(TipoDeVectorEnum.Persona, mutacion.valorAIncrementar)
                    patogeno!!.setCapacidadDeContagio(TipoDeVectorEnum.Animal, mutacion.valorAIncrementar)
                    patogeno!!.setCapacidadDeContagio(TipoDeVectorEnum.Insecto, mutacion.valorAIncrementar)
                }
                "letalidad" -> patogeno!!.letalidad = patogeno!!.letalidad?.plus(mutacion.valorAIncrementar)
                "defensa" -> patogeno!!.defensa = patogeno!!.defensa?.plus(mutacion.valorAIncrementar)
                else -> throw AttributeNotFoundException("El atributo ${mutacion.atributoAIncrementar} no existe")
            }

        }
    }
    fun esContagioExitoso(tipoVector: TipoDeVectorEnum) : Boolean {
        val factorDeContagioExitoso = getCapacidadDeContagio(tipoVector)!!.plus(Random.nextInt(1, 10))
        return if (factorDeContagioExitoso > 50) {
            Random.nextInt(factorDeContagioExitoso-50, 100) < factorDeContagioExitoso
        } else {
            Random.nextInt(1, 100) < factorDeContagioExitoso
        }
    }
}