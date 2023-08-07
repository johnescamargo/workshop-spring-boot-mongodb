package com.imav.whatsapp.model;

public class ErrorResponse {

	private Error error;

	public ErrorResponse(Error error) {

		this.error = error;
	}

	public ErrorResponse() {
		new Error();
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "ErrorResponse [error=" + error + "]";
	}

	public class Error {
		private String message;
		private String type;
		private int code;
		private Error_data error_data;
		private int error_subcode;
		private String fbtrace_id;

		public Error(String message, String type, int code, Error_data error_data, int error_subcode,
				String fbtrace_id) {

			this.message = message;
			this.type = type;
			this.code = code;
			this.error_data = error_data;
			this.error_subcode = error_subcode;
			this.fbtrace_id = fbtrace_id;
		}

		public Error() {
			new Error_data();
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public Error_data getError_data() {
			return error_data;
		}

		public void setError_data(Error_data error_data) {
			this.error_data = error_data;
		}

		public int getError_subcode() {
			return error_subcode;
		}

		public void setError_subcode(int error_subcode) {
			this.error_subcode = error_subcode;
		}

		public String getFbtrace_id() {
			return fbtrace_id;
		}

		public void setFbtrace_id(String fbtrace_id) {
			this.fbtrace_id = fbtrace_id;
		}

		@Override
		public String toString() {
			return "Error [message=" + message + ", type=" + type + ", code=" + code + ", error_data=" + error_data
					+ ", error_subcode=" + error_subcode + ", fbtrace_id=" + fbtrace_id + "]";
		}

	}

	public class Error_data {
		private String messaging_product;
		private String details;

		public Error_data(String messaging_product, String details) {

			this.messaging_product = messaging_product;
			this.details = details;
		}

		public Error_data() {

		}

		public String getMessaging_product() {
			return messaging_product;
		}

		public void setMessaging_product(String messaging_product) {
			this.messaging_product = messaging_product;
		}

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}

		@Override
		public String toString() {
			return "Error_data [messaging_product=" + messaging_product + ", details=" + details + "]";
		}

	}

}
