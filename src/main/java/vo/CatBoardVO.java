package vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CatBoardVO {
	private int seq;
	private String id;
	private String title;
	private String content;
	private String regdate;
	private int cnt;
	private int comments;
	private int heart;
	
	public int getHeart() {
		return heart;
	}
	public void setHeart(int heart) {
		this.heart = heart;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {		
		this.regdate = regdate;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	
	@Override
	public String toString() {
		return "CatBoardVO [seq=" + seq + ", id=" + id + ", title=" + title + ", content=" + content + ", regdate="
				+ regdate + ", cnt=" + cnt + ", comments=" + comments + ", heart=" + heart + "]";
	}
} // CatBoardVO