package com.cheng.test.quickindexbar;

public class Friend implements Comparable<Friend> {

	private String name;
	private String pinyin;

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public Friend(String name) {
		this.name = name;
		// new的时候就转化好对应拼音
		setPinyin(PinYinUtil.getPinYin(name));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Friend another) {

		return getPinyin().compareTo(another.getPinyin());
	}

}
