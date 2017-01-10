package gov.goias.services;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by lucas-mp on 07/08/15.
 */

public class InfoconvTest {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(InfoConvConfiguration.class, args);

        InfoConvService infoConvService = ctx.getBean(InfoConvService.class);


        System.out.printf(infoConvService.consultaPorCpfInfoConvHomolog("03794778154"));

    }
}
