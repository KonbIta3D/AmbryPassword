package com.ambry.passw.activity;

/**
 * Created with IntelliJ IDEA. User: SKOBELEV Date: 15.08.13 Time: 9:41 
 */
public class Item {
	private CharSequence login;
	private CharSequence password;
	private CharSequence comment;
	private long id;

	public CharSequence getLogin() {
		return login;
	}

	public CharSequence getPassword() {
		return password;
	}

	public CharSequence getComment() {
		return comment;
	}

	public long getId() {
		return id;
	}

	public void setPassword(CharSequence newPassword) {
		this.password = newPassword;
	}

	public Item(String login, String password, String comment, long id) {
		this.login = login;
		this.password = password;
		this.comment = comment;
		this.id = id;
	}

}
