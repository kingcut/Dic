package com.example.dict_en_vn.db.dao;

import java.io.Serializable;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table NOTE.
 */
public class Note implements Serializable{

    private Long id;
    /** Not-null value. */
    private String word;
    private String content;
    private String pronunciation;
    private String voice;
    private String unsign_vn;
    private Long story;
    private Long family;

    public Note() {
    }

    public Note(Long id) {
        this.id = id;
    }

    public Note(Long id, String word, String content,String pronunciation, String voice, String unsign, Long story, Long family) {
        this.id = id;
        this.word = word;
        this.content = content;
        this.pronunciation = pronunciation;
        this.voice = voice;
        this.unsign_vn = unsign;
        this.story = story;
        this.family = family;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getPronunciation() {
		return pronunciation;
	}

	public void setPronunciation(String pronunciation) {
		this.pronunciation = pronunciation;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getUnsign_vn() {
		return unsign_vn;
	}

	public void setUnsign_vn(String unsign_vn) {
		this.unsign_vn = unsign_vn;
	}

	public Long getStory() {
		return story;
	}

	public void setStory(Long story) {
		this.story = story;
	}

	public Long getFamily() {
		return family;
	}

	public void setFamily(Long family) {
		this.family = family;
	}

    

}
