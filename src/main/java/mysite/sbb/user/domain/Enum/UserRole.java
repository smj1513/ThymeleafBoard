package mysite.sbb.user.domain.Enum;

import lombok.Getter;

@Getter
public enum UserRole {
	ADMIN("ADMIN"),
	USER("USER");

	UserRole(String value){
		this.value = value;
	}
	String value;
}
