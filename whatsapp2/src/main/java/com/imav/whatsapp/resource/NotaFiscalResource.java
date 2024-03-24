package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.imav.whatsapp.dto.NotaFiscalDto;
import com.imav.whatsapp.dto.NotaFiscalUpdateDto;
import com.imav.whatsapp.entity.Exames;
import com.imav.whatsapp.entity.NotaFiscal;
import com.imav.whatsapp.repository.ExamesRepository;
import com.imav.whatsapp.repository.NotaFiscalRepository;

@Service
public class NotaFiscalResource {

	private Gson gson = new Gson();

	@Autowired
	private NotaFiscalRepository fiscalRepository;

	@Autowired
	private ExamesRepository examesRepository;;

	public void saveNotaFiscal(String obj) {

		Long timestamp = System.currentTimeMillis() / 1000;

		try {

			// System.out.println(obj);
			NotaFiscal nf = new NotaFiscal();
			nf = gson.fromJson(obj, NotaFiscal.class);
			nf.setTimestampCreated(timestamp);
			nf.setTimestampNf(0L);

			nf = fiscalRepository.save(nf);

			List<Exames> exames = nf.getExames();

			for (int i = 0; i < exames.size(); i++) {
				Exames ex = new Exames();
				ex.setNotaFiscal(nf);
				ex.setName(exames.get(i).getName());
				ex.setNameId(exames.get(i).getNameId());

				examesRepository.save(ex);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Optional<NotaFiscal> findById(Long id) {

		Optional<NotaFiscal> nf = java.util.Optional.empty();

		try {

			nf = fiscalRepository.findById(id);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nf;
	}

	public List<NotaFiscal> findAllByNfNotDone() {

		List<NotaFiscal> nfs = new ArrayList<>();

		try {

			nfs = fiscalRepository.findAllByNfNotDone();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nfs;
	}

	public List<NotaFiscalDto> findAllByNfNotDoneDto() {

		List<NotaFiscalDto> dtos = new ArrayList<>();
		List<NotaFiscal> nfs = new ArrayList<>();

		try {

			nfs = fiscalRepository.findAllByNfNotDone();

			for (int i = 0; i < nfs.size(); i++) {
				NotaFiscalDto dto = new NotaFiscalDto();
				dto.setId(nfs.get(i).getId());
				dto.setNome(nfs.get(i).getNome());
				dto.setTimestampCreated(nfs.get(i).getTimestampCreated());
				dto.setTimestampNf(nfs.get(i).getTimestampNf());
				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dtos;
	}

	public List<NotaFiscalDto> findLiveSearch(@RequestParam String input) {

		List<NotaFiscalDto> dtos = new ArrayList<>();
		List<NotaFiscal> nfs = new ArrayList<>();

		try {

			nfs = fiscalRepository.findLiveSearch(input);

			for (int i = 0; i < nfs.size(); i++) {
				NotaFiscalDto dto = new NotaFiscalDto();
				dto.setId(nfs.get(i).getId());
				dto.setNome(nfs.get(i).getNome());
				dto.setTimestampCreated(nfs.get(i).getTimestampCreated());
				dto.setTimestampNf(nfs.get(i).getTimestampNf());
				dto.setNfDone(nfs.get(i).isNfDone());
				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dtos;
	}

	// TODO
	public List<NotaFiscalDto> findAllByTimestampCreated(@RequestParam String date) {

		List<NotaFiscalDto> dtos = new ArrayList<>();
		List<NotaFiscal> nfs = new ArrayList<>();

		try {

			long beginDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date + " 00:00:01").getTime()
					/ 1000;
			long endDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date + " 23:59:59").getTime()
					/ 1000;

			nfs = fiscalRepository.findAllByTimestampCreated(beginDate, endDate);

			for (int i = 0; i < nfs.size(); i++) {
				NotaFiscalDto dto = new NotaFiscalDto();
				dto.setId(nfs.get(i).getId());
				dto.setNfDone(nfs.get(i).isNfDone());
				dto.setNome(nfs.get(i).getNome());
				dto.setTimestampCreated(nfs.get(i).getTimestampCreated());
				dto.setTimestampNf(nfs.get(i).getTimestampNf());
				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dtos;
	}

	public void updateNotaFiscal(String obj) {

		Long timestamp = System.currentTimeMillis() / 1000;

		try {

			NotaFiscalUpdateDto dto = new NotaFiscalUpdateDto();
			dto = gson.fromJson(obj, NotaFiscalUpdateDto.class);

			long id = dto.getId();
			Optional<NotaFiscal> nfOp = java.util.Optional.empty();
			nfOp = fiscalRepository.findById(id);

			NotaFiscal nf = new NotaFiscal();
			nf = nfOp.get();

			nf.setnfDone(true);
			nf.setNfDoneBy(dto.getNfDoneBy());
			nf.setNfNumero(dto.getNfNumero());
			nf.setTimestampNf(timestamp);

			fiscalRepository.save(nf);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateNF(String obj) {

		try {

			System.out.println(obj);

			NotaFiscal nf = new NotaFiscal();
			NotaFiscal nf2 = new NotaFiscal();
			nf = gson.fromJson(obj, NotaFiscal.class);
			nf2 = gson.fromJson(obj, NotaFiscal.class);

			Long id = (Long) nf.getId();

			Optional<NotaFiscal> nfdb = fiscalRepository.findById(id);

			nf.setTimestampCreated(nfdb.get().getTimestampCreated());
			nf.setTimestampNf(nfdb.get().getTimestampNf());

			nf = fiscalRepository.save(nf);

			updateExames(nf2);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateExames(NotaFiscal nf) {

		List<Exames> examesDb = new ArrayList<>();

		List<Exames> examesNf = new ArrayList<>();

		try {

			examesDb = examesRepository.findAllByNotaFiscal(nf);
			examesNf = nf.getExames();

			// Working - delete if it does not exist
			for (int i = 0; i < examesDb.size(); i++) {

				String nameIdDb = examesDb.get(i).getNameId();
				boolean resp = false;

				for (int j = 0; j < examesNf.size(); j++) {
					if (nameIdDb.equals(examesNf.get(j).getNameId())) {
						resp = true;
						break;
					}
				}

				if (!resp) {
					examesRepository.deleteById(examesDb.get(i).getId());
				}
			}

			for (int i = 0; i < examesNf.size(); i++) {

				String nameIdNf = examesNf.get(i).getNameId();
				//Long id = examesNf.get(i).getId();
				boolean resp = false;

				for (int j = 0; j < examesDb.size(); j++) {
					if (nameIdNf.equals(examesDb.get(j).getNameId())) {
						resp = true;
						break;
					}
				}

				if (!resp) {
					Exames exame = new Exames();
					exame.setName(examesNf.get(i).getName());
					exame.setNameId(examesNf.get(i).getNameId());
					exame.setNotaFiscal(nf);
					exame = examesRepository.save(exame);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
