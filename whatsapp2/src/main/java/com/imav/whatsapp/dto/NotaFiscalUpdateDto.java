package com.imav.whatsapp.dto;

public class NotaFiscalUpdateDto {

	private Long id;
	private String nfNumero;
	private boolean nfDone;
	private String nfDoneBy;

	public NotaFiscalUpdateDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNfNumero() {
		return nfNumero;
	}

	public void setNfNumero(String nfNumero) {
		this.nfNumero = nfNumero;
	}

	public boolean isNfDone() {
		return nfDone;
	}

	public void setNfDone(boolean nfDone) {
		this.nfDone = nfDone;
	}

	public String getNfDoneBy() {
		return nfDoneBy;
	}

	public void setNfDoneBy(String nfDoneBy) {
		this.nfDoneBy = nfDoneBy;
	}

	@Override
	public String toString() {
		return "NotaFiscalUpdateDto [id=" + id + ", nfNumero=" + nfNumero + ", nfDone=" + nfDone + ", nfDoneBy="
				+ nfDoneBy + "]";
	}

}
