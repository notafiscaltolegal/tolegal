package gov.goias.util;

import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.Locale;

public class MarshallingXmlViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, Locale locale)
            throws Exception {
        MarshallingView view = new MarshallingView();
        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setAutodetectAnnotations(true);
        view.setMarshaller(xStreamMarshaller);
        return view;
    }
}