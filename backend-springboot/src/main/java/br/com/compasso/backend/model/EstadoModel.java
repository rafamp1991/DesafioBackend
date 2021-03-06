package br.com.compasso.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Modelo para criação do Estado
 * @author Rafael Martins de Padua
 */
@Entity
@Table(name = "estados")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EstadoModel {
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estado")
	private Long estadoId;
	
	/**
	 * Nome
	 */
	@Column(name = "nome")
	private String nome;
	
	/**
	 * UF
	 */
	@Column(name = "uf")
	private String uf;

	public Long getEstadoId() {
		return estadoId;
	}

	public void setEstadoId(Long estadoId) {
		this.estadoId = estadoId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}
}
