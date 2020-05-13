package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO

class HibernateEspecieDAO: HibernateDAO<Especie>(Especie::class.java), EspecieDAO {
}