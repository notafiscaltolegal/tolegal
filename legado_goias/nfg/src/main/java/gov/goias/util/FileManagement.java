package gov.goias.util;

import gov.goias.entidades.enums.DirFilesEnum;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Letícia Álvares on 02/06/16.
 */
public class FileManagement {

    public FileManagement() {

        this.caminhoArquivosECF = DirFilesEnum.DIR_RECEBE_ARQUIVOS_MFD.getValue();
        this.caminhoArquivosECF_OK = DirFilesEnum.DIR_ARQUIVOS_PRONTOS_MFD.getValue();

    }

    public String getCaminhoArquivosECF() {
        return caminhoArquivosECF;
    }

    public String caminhoArquivosECF;

    public String caminhoArquivosECF_OK;

    public void salvarArquivo(MultipartFile file){

        if(! new File(this.getCaminhoArquivosECF()).exists())
        {
            new File(this.getCaminhoArquivosECF()).mkdirs();
        }

        String orgName = file.getOriginalFilename();
        String filePath = this.getCaminhoArquivosECF() + orgName;

        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvarArquivoValidado(String caminhoCompletoArquivo, String nomeArq){
        String sFileOrigem = caminhoCompletoArquivo;
        String filePath = this.getCaminhoArquivosECF_OK() + nomeArq;

        File fileOrigem = new File(caminhoCompletoArquivo);
        File dest = new File(filePath);
        try {
            fileOrigem.renameTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static String unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        String caminhoFinal="";
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file

        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                caminhoFinal = newFile.getAbsolutePath();
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return caminhoFinal;
    }

    //Rotina  feita para ver quais CNPJs tiveram problema com a atualização do NFGoiana - Problema da versão 1.2.0
    public  void percorrerDiretorioLendoTxt(){
        File diretorio = new File("D:\\ArquivosMFD");

        ArrayList<infoECF> listaProblemas = new ArrayList<infoECF>();

        for (File file : diretorio.listFiles()) {
            try {

                if (file.getName().contains("null")){
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String linha = "";

                    //anda pela primeira linha
                    bufferedReader.readLine();
                    //le a segunda linha, onde estao as informacoes
                    linha = bufferedReader.readLine();
                    if (linha != null && linha.length() > 0)
                    {
                        infoECF info = new infoECF();
                        info.setCNPJ(linha.substring(98, 112));
                        info.setNumECF(linha.substring(95, 98));
                        info.setPeriodoInicial(linha.substring(133, 135) + "/" + linha.substring(131, 133) + "/" + linha.substring(127, 131));
                        info.setPeriodoFinal(linha.substring(141,143) +"/" + linha.substring(139,141) +"/" + linha.substring(135,139));

                        listaProblemas.add(info);
                    }

                    fileReader.close();
                    bufferedReader.close();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            PrintWriter writer = new PrintWriter("D:\\ArquivosMFD\\saida.txt", "UTF-8");

            writer.println("CNPJ;NUM. ECF; DATA INICIAL; DATA FINAL");

            for (infoECF info: listaProblemas) {
                writer.println(info.getCNPJ() + ";" + info.getNumECF() + ";" + info.getPeriodoInicial() + ";" + info.getPeriodoFinal());
            }
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public String getCaminhoArquivosECF_OK() {
        return caminhoArquivosECF_OK;
    }

    public void setCaminhoArquivosECF_OK(String caminhoArquivosECF_OK) {
        this.caminhoArquivosECF_OK = caminhoArquivosECF_OK;
    }


    public static String getHashMD5(File arquivo) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        String algoritmoHash = "MD5";
        File f = arquivo;
        MessageDigest digest1 = MessageDigest.getInstance(algoritmoHash);
        InputStream is = new FileInputStream(f);
        byte[] buffer = new byte[8192];

        int read = 0;

        try {
            while ((read = is.read(buffer)) > 0) {
                digest1.update(buffer, 0, read);
            }
            byte[] md5sum = digest1.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);

            if (output.length() != 32) {
                int diferenca = 32 - output.length();
                for (int i = 0; i < diferenca; i++) {
                    output = "0" + output;
                }
            }
            return output.toUpperCase();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("Não foi possível fechar o arquivo para o calculo do Hash", e);
            }
        }
    }



    private class infoECF {


        private String CNPJ;
        private String numECF;
        private String periodoInicial;
        private String periodoFinal;

        public String getCNPJ() {
            return CNPJ;
        }

        public void setCNPJ(String CNPJ) {
            this.CNPJ = CNPJ;
        }

        public String getNumECF() {
            return numECF;
        }

        public void setNumECF(String numECF) {
            this.numECF = numECF;
        }

        public String getPeriodoInicial() {
            return periodoInicial;
        }

        public void setPeriodoInicial(String periodoInicial) {
            this.periodoInicial = periodoInicial;
        }

        public String getPeriodoFinal() {
            return periodoFinal;
        }

        public void setPeriodoFinal(String periodoFinal) {
            this.periodoFinal = periodoFinal;
        }
    }

}


