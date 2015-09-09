package com.changhong.pushoutView;

import java.util.ArrayList;

import org.json.JSONArray;

public class LiveHotWiki {

	private static final String TAG = "DTVpushview";
	
	private String wiki_id;
	private String model;
	private String wiki_title;
	private String wiki_cover;
	private String wiki_content;
	private String hot;
	private ArrayList<ProgramInfo> programs;
	
	
	private JSONArray wiki_director;
	private JSONArray wiki_starring;
	private JSONArray wiki_host;
	private JSONArray wiki_guest ;
	private JSONArray tags;


	public LiveHotWiki() {

	}
	
	public JSONArray getWikiDirector() {
		return wiki_director;
	}

	public void setWikiDirector(JSONArray wikidirector) {
		this.wiki_director = wikidirector;
	}
	
	public JSONArray getWikiStarring() {
		return wiki_starring;
	}

	public void setWikiStarring(JSONArray wikistarring) {
		this.wiki_starring = wikistarring;
	}
	

	public JSONArray getWikiHost() {
		return wiki_host;
	}

	public void setWikiHost(JSONArray wikihost) {
		this.wiki_host = wikihost;
	}
	
	public JSONArray getWikiGuest() {
		return wiki_guest;
	}

	public void setWikiGuest(JSONArray wikiguest) {
		this.wiki_guest = wikiguest;
	}
	
	public JSONArray getWikiTags() {
		return tags;
	}

	public void setWikiTags(JSONArray wikitags) {
		this.tags = wikitags;
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

	public ArrayList<ProgramInfo> getPrograms() {
		return programs;
	}

	public void setPrograms(ArrayList<ProgramInfo> programs) {
		this.programs = programs;
	}
}