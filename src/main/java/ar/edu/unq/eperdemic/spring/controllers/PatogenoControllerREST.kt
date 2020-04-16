package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ServiceREST
@RequestMapping("/patogeno")
class PatogenoControllerREST(private val patogenoService: PatogenoService) {

  @PostMapping
  fun create(@RequestBody patogeno: Patogeno): ResponseEntity<Int> {
    val patogenoId = patogenoService.crearPatogeno(patogeno)
    return ResponseEntity(patogenoId, HttpStatus.CREATED)
  }

  @PostMapping("/{id}")
  fun agregarEspecie(@PathVariable id: Int, @RequestBody especieDTO: EspecieDTO): ResponseEntity<EspecieDTO> {
    val especie = patogenoService.agregarEspecie(id, especieDTO.nombre, especieDTO.paisDeOrigen)
    val dto = EspecieDTO.from(especie)
    return ResponseEntity(dto, HttpStatus.OK)
  }

  @GetMapping("/{id}")
  fun findById(@PathVariable id: Int) = patogenoService.recuperarPatogeno(id)

  @GetMapping
  fun getAll() = patogenoService.recuperarATodosLosPatogenos()


}