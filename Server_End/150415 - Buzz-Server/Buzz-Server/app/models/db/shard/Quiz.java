package models.db.shard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import models.db.common.NonShardedEntity;
import models.dto.Response;

import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;

@Entity
@Table(name = "quiz")
@NamedQueries({
	@NamedQuery(name = "Quiz.getQuizById", query = "SELECT q FROM Quiz q WHERE q.id = :id"),
	@NamedQuery(name = "Quiz.getQuizByIds", query = "SELECT q FROM Quiz q WHERE q.id IN :ids"),
	@NamedQuery(name = "Quiz.getQuizIdByLevel", query = "SELECT q.id FROM Quiz q WHERE q.level = :level")
})
public class Quiz extends NonShardedEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 10)
	long id;
	
	@Column(name = "level", length = 10)
	long level;
	
	@Column(name = "question", length = 1000)
	String question;

	@Column(name = "option_a", length = 255)
	String optionA;
	
	@Column(name = "option_b", length = 255)
	String optionB;
	
	@Column(name = "option_c", length = 255)
	String optionC;
	
	@Column(name = "option_d", length = 255)
	String optionD;
	
	@Column(name = "answer", length = 1)
	String answer;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLevel() {
		return level;
	}

	public void setLevel(long level) {
		this.level = level;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}

	public String getOptionD() {
		return optionD;
	}

	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public ObjectNode getJson() {
		ObjectNode quiz = Json.newObject();
		quiz.put(Response.Params.QUIZ_ID.getValue(), this.getId());
		quiz.put(Response.Params.QUIZ_LEVEL.getValue(), this.getLevel());
		quiz.put(Response.Params.QUIZ_QUESTION.getValue(), this.getQuestion());
		quiz.put(Response.Params.QUIZ_OPTION_A.getValue(), this.getOptionA());
		quiz.put(Response.Params.QUIZ_OPTION_B.getValue(), this.getOptionB());
		quiz.put(Response.Params.QUIZ_OPTION_C.getValue(), this.getOptionC());
		quiz.put(Response.Params.QUIZ_OPTION_D.getValue(), this.getOptionD());
		quiz.put(Response.Params.QUIZ_ANSWER.getValue(), this.getAnswer());
		return quiz;
	}	
}
