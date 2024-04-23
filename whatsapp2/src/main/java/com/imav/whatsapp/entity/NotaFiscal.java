package com.imav.whatsapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "notafiscal")
public class NotaFiscal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private Long timestampCreated;
	private Long timestampNf;
	private String prontuario;
	private boolean consulta;
	private boolean exame;
	private String nome;
	private String cpf;
	private String email;
	private Long telefone;
	private String cep;
	private String rua;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private int valor;
	private String formaPagamento;
	private String medico;
	private String nfNumero;
	private boolean nfDone;
	private String pagamento1;
	private String pagamento2;
	private String userName;
	private String nfDoneBy;
	private String nomeConvenio;
	private String nomeRede;

	@OneToMany(mappedBy = "notaFiscal")
	List<Exames> exames;

	public NotaFiscal() {

	}

	public NotaFiscal(Long timestampCreated, Long timestampNf, String prontuario, boolean consulta, boolean exame,
			String nome, String cpf, String email, Long telefone, String cep, String rua, String numero,
			String complemento, String bairro, String cidade, String estado, int valor, String formaPagamento,
			String medico, String nfNumero, boolean nfDone, String pagamento1, String pagamento2, String userName,
			String nfDoneBy, String nomeConvenio, String nomeRede, List<Exames> exames) {
		this.timestampCreated = timestampCreated;
		this.timestampNf = timestampNf;
		this.prontuario = prontuario;
		this.consulta = consulta;
		this.exame = exame;
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.telefone = telefone;
		this.cep = cep;
		this.rua = rua;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.valor = valor;
		this.formaPagamento = formaPagamento;
		this.medico = medico;
		this.nfNumero = nfNumero;
		this.nfDone = nfDone;
		this.pagamento1 = pagamento1;
		this.pagamento2 = pagamento2;
		this.userName = userName;
		this.exames = exames;
		this.setNomeConvenio(nomeConvenio);
		this.setNomeRede(nomeRede);
		this.setNfDoneBy(nfDoneBy);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProntuario() {
		return prontuario;
	}

	public void setProntuario(String prontuario) {
		this.prontuario = prontuario;
	}

	public boolean isConsulta() {
		return consulta;
	}

	public void setConsulta(boolean consulta) {
		this.consulta = consulta;
	}

	public boolean isExame() {
		return exame;
	}

	public void setExame(boolean exame) {
		this.exame = exame;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getTelefone() {
		return telefone;
	}

	public void setTelefone(Long telefone) {
		this.telefone = telefone;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getMedico() {
		return medico;
	}

	public void setMedico(String medico) {
		this.medico = medico;
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

	public void setnfDone(boolean nfDone) {
		this.nfDone = nfDone;
	}

	public String getPagamento1() {
		return pagamento1;
	}

	public void setPagamento1(String pagamento1) {
		this.pagamento1 = pagamento1;
	}

	public String getPagamento2() {
		return pagamento2;
	}

	public void setPagamento2(String pagamento2) {
		this.pagamento2 = pagamento2;
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

	public List<Exames> getExames() {
		return exames;
	}

	public void setExames(ArrayList<Exames> exames) {
		this.exames = exames;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String user) {
		this.userName = user;
	}

	public String getNfDoneBy() {
		return nfDoneBy;
	}

	public void setNfDoneBy(String nfDoneBy) {
		this.nfDoneBy = nfDoneBy;
	}

	public String getNomeConvenio() {
		return nomeConvenio;
	}

	public void setNomeConvenio(String nomeConvenio) {
		this.nomeConvenio = nomeConvenio;
	}

	public String getNomeRede() {
		return nomeRede;
	}

	public void setNomeRede(String nomeRede) {
		this.nomeRede = nomeRede;
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
		NotaFiscal other = (NotaFiscal) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "NotaFiscal [id=" + id + ", timestampCreated=" + timestampCreated + ", timestampNf=" + timestampNf
				+ ", prontuario=" + prontuario + ", consulta=" + consulta + ", exame=" + exame + ", nome=" + nome
				+ ", cpf=" + cpf + ", email=" + email + ", telefone=" + telefone + ", cep=" + cep + ", rua=" + rua
				+ ", numero=" + numero + ", complemento=" + complemento + ", bairro=" + bairro + ", cidade=" + cidade
				+ ", estado=" + estado + ", valor=" + valor + ", formaPagamento=" + formaPagamento + ", medico="
				+ medico + ", nfNumero=" + nfNumero + ", nfDone=" + nfDone + ", pagamento1=" + pagamento1
				+ ", pagamento2=" + pagamento2 + ", userName=" + userName + ", nfDoneBy=" + nfDoneBy + ", nomeConvenio="
				+ nomeConvenio + ", nomeRede=" + nomeRede + ", exames=" + exames + "]";
	}

}
