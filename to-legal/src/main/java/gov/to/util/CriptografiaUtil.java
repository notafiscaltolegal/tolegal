package gov.to.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;

import gov.goias.util.TextUtils;

/**
 * 
 * @author pedro.oliveira
 * 
 * <p>Classe utilizada para criptografar dados que possam ser transmitidos ou obtidos via rede.</p>
 * 
 * <p>Possue basicamente dois métodos:</p>
 * 
 * <ul>
 * 	<li>{@link CriptografiaUtil#criptografar(String)}</li>
 * 	<li>{@link CriptografiaUtil#descriptografar(String)}</li>
 * </ul>
 *  
 *
 */
public final class CriptografiaUtil {
	
	/**
	 * Quando criptografa um objeto, ele é bloqueado por uma chave e só pode ser descriptografado se for informado a mesma chave da criptografia.<br>
	 * 
	 * Nessa variável é armazenada o valor gerado aleatoriamente da chave.
	 */
	private static String keySecretSpec = null;
	
	/**
	 * Quando criptografa um objeto, ele é bloqueado por uma chave e só pode ser descriptografado se for informado a mesma chave da criptografia.<br>
	 * 
	 * Nessa variável é armazenada o valor gerado aleatoriamente da chave.
	 */
	private static String keyIvParameterSpec = null;
	
	private static String ALGORITMO_CRIPTOGRAFICA = "AES/CBC/PKCS5PADDING";
	private static String TIPO_CRIPTOGRAFIA_AES = "AES";
	
	private static String ENCODING = "UTF-8";
	
	static {
		keySecretSpec = "96d7a287-1099-47";
		keyIvParameterSpec = "5f74a10a-2ad1-4e";
	}
	
	private CriptografiaUtil(){
		//Evita instanciação
	}
	
	/**
	 * 
	 * <p>Criptografa objeto:</p>
	 * 
	 * <p>1 - Criptografa String comum OU Criptografa Objeto convertendo em Json.</p>
	 * 
	 * <p>2 - Criptografa String em array de bytes.</p>
	 * 
	 * <p>3 - Converte o array de bytes em encode Base64.</p>
	 * 
	 * 
	 * @param valor {@link String} 
	 * 
	 * @return String criptografada códificada em Base64.
	 */
    public static String criptografar(Object obj) {
    	
    	String valor = transformaObjetoEmJSon(obj);
    	
    	String valorCriptografado = null;
    	
        try {
           
            byte[] criptByte = cipher(Cipher.ENCRYPT_MODE).doFinal(valor.getBytes());
            
            valorCriptografado = Base64.encodeBase64String(criptByte);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return valorCriptografado;
    }

    /**
     * Converte o objeto em string usando Json.
     * 
     * @param obj
     * @return Json do objeto
     */
	private static String transformaObjetoEmJSon(Object obj) {
		
		String valor = null;
		Gson gson = new Gson();
			
		valor = gson.toJson(obj);
			
		return valor;
	}
	
	/**
	 * Descriptografa o objeto e retorna o valor a partir do tipoRetorno informado ({@link List} ou ({@link Object})
	 * 
	 * Métodos que utilizam:
	 * 
	 *  <ul>
	 *  	<li>{@link #descriptografarObjeto(Object, Class)}</li>
	 *  	<li>{@link #descriptografarList(Object, Class)}</li>
	 *  </ul>
	 * 
	 * @param criptografia Valor retornado do método {@link #criptografar(Object)}
	 * @param tipoRetorno {@link List}(Tipada) ou {@link Object}(Tipado)
	 * @return
	 * 
	 * Retorna o valor original descriptografado já com a tipagem utilizada no parâmetro tipoRetorno.
	 */
	private static <T> T descriptografar(Object criptografia, Type tipoRetorno) {
		
		String valorOriginal = null;
		
		if (criptografia != null && criptografia instanceof String){
			
			valorOriginal = criptografia.toString();
		}else{
			return null;
		}
        
		try {
        	
            byte[] originalByte = cipher(Cipher.DECRYPT_MODE).doFinal(Base64.decodeBase64(valorOriginal));

            valorOriginal = new String(originalByte);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		return new Gson().fromJson(valorOriginal, tipoRetorno);
	}

	/**
	 * <p>Descriptografa o objeto e retorna o valor a partir do parâmetro objClass.</p>
	 * 
	 * <p>Modo de usar:</p>
	 * 
	 *  <ul>
	 *  	<li>{@link descriptografarObjeto("teste", String.class)}</li>
	 *  	<li>{@link descriptografarObjeto(obj, Pessoa.class)}</li>
	 *  </ul>
	 * 
	 * @param criptografia Valor retornado do método {@link #criptografar(Object)}
	 * @param objClass .class do retorno.
	 * @return
	 * 
	 * Retorna objeto utilizando a referência do parâmetro objClass.
	 */
	public static <T> T descriptografarObjeto(Object criptografia, Class<T> objClass) {
		
        return descriptografar(criptografia, objClass);
    }
	
	/**
	 * <p>Descriptografa o objeto e retorna um {@link List} com o tipo informado no parâmetro tipoList.</p>
	 * 
	 * <p>Modo de usar:</p>
	 * 
	 *  <ul>
	 *  	<li>Passo 1 - {@code List<Pessoa> listPessoa = new ArrayList<Pessoa>()}</li>
	 *  	<li>Passo 2 - {@code String listaCriptografada = CriptografiaUtil.criptografar(listPessoa)}</li>
	 *  	<li>Passo 3 - {@code List<Pessoa> list = descriptografarList(listaCriptografada, Pessoa.class)}</li>
	 *  </ul>
	 * 
	 * @param criptografia Valor retornado do método {@link #criptografar(Object)}
	 * @param tipoList .class do tipo do {@link List}.
	 * @return
	 * 
	 * Retorna {@link List} tipado com o valor informado do parâmetro tipoList.
	 */
	public static <T> List<T> descriptografarList(Object criptografia, Class<T> tipoList) {
		
		//Cria um Type de uma lista tipada, a partir do tipoList
		Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, List.class, tipoList);
		
        return descriptografar(criptografia, type);
    }
	
	private static Cipher cipher(final int encryptMode) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException {
    	
   	 	IvParameterSpec iv = new IvParameterSpec(keyIvParameterSpec.getBytes(ENCODING));
        SecretKeySpec skeySpec = new SecretKeySpec(keySecretSpec.getBytes(ENCODING),TIPO_CRIPTOGRAFIA_AES);
        Cipher cipher = Cipher.getInstance(ALGORITMO_CRIPTOGRAFICA);
        cipher.init(encryptMode, skeySpec, iv);
        
		return cipher;
	}
}