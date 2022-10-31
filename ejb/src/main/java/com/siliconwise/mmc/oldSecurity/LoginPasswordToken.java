package  com.siliconwise.mmc.oldSecurity;


import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

//import org.apache.shiro.authc.UsernamePasswordToken;

@XmlRootElement(name= "user")
//public class LoginPasswordToken extends UsernamePasswordToken  implements Serializable {
public class LoginPasswordToken   implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String NOT_VALID = "loginPasswordToken.notValid" ;

	private String username = null ;
	private char[] password = null ;
	private String urlHote = null ; // ajouter pour prise en compte modif login 
	

	public String  getLogin() {
		return username;
	}
	

	public void setLogin(String login) {
		this.username = login;
	}
	

	public char[] getPassword() {
		return password;
	}

	
	public void setPassword(String password) {
		
	
		this.password = password.toCharArray();
	}
	

	public String getUrlHote() {
		return urlHote;
	}
	

	public void setUrlHote(String urlHote) {
		this.urlHote = urlHote;
	}
	

	@Override
	public String toString() {
		return "LoginPasswordToken [getLogin()=" + getLogin() + ", getPassword()=" + Arrays.toString(getPassword())
				+ "]";
	}
	
	

}
