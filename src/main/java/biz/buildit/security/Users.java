package biz.buildit.security;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Users {

    /**
     */
    private String username;

    /**
     */
    private String password;

    /**
     */
    private Boolean enabled;
}
