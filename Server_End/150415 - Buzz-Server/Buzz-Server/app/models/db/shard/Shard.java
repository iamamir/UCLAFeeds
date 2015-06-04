package models.db.shard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "shard")
@NamedQueries({
	@NamedQuery(name = "Shard.getEnabledShards", query = "SELECT s FROM Shard s WHERE s.startedAt < :startedAt")
})
public class Shard {
	
	@Id
	@Column(name = "id", length = 10)
	long id;
	
	@Column(name = "weight", length = 10)
	long weight;
	
	@Column(name = "started_at", length = 10)
	long startedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

	public long getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(long startedAt) {
		this.startedAt = startedAt;
	}
	
}
