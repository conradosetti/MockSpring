package br.com.valueprojects.mock_spring.model;

import java.util.ArrayList;
import java.util.List;

public class Participante {

	private List<Sms> sms = new ArrayList<Sms>();
	private int id;
	private String nome;
	
	public Participante(String nome) {
		this(0, nome);
	}

	public Participante(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void recebeSms(Sms sms) {
		this.sms.add(sms);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participante other = (Participante) obj;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	
}
