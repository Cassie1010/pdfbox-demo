package org.apache;

import org.apache.pdfbox.examples.signature.CreateVisibleSignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author: zmm
 * @time: 2021/3/25 14:56
 */
public class Test {

    @org.junit.Test
    public void test() throws IOException {
/*
        // sign PDF
        String inPath = IN_DIR + "sign_me_visible.pdf";
        File destFile;
        try (FileInputStream fis = new FileInputStream(JPEG_PATH))
        {
            CreateVisibleSignature signing = new CreateVisibleSignature(keyStore, PASSWORD.toCharArray());
            signing.setVisibleSignDesigner(inPath, 0, 0, -50, fis, 1);
            signing.setVisibleSignatureProperties("name", "location", "Security", 0, 1, true);
            signing.setExternalSigning(externallySign);
            destFile = new File(OUT_DIR + getOutputFileName("signed{0}_visible.pdf", externallySign));
            signing.signPDF(new File(inPath), destFile, null);
        }*/
    }

}
