package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Mutacion() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var atributoAIncrementar: String = ""
    var adnRequerido: Int = 0
    var valorAIncrementar: Int = 0

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "mutacion_mutaciones_requeridas",
            joinColumns = [JoinColumn(name = "mutacion_id")],
            inverseJoinColumns = [JoinColumn(name = "mutacion_requerida_id")])
    var mutacionesRequeridas: MutableSet<Mutacion> = HashSet()

    constructor(atributo: String, adn: Int, valor: Int) : this() {
        this.atributoAIncrementar = atributo
        this.adnRequerido = adn
        this.valorAIncrementar = valor
    }

}