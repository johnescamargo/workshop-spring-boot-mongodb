package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.entity.CustomerMessage;
import com.imav.whatsapp.entity.ImavButton;
import com.imav.whatsapp.entity.ImavButtonMessage;
import com.imav.whatsapp.model.ButtonReply;
import com.imav.whatsapp.repository.CustomerMessageRepository;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.repository.ImavButtonMessageRepository;
import com.imav.whatsapp.repository.ImavButtonRepository;

@Service
public class DBImavMessageButtonReplyResource {

	@Autowired
	private CustomerMessageRepository messageRepo;

	@Autowired
	private ImavButtonMessageRepository buttonReplyRepo;

	@Autowired
	private ImavButtonRepository buttonMessageRepo;

	@Autowired
	private CustomerRepository customerRepository;

	public void saveImavMessageButtonReplyIntoDatabase(ButtonReply button, String idWamid) {

		try {

			ImavButtonMessage buttonReply = new ImavButtonMessage();
			List<ImavButton> buttonMessages = new ArrayList<>();

			String timestamp = Long.toString(System.currentTimeMillis() / 1000);
			String phone = button.getTo();

			buttonReply.setB_status(1);
			buttonReply.setB_text(button.getInteractive().getBody().getText());
			buttonReply.setB_timestamp(timestamp);
			buttonReply.setB_to(phone);
			buttonReply.setB_type(button.getInteractive().getType());
			buttonReply.setIdWamid(idWamid);

			buttonReplyRepo.save(buttonReply);

			for (int i = 0; i < button.getInteractive().getAction().getButtons().size(); i++) {
				ImavButton buttonMessage = new ImavButton();
				buttonMessage.setB_id(button.getInteractive().getAction().getButtons().get(i).getReply().getId());
				buttonMessage.setB_title(button.getInteractive().getAction().getButtons().get(i).getReply().getTitle());
				buttonMessage.setB_type(button.getInteractive().getAction().getButtons().get(i).getType());
				buttonMessage.setImavButtonMessage(buttonReply);

				buttonMessages.add(buttonMessage);
			}

			buttonMessageRepo.saveAll(buttonMessages);
			updateCustomersTimestamp(phone, timestamp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveImavButtonMessageIntoDb(ImavButtonMessage obj, String idWamid) {

		try {

			String phone = obj.getB_to();
			String timestamp = obj.getB_timestamp();
			
			ImavButtonMessage button = new ImavButtonMessage();
			List<ImavButton> buttonMessages = new ArrayList<>();

			button.setB_text(obj.getB_text());
			button.setB_status(obj.getB_status());
			button.setB_timestamp(obj.getB_timestamp());
			button.setB_to(obj.getB_to());
			button.setB_type(obj.getB_type());
			button.setIdWamid(obj.getIdWamid());

			buttonReplyRepo.save(button);

			for (int i = 0; i < obj.getButtons().size(); i++) {
				ImavButton imavButton = new ImavButton();

				imavButton.setB_id(obj.getButtons().get(i).getB_id());
				imavButton.setB_title(obj.getButtons().get(i).getB_title());
				imavButton.setB_type(obj.getButtons().get(i).getB_type());
				imavButton.setImavButtonMessage(button);

				buttonMessages.add(imavButton);
			}

			buttonMessageRepo.saveAll(buttonMessages);
			updateCustomersTimestamp(phone, timestamp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveMessageButtonReply(CustomerMessage message) {
		try {
			messageRepo.save(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateCustomersTimestamp(String phoneNumber, String timestamp) {

		try {
			Customer customer = new Customer();
			customer = customerRepository.findByPhoneNumber(phoneNumber);
			customer.setTimestamp(timestamp);
			customerRepository.save(customer);
			System.out.println(phoneNumber + ": Updated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
