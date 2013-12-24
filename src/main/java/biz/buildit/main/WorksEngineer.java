package biz.buildit.main;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import biz.buildit.security.Users;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class WorksEngineer extends Users{

}
