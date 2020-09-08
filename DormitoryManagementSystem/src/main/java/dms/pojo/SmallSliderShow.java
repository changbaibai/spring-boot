package dms.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_smallslidershow")
public class SmallSliderShow implements Serializable {
 

		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue
		private Integer id;
		@Column(name="title")
		private String title;
		@Column(name="text")
		private String text;
		@Column(name="filepath")
		private String filepath;
		
		public SmallSliderShow() {
			super();
		}
		
		public String getFilepath() {
			return filepath;
		}
	 
		public void setFilepath(String filepath) {
			this.filepath = filepath;
		}
	 
		public Integer getId() {
			return id;
		}
	 
		public void setId(Integer id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	 


		

}
