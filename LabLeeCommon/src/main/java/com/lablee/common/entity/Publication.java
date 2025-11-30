package com.lablee.common.entity;

import java.util.Objects;

import com.lablee.common.constant.ConstantUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "publications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Publication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String title;
	private String doiLink;
	
	@Column(columnDefinition = "TEXT", nullable = true)
	private String cite;
	
	@Column(columnDefinition = "TEXT", nullable = true)
	private String publicationAbstract;

	private String thumbnail;

	private boolean enabled;

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
		Publication other = (Publication) obj;
		return Objects.equals(id, other.id);
	}

	@Transient
	public String getThumbnailPath() {
		if (this.id == null || this.thumbnail == null) {
			return "/images/default-user.png";
		}

		return new StringBuffer(ConstantUtil.PATH_PUBLICATION_THUMBNAIL_STORED_DEFAULT).append(this.id).append("/")
				.append(this.thumbnail).toString();
	}
	
	@Transient
	public String getShortTitle() {
		if (title != null && title.length() > 60) {
			return title.substring(0, 60) + "...";
		}
		
		return title;
	}
	
	@Transient
	public String getShortCite() {
		if (cite != null && cite.length() > 60) {
			return cite.substring(0, 60) + "...";
		}
		
		return cite;
	}
	
	@Transient
	public String getShortDoiLink() {
		if (doiLink != null && doiLink.length() > 60) {
			return doiLink.substring(0, 60) + "...";
		}
		
		return doiLink;
	}
	
	@Transient
	public String getShortPublicationAbstract() {
		if (publicationAbstract != null && publicationAbstract.length() > 60) {
			return publicationAbstract.substring(0, 60) + "...";
		}
		return publicationAbstract;
	}

}
