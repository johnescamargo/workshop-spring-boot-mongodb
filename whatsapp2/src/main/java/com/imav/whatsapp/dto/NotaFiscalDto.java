package com.imav.whatsapp.dto;

public class NotaFiscalDto {

	private Long id;
	private String nome;
	private Long timestampCreated;
	private Long timestampNf;
	private boolean nfDone;

	public NotaFiscalDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getTimestampCreated() {
		return timestampCreated;
	}

	public void setTimestampCreated(Long timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Long getTimestampNf() {
		return timestampNf;
	}

	public void setTimestampNf(Long timestampNf) {
		this.timestampNf = timestampNf;
	}

	public boolean isNfDone() {
		return nfDone;
	}

	public void setNfDone(boolean nfDone) {
		this.nfDone = nfDone;
	}

	@Override
	public String toString() {
		return "NotaFiscalDto [id=" + id + ", nome=" + nome + ", timestampCreated=" + timestampCreated
				+ ", timestampNf=" + timestampNf + ", nfDone=" + nfDone + "]";
	}

}
