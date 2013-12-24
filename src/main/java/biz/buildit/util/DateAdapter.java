package biz.buildit.util;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<Date, Date>{

    @Override
    public Date unmarshal(Date date) throws Exception {
        return date;
    }

    @Override
    public Date marshal(Date date) throws Exception {
        if(null == date) {
            return date;
        }
        return new Date(date.getTime());
    }

}