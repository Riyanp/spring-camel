package com.odenktools.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Handling Permission for END USER (Group).
 *
 * @author Odenktools.
 */
@Getter
@Setter
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Group")
@Table(name = "groups")
@JsonIgnoreProperties(
		ignoreUnknown = true,
		value = {"createdAt", "updatedAt"},
		allowGetters = true
)
@Builder
@SuppressWarnings("unused")
public class Group implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@Size(min = 2, max = 100)
	@Column(name = "group_name", unique = true, nullable = false)
	private String name;

	@Size(min = 2, max = 100)
	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

    @Column(name = "is_deleted", columnDefinition = "default int 0")
    @JsonProperty("is_deleted")
    private int isDeleted;

	/**
	 * Sets created_at before insert.
	 */
	@PrePersist
	public void setCreationDate() {

		this.createdAt = new Date();
	}
}

