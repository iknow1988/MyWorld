class User {
	private String id;
	private String username;
	private String password;

	public User(String id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "<" + id + ", " + username + ", " + password + ">";
	}

	public String getUserName() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getID() {
		return this.id;
	}
}