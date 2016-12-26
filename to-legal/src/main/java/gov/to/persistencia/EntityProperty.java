package gov.to.persistencia;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação utilizada nos DTOS para facilitar a consulta nas ENTIDADES <br><br>
 * Exemplo:
 * <pre>
 * <code>
 *&#64;Entity
 *class Entidade {
 * 
 *	&#64;Id	
 *	private Long id;
 * 	
 *	&#64;ManyToOne
 *	private Pessoa pessoa;
 *}
 *
 *&#64;Entity
 *class Pessoa {
 * 
 *	&#64;Id	
 *	private Long id;
 *  
 *}
 * 
 * //Utilizando a anotação no DTO:
 *class FiltroEntidadeDTO {
 * 		
 *	&#64;EntityProperty("id")
 *	private Long id;
 * 
 *	&#64;EntityProperty("pessoa.id")
 *	private Long idPessoa;
 * 
 *}
 *</code>
 *</pre>
 *
 * {@link br.com.ged.service.TemplateService}.
 */
@Target(value={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityProperty {
	
	/**
	 * Value.
	 *
	 */
	String value();
	
	public abstract boolean pesquisaExata() default false;
	
	public abstract boolean ignoraCaseSensitive() default false;
	
	public abstract boolean listIds() default false;
}
