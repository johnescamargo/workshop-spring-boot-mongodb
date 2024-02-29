package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.dto.ConvenioNameDto;
import com.imav.whatsapp.entity.Content;
import com.imav.whatsapp.entity.Convenio;
import com.imav.whatsapp.entity.Info;
import com.imav.whatsapp.entity.Plano;
import com.imav.whatsapp.repository.ContentRepository;
import com.imav.whatsapp.repository.ConvenioRepository;
import com.imav.whatsapp.repository.InfoRepository;
import com.imav.whatsapp.repository.PlanoRepository;

@Service
public class ConvenioResource {

	private Gson gson = new Gson();

	@Autowired
	private ConvenioRepository convenioRepository;

	@Autowired
	private InfoRepository infoRepository;

	@Autowired
	private PlanoRepository planoRepository;

	@Autowired
	private ContentRepository contentRepository;

	public List<ConvenioNameDto> getAll() {

		List<ConvenioNameDto> dtos = new ArrayList<>();

		try {

			List<Convenio> objs = convenioRepository.findAll();

			for (int i = 0; i < objs.size(); i++) {
				ConvenioNameDto dto = new ConvenioNameDto();
				dto.setId(objs.get(i).getId());
				dto.setName(objs.get(i).getName());

				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dtos;
	}

	public boolean saveConvenio(String json) {

		boolean resp = true;
		Convenio convenio = new Convenio();
		convenio = gson.fromJson(json, Convenio.class);

		try {

			Convenio convenio2 = convenioRepository.save(convenio);
			saveInfo(convenio2, convenio);
			savePlano(convenio2);

		} catch (Exception e) {
			e.printStackTrace();
			resp = false;
		}

		return resp;
	}

	public boolean saveInfo(Convenio convenio2, Convenio convenio) {

		boolean resp = true;

		Info info = new Info();

		try {

			for (int i = 0; i < convenio.getInfos().size(); i++) {
				info = convenio.getInfos().get(i);
				info.setConvenio(convenio2);
				Info info2 = new Info();

				info2 = infoRepository.save(info);

				// Save content
				for (int j = 0; j < convenio.getInfos().get(i).getContent().size(); j++) {
					Content content = new Content();
					content = convenio.getInfos().get(i).getContent().get(j);
					content.setInfo(info2);

					contentRepository.save(content);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			resp = false;
		}

		return resp;
	}

	public boolean savePlano(Convenio convenio) {

		boolean resp = true;
		Plano plano = new Plano();

		try {

			for (int i = 0; i < convenio.getPlanos().size(); i++) {
				plano = convenio.getPlanos().get(i);
				plano.setConvenio(convenio);

				planoRepository.save(plano);
			}

		} catch (Exception e) {
			e.printStackTrace();
			resp = false;
		}

		return resp;
	}

	public Optional<Convenio> getConvenioById(Long id) {

		Optional<Convenio> convenio = Optional.ofNullable(new Convenio());

		try {

			convenio = convenioRepository.findById(id);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return convenio;
	}

}
