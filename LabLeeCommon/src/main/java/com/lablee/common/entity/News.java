package com.lablee.common.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.lablee.common.constant.ConstantUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "news")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class News {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, nullable = false)
	private String title;
	
	@Column(columnDefinition = "VARCHAR(512)", nullable = true)
	private String shortDescription;
	
	@Column(columnDefinition = "TEXT", nullable = true)
	private String content;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime createAt;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime updateAt;
	
	private String thumbnail;
	
	private boolean enabled;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		News other = (News) obj;
		return Objects.equals(id, other.id);
	}
	
	@Transient
	public String getThumbnailPath() {
		if (this.id == null || this.thumbnail == null) {
			return "/images/default-user.png";
		}

		return new StringBuffer(ConstantUtil.PATH_NEWS_THUMBNAIL_STORED_DEFAULT).append(this.id).append("/")
				.append(this.thumbnail).toString();
	}
	
	@Transient
	public String getAuthorFullName() {
		return user.getFullName();
	}
	
	@Transient
	public String getShortTitle() {
		if (title != null && title.length() > 60) {
			return title.substring(0, 60) + "...";
		}
		
		return title;
	}
	
}
