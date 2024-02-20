package com.imav.whatsapp.webhookModel;

public class WebhookLocation {

	public String messaging_product;
	public String to;
	public String type;
	public Location location;

	public WebhookLocation() {
		new Location();
	}

	public String getMessaging_product() {
		return messaging_product;
	}

	public void setMessaging_product(String messaging_product) {
		this.messaging_product = messaging_product;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "WebhookLocation [messaging_product=" + messaging_product + ", to=" + to + ", type=" + type
				+ ", location=" + location + "]";
	}

	public class Location {
		public double longitude;
		public double latitude;
		public String name;
		public String address;

		public Location() {

		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		@Override
		public String toString() {
			return "Location [longitude=" + longitude + ", latitude=" + latitude + ", name=" + name + ", address="
					+ address + "]";
		}

	}

}
