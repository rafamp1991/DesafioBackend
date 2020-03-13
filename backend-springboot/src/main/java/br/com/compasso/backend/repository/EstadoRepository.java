package br.com.compasso.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.compasso.backend.model.EstadoModel;

public interface EstadoRepository extends JpaRepository<EstadoModel, Long> {
	
	EstadoModel findByNome(String nome);
	
	EstadoModel findByUf(String uf);
}
