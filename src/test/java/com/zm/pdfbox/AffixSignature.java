package com.zm.pdfbox;
/*import java.awt.geom.AffineTransform;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.security.Signature;

import java.util.ArrayList;

import org.bouncycastle.cert.X509CertificateHolder;

import org.bouncycastle.cert.jcajce.JcaCertStore;

import org.bouncycastle.cms.CMSProcessableByteArray;

import org.bouncycastle.cms.CMSTypedData;

import org.bouncycastle.cms.SignerInfoGenerator;

import org.bouncycastle.cms.SignerInfoGeneratorBuilder;

import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import org.bouncycastle.util.Store;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import java.security.KeyStore;

import java.security.PrivateKey;

import java.security.cert.CertStore;

import java.security.cert.Certificate;

import java.security.cert.CollectionCertStoreParameters;

import java.security.cert.X509Certificate;

import java.text.DecimalFormat;

import java.text.SimpleDateFormat;

import java.util.Arrays;

import java.util.Calendar;

import java.util.Date;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import org.apache.pdfbox.cos.COSArray;

import org.apache.pdfbox.cos.COSDictionary;

import org.apache.pdfbox.cos.COSName;

import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPage;

import org.apache.pdfbox.pdmodel.PDResources;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import org.apache.pdfbox.pdmodel.common.PDStream;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import org.apache.pdfbox.pdmodel.font.PDFont;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectForm;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;

import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;

import org.bouncycastle.cms.CMSSignedData;

import org.bouncycastle.cms.CMSSignedDataGenerator;

import org.bouncycastle.cms.CMSSignedGenerator;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AffixSignature {
    String path = "D:\\reports\\";

    String onlyFileName = "";

    String pdfExtension = ".pdf";

    String pdfFileName = "";

    String pdfFilePath = "";

    String signedPdfFileName = "";

    String signedPdfFilePath = "";

    String ownerPassword = "";

    String tempSignedPdfFileName = "";

    String tempSignedPdfFilePath = "";

    String userPassword = "";

    String storePath = "resources/my.p12";

    String entryAlias = "signerCert";

    String keyStorePassword = "password";

    ByteArrayOutputStream documentOutputStream = null;

    private Certificate[] certChain;

    private static BouncyCastleProvider BC = new BouncyCastleProvider();

    int offsetContentStart = 0;

    int offsetContentEnd = 0;

    int secondPartLength = 0;

    int offsetStartxrefs = 0;

    String contentString = "";

    OutputStream signedPdfFileOutputStream;

    OutputStream pdfFileOutputStream;

    public AffixSignature() {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh_mm_ss");

            onlyFileName = "Report_" + timeFormat.format(new Date());

            pdfFileName = onlyFileName + ".pdf";

            pdfFilePath = path + pdfFileName;

            File pdfFile = new File(pdfFilePath);

            pdfFileOutputStream = new FileOutputStream(pdfFile);

            signedPdfFileName = "Signed_" + onlyFileName + ".pdf";

            signedPdfFilePath = path + signedPdfFileName;

            File signedPdfFile = new File(signedPdfFilePath);

            signedPdfFileOutputStream = new FileOutputStream(signedPdfFile);

            String tempFileName = "Temp_Report_" + timeFormat.format(new Date());

            String tempPdfFileName = tempFileName + ".pdf";

            String tempPdfFilePath = path + tempPdfFileName;

            File tempPdfFile = new File(tempPdfFilePath);

            OutputStream tempSignedPdfFileOutputStream = new FileOutputStream(tempPdfFile);

            PDDocument document = new PDDocument();

            PDDocumentCatalog catalog = document.getDocumentCatalog();

            PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDFont font = PDType1Font.HELVETICA;

            Map fonts = new HashMap();

            fonts = new HashMap();

            fonts.put("F1", font);

// contentStream.setFont(font, 12);

            contentStream.setFont(font, 12);

            contentStream.beginText();

            contentStream.moveTextPositionByAmount(100, 700);

            contentStream.drawString("DIGITAL SIGNATURE TEST");

            contentStream.endText();

            contentStream.close();

            document.addPage(page);

//To Affix Visible Digital Signature

            PDAcroForm acroForm = new PDAcroForm(document);

            catalog.setAcroForm(acroForm);

            PDSignatureField sf = new PDSignatureField(acroForm);

            PDSignature pdSignature = new PDSignature();

            page.getAnnotations().add(sf.getWidget());

            pdSignature.setName("sign");

            pdSignature.setByteRange(new int[]{0, 0, 0, 0});

            pdSignature.setContents(new byte[4 * 1024]);

            pdSignature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);

            pdSignature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);

            pdSignature.setName("NAME");

            pdSignature.setLocation("LOCATION");

            pdSignature.setReason("SECURITY");

            pdSignature.setSignDate(Calendar.getInstance());

            List acroFormFields = acroForm.getFields();

            sf.setSignature(pdSignature);

            sf.getWidget().setPage(page);

            COSDictionary acroFormDict = acroForm.getDictionary();

            acroFormDict.setDirect(true);

            acroFormDict.setInt(COSName.SIG_FLAGS, 3);

            acroFormFields.add(sf);

            PDRectangle frmRect = new PDRectangle();

// float[] frmRectParams = {lowerLeftX,lowerLeftY,upperRightX,upperRight};

// float[] frmRectLowerLeftUpperRightCoordinates = {5f, page.getMediaBox().getHeight() - 50f, 100f, page.getMediaBox().getHeight() - 5f};

            float[] frmRectLowerLeftUpperRightCoordinates = {5f, 5f, 205f, 55f};

            frmRect.setUpperRightX(frmRectLowerLeftUpperRightCoordinates[2]);

            frmRect.setUpperRightY(frmRectLowerLeftUpperRightCoordinates[3]);

            frmRect.setLowerLeftX(frmRectLowerLeftUpperRightCoordinates[0]);

            frmRect.setLowerLeftY(frmRectLowerLeftUpperRightCoordinates[1]);

            sf.getWidget().setRectangle(frmRect);

            COSArray procSetArr = new COSArray();

            procSetArr.add(COSName.getPDFName("PDF"));

            procSetArr.add(COSName.getPDFName("Text"));

            procSetArr.add(COSName.getPDFName("ImageB"));

            procSetArr.add(COSName.getPDFName("ImageC"));

            procSetArr.add(COSName.getPDFName("ImageI"));

            String signImageFilePath = "resources/sign.JPG";

            File signImageFile = new File(signImageFilePath);

            InputStream signImageStream = new FileInputStream(signImageFile);

            PDJpeg img = new PDJpeg(document, signImageStream);

            PDResources holderFormResources = new PDResources();

            PDStream holderFormStream = new PDStream(document);

            PDXObjectForm holderForm = new PDXObjectForm(holderFormStream);

            holderForm.setResources(holderFormResources);

            holderForm.setBBox(frmRect);

            holderForm.setFormType(1);

            PDAppearanceDictionary appearance = new PDAppearanceDictionary();

            appearance.getCOSObject().setDirect(true);

            PDAppearanceStream appearanceStream = new PDAppearanceStream(holderForm.getCOSStream());

            appearance.setNormalAppearance(appearanceStream);

            sf.getWidget().setAppearance(appearance);

            acroFormDict.setItem(COSName.DR, holderFormResources.getCOSDictionary());

            PDResources innerFormResources = new PDResources();

            PDStream innerFormStream = new PDStream(document);

            PDXObjectForm innerForm = new PDXObjectForm(innerFormStream);

            innerForm.setResources(innerFormResources);

            innerForm.setBBox(frmRect);

            innerForm.setFormType(1);

            String innerFormName = holderFormResources.addXObject(innerForm, "FRM");

            PDResources imageFormResources = new PDResources();

            PDStream imageFormStream = new PDStream(document);

            PDXObjectForm imageForm = new PDXObjectForm(imageFormStream);

            imageForm.setResources(imageFormResources);

            byte[] AffineTransformParams = {1, 0, 0, 1, 0, 0};

            AffineTransform affineTransform = new AffineTransform(AffineTransformParams[0], AffineTransformParams[1], AffineTransformParams[2], AffineTransformParams[3], AffineTransformParams[4], AffineTransformParams[5]);

            imageForm.setMatrix(affineTransform);

            imageForm.setBBox(frmRect);

            imageForm.setFormType(1);

            String imageFormName = innerFormResources.addXObject(imageForm, "n");

            String imageName = imageFormResources.addXObject(img, "img");

            innerForm.getResources().getCOSDictionary().setItem(COSName.PROC_SET, procSetArr);

            page.getCOSDictionary().setItem(COSName.PROC_SET, procSetArr);

            innerFormResources.getCOSDictionary().setItem(COSName.PROC_SET, procSetArr);

            imageFormResources.getCOSDictionary().setItem(COSName.PROC_SET, procSetArr);

            holderFormResources.getCOSDictionary().setItem(COSName.PROC_SET, procSetArr);

            String holderFormComment = "q 1 0 0 1 0 0 cm /" + innerFormName + " Do Q \n";

            String innerFormComment = "q 1 0 0 1 0 0 cm /" + imageFormName + " Do Q\n";

            String imgFormComment = "q " + 100 + " 0 0 50 0 0 cm /" + imageName + " Do Q\n";

            appendRawCommands(holderFormStream.createOutputStream(), holderFormComment);

            appendRawCommands(innerFormStream.createOutputStream(), innerFormComment);

            appendRawCommands(imageFormStream.createOutputStream(), imgFormComment);

            documentOutputStream = new ByteArrayOutputStream();

            document.save(documentOutputStream);

            document.close();

            tempSignedPdfFileOutputStream.write(documentOutputStream.toByteArray());

            generateSignedPdf();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void appendRawCommands(OutputStream os, String commands) throws IOException {
        os.write(commands.getBytes("ISO-8859-1"));

        os.close();

    }

    public void generateSignedPdf() {
        try {
//Find the Initial Byte Range Offsets

            String docString = new String(documentOutputStream.toByteArray(), "ISO-8859-1");

            offsetContentStart = (documentOutputStream.toString().indexOf("Contents

                    offsetContentEnd = (documentOutputStream.toString().indexOf("000000>") + 7);

            secondPartLength = (documentOutputStream.size() - documentOutputStream.toString().indexOf("000000>") - 7);

//Calculate the Updated ByteRange

            String initByteRange = "";

            if (docString.indexOf("/ByteRange [0 1000000000 1000000000 1000000000]") > 0) {
                initByteRange = "/ByteRange [0 1000000000 1000000000 1000000000]";

            } else if (docString.indexOf("/ByteRange [0 0 0 0]") > 0) {
                initByteRange = "/ByteRange [0 0 0 0]";

            } else {
                System.out.println("No /ByteRange Token is Found!");

                System.exit(1);

            }

            String interimByteRange = "/ByteRange [0 " + offsetContentStart + " " + offsetContentEnd + " " + secondPartLength + "]";

            int byteRangeLengthDifference = interimByteRange.length() - initByteRange.length();

            offsetContentStart = offsetContentStart + byteRangeLengthDifference;

            offsetContentEnd = offsetContentEnd + byteRangeLengthDifference;

            String finalByteRange = "/ByteRange [0 " + offsetContentStart + " " + offsetContentEnd + " " + secondPartLength + "]";

            byteRangeLengthDifference += interimByteRange.length() - finalByteRange.length();

//Replace the ByteRange

            docString = docString.replace(initByteRange, finalByteRange);

//Update xref Table

            int xrefOffset = docString.indexOf("xref");

            int startObjOffset = docString.indexOf("0000000000 65535 f") + "0000000000 65535 f".length() + 1;

            int trailerOffset = docString.indexOf("trailer") - 2;

            String initialXrefTable = docString.substring(startObjOffset, trailerOffset);

            int signObjectOffset = docString.indexOf("/Type /Sig") - 3;

            String updatedXrefTable = "";

            while (initialXrefTable.indexOf("n") > 0) {
                String currObjectRefEntry = initialXrefTable.substring(0, initialXrefTable.indexOf("n") + 1);

                String currObjectRef = currObjectRefEntry.substring(0, currObjectRefEntry.indexOf(" 00000 n"));

                int currObjectOffset = Integer.parseInt(currObjectRef.trim().replaceFirst("^0+(?!$)", ""));

                if ((currObjectOffset + byteRangeLengthDifference) > signObjectOffset) {
                    currObjectOffset += byteRangeLengthDifference;

                    int currObjectOffsetDigitsCount = Integer.toString(currObjectOffset).length();

                    currObjectRefEntry = currObjectRefEntry.replace(currObjectRefEntry.substring(currObjectRef.length() - currObjectOffsetDigitsCount, currObjectRef.length()), Integer.toString(currObjectOffset));

                    updatedXrefTable += currObjectRefEntry;

                } else {
                    updatedXrefTable += currObjectRefEntry;

                }

                initialXrefTable = initialXrefTable.substring(initialXrefTable.indexOf("n") + 1);

            }

//Replace with Updated xref Table

            docString = docString.substring(0, startObjOffset).concat(updatedXrefTable).concat(docString.substring(trailerOffset));

//Update startxref

            int startxrefOffset = docString.indexOf("startxref");

//Replace with Updated startxref

            docString = docString.substring(0, startxrefOffset).concat("startxref\n".concat(Integer.toString(xrefOffset))).concat("\n%%EOF\n");

//Construct Original Document For Signature by Removing Temporary Enveloped Detached Signed Content(000...000)

            contentString = docString.substring(offsetContentStart + 1, offsetContentEnd - 1);

            String docFirstPart = docString.substring(0, offsetContentStart);

            String docSecondPart = docString.substring(offsetContentEnd);

            String docForSign = docFirstPart.concat(docSecondPart);

//Generate Signature

            pdfFileOutputStream.write(docForSign.getBytes("ISO-8859-1"));

            File keyStorefile = new File(storePath);

            InputStream keyStoreInputStream = new FileInputStream(keyStorefile);

            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());

            certChain = keyStore.getCertificateChain(entryAlias);

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(entryAlias, keyStorePassword.toCharArray());

            List certList = new ArrayList();

            certList = Arrays.asList(certChain);

            Store store = new JcaCertStore(certList);

// String algorithm="SHA1WithRSA";

// String algorithm="SHA2WithRSA";

            String algorithm = "MD5WithRSA";

//String algorithm = "DSA";

//Updated Sign Method

            CMSTypedData msg = new CMSProcessableByteArray(docForSign.getBytes("ISO-8859-1"));

            CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

            *//* Build the SignerInfo generator builder, that will build the generator... that will generate the SignerInformation... *//*

            SignerInfoGeneratorBuilder signerInfoBuilder = new SignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider(BC).build());

//JcaContentSignerBuilder contentSigner = new JcaContentSignerBuilder("SHA2withRSA");

            JcaContentSignerBuilder contentSigner = new JcaContentSignerBuilder(algorithm);

            contentSigner.setProvider(BC);

            SignerInfoGenerator signerInfoGenerator = signerInfoBuilder.build(contentSigner.build(privateKey), new X509CertificateHolder(certList.get(0).getEncoded()));

            generator.addSignerInfoGenerator(signerInfoGenerator);

            generator.addCertificates(store);

            CMSSignedData signedData = generator.generate(msg, false);

            String apHexEnvelopedData = org.apache.commons.codec.binary.Hex.encodeHexString(signedData.getEncoded()).toUpperCase();

//Construct Content Tag Data

            contentString = apHexEnvelopedData.concat(contentString).substring(0, contentString.length());

            contentString = "");

//Construct Signed Document

            String signedDoc = docFirstPart.concat(contentString).concat(docSecondPart);

//Write Signed Document to File

            signedPdfFileOutputStream.write(signedDoc.getBytes("ISO-8859-1"));

            signedPdfFileOutputStream.close();

            signedDoc = null;

        } catch (Exception e) {
            throw new RuntimeException("Error While Generating Signed Data", e);

        }

    }

    public static void main(String[] args) {
        AffixSignature affixSignature = new AffixSignature();

    }

}*/