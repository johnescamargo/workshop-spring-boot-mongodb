package com.imav.whatsapp.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Convenio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String name;
	private String redePlano;

	@OneToMany(mappedBy = "convenio", orphanRemoval = true)
	private List<Info> infos;

	@OneToMany(mappedBy = "convenio", orphanRemoval = true)
	private List<Plano> planos;

	public Convenio() {

	}

	public Convenio(String name, String redePlano, List<Info> infos, List<Plano> planos) {
		this.name = name;
		this.redePlano = redePlano;
		this.infos = infos;
		this.planos = planos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRedePlano() {
		return redePlano;
	}

	public void setRedePlano(String redePlano) {
		this.redePlano = redePlano;
	}

	public List<Info> getInfos() {
		return infos;
	}

	public void setInfos(List<Info> infos) {
		this.infos = infos;
	}

	public List<Plano> getPlanos() {
		return planos;
	}

	public void setPlanos(List<Plano> planos) {
		this.planos = planos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Convenio other = (Convenio) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Convenio [id=" + id + ", name=" + name + ", redePlano=" + redePlano + ", infos=" + infos + ", planos="
				+ planos + "]";
	}

}
