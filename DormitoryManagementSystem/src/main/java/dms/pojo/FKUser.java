package dms.pojo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="tb_user")
public class FKUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
    private Long id;
	@Column(name="login_name")
	private String loginName;
	private String username;
    private String password;

    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinTable(name="tb_user_role",
	joinColumns={@JoinColumn(name="user_id")},
	inverseJoinColumns={@JoinColumn(name="role_id")})
    private List<FKRole> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}




	public List<FKRole> getRoles() {
		return roles;
	}

	public void setRoles(List<FKRole> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "FKUser [id=" + id + ", loginName=" + loginName + ", username=" + username + ", password=" + password
				+ ", roles=" + roles + "]";
	}

	
	

}
