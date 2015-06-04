package models.dto;

public class Request {

	public enum Params {
		
		USER_ID("userId"),
		HASH("hash");
		
		String param;
		
		private Params(String param) {
			this.param = param;
		}
		public String getValue() {
			return this.param;
		}
	}
}
