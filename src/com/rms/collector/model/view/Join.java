package com.rms.collector.model.view;

public class Join {
	private String from;
	private String to;
	private String on;
	private boolean max;
	public Join(String from, String to, String on) {
		this.from = from;
		this.to = to;
		this.on = on;
	}
	public Join(String from, String on) {
		this.from = from;
		this.to = from;
		this.on = on;
	}
	public Join(String from, String to, String on, boolean max) {
		this.from = from;
		this.to = to;
		this.on = on;
		this.max = max;
	}
	public Join(String from, String on, boolean max) {
		this.from = from;
		this.to = from;
		this.on = on;
		this.max = max;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getOn() {
		return on;
	}
	public void setOn(String on) {
		this.on = on;
	}
	public boolean isMax() {
		return max;
	}
	public void setMax(boolean max) {
		this.max = max;
	}
}