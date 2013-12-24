package biz.buildit.main;
import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import biz.buildit.util.Approval;
import biz.buildit.util.POStatus;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class PurchaseOrder {

	private Long poId;
    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date startDate;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date endDate;

    /**
     */
    private float price;

    /**
     */
    @ManyToOne
    private Plant plant;
    
    private int siteId;
    
    private POStatus poStatus;
    
    @OneToOne
    private Invoice invoice;
}
