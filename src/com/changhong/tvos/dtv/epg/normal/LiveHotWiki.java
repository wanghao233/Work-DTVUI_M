package com.changhong.tvos.dtv.epg.normal;

import java.util.ArrayList;

public class LiveHotWiki {

	private String wiki_id;
	private String model;
	private String wiki_title;
	private String wiki_cover;
	private String wiki_content;
	private String hot;
	private int index;
	private String wiki_director;
	private String wiki_starring;
	private String wiki_host;
	private String wiki_guest;
	private String tags;
	private ArrayList<LiveHotProgram> programs;

	public LiveHotWiki() {

	}

	public String getWikiId() {
		return wiki_id;
	}

	public void setWikiId(String wiki_id) {
		this.wiki_id = wiki_id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getWikiTitle() {
		return wiki_title;
	}

	public void setWikiTitle(String wiki_title) {
		this.wiki_title = wiki_title;
	}

	public String getWikiCover() {
		return wiki_cover;
	}

	public void setWikiCover(String wiki_cover) {
		this.wiki_cover = wiki_cover;
	}

	public String getWikiContent() {
		return wiki_content;
	}

	public void setWikiContent(String wiki_content) {
		this.wiki_content = wiki_content;
	}

	public String getHot() {
		return hot;
	}

	public void setHot(String hot) {
		this.hot = hot;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getWikiDirector() {
		return wiki_director;
	}

	public void setWikiDirector(String wiki_director) {
		this.wiki_director = wiki_director;
	}

	public String getWikiStarring() {
		return wiki_starring;
	}

	public void setWikiStarring(String wiki_starring) {
		this.wiki_starring = wiki_starring;
	}

	public String getWikiHost() {
		return wiki_host;
	}

	public void setWikiHost(String wiki_host) {
		this.wiki_host = wiki_host;
	}

	public String getWikiGuest() {
		return wiki_guest;
	}

	public void setWikiGuest(String wiki_guest) {
		this.wiki_guest = wiki_guest;
	}

	public ArrayList<LiveHotProgram> getPrograms() {
		return programs;
	}

	public void setPrograms(ArrayList<LiveHotProgram> programs) {
		this.programs = programs;
	}

}
