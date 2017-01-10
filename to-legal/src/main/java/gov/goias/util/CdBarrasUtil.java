package gov.goias.util;

import org.apache.log4j.Logger;
import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.impl.code128.Code128;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.impl.upcean.UPCABean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by lucas-mp on 13/01/15.
 */
public class CdBarrasUtil {

        private static final Logger log = Logger.getLogger(CdBarrasUtil.class);

        public static File geraCodigoDeBarras(HttpServletResponse response, String code) throws IOException {
        //        Interleaved2Of5Bean bean = new Interleaved2Of5Bean();
        //        Code128Bean bean = new Code128Bean();
        //        CodabarBean bean = new CodabarBean();
        //        EAN13Bean bean = new EAN13Bean();
        //        UPCABean bean = new UPCABean();
                Code39Bean bean = new Code39Bean();

                bean.setHeight(7d);

                bean.setBarHeight(5d);

                bean.doQuietZone(false);

                File imagemCdBarras = new File("barcode.jpg");
                OutputStream out =
                        new java.io.FileOutputStream(
                                imagemCdBarras
                        );

                BitmapCanvasProvider provider =
                        new BitmapCanvasProvider(out, "image/jpeg", 200,
                                BufferedImage.TYPE_BYTE_BINARY, false,
                                0);
                bean.generateBarcode(provider, code);

                provider.finish();
                return imagemCdBarras;
        }


}
