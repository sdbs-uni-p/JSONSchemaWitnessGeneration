package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.UnsupportedURIRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Classe di utilità per rappresentare URI
 */
public class URI_JS {
	private String uri;				//ex: properties/aaa/properties/bbb
	private String normalizedURI;	//ex: /$defs/
	private String normalizedName;	//: aaa_properties_bbb

	private static Logger logger = LogManager.getLogger(URI_JS.class);

	public URI_JS(String uri) {
		if(uri.toLowerCase().contains(".json") || !uri.startsWith("#"))
			throw new UnsupportedURIRuntimeException("Unsupported URI ("+ uri+")");

		this.uri = uri;
		logger.trace("Created a new URI_JS: {}", this);
	}
	
	public URI_JS() {
		logger.trace("Creating an empty URI_JS");
	}

	public void found() {
		normalizeURI();
	}

	/**
	 *
	 * @return ritorna l'uri originale se non normalizzato, l'uri normalizzato altrimenti
	 */
	public String toString() {
		if(normalizedURI == null)
			return uri;
		return normalizedName;
	}

	public String getNormalizedName() {
		normalizeURI();
		return normalizedName;
	}

	public void setNormalizedName(String normalizedName) {
		this.normalizedName = normalizedName;
	}

	private void normalizeURI() {
		logger.trace("Trying to normalizing {}", this.uri);

		//start: uri --> #/$defs/a/b/foo
		normalizedName = "";
		normalizedURI = "";
		
		//case #
		if(uri.equals("#")) {
			normalizedURI = "";
			normalizedName = AlgebraStrings.ROOTDEF_DEFAULTNAME;
			logger.trace("Uri[{}] equals # --> returning {} ", uri, this);
			return;
		}
		
		String[] splittedURI = uri.split("/"); // expected: ["#", "$defs", "a", "b", "foo"]
		
		int i = 0;
		
		normalizedURI += "#/$defs/"; 	// uri --> #/$defs
		
		if(splittedURI[i].equals("#")) i++;
		//TODO: controllo indici
		if(i>=splittedURI.length)
			throw new UnsupportedURIRuntimeException("Unsupported URI");
		if(splittedURI[i].equals("definitions") || splittedURI[i].equals("$defs")) i++;

		normalizedName = splittedURI[i++];
		for(; i < splittedURI.length; i++)
			normalizedName += "_"+splittedURI[i];


		logger.trace("Uri[{}] normalized as {} ", uri, this);
		// expected: a_b_foo
	}
	
	public URI_JS clone() {
		URI_JS clone = new URI_JS();
		
		clone.uri = uri;
		clone.normalizedURI = normalizedURI;
		clone.normalizedName = normalizedName;
		
		return clone;
	}
	
	
	
	
	
	
	/*
	Divide l'URI secondo i "/", ritorna poi l'iteratore *
	 */
	public Iterator<String> iterator() {
        Iterator<String> it = new Iterator<>() {

            private int currentIndex;
            private String[] splittedURI;
            
            {
            	splittedURI = uri.split("/");
            	if(splittedURI[0].contains("#")) currentIndex = 1;
            	else currentIndex = 0;
            }
            
            @Override
            public boolean hasNext() {
                return currentIndex != splittedURI.length;
            }

            // *** ATTENZIONE: per far andare avanti l'iteratore, richiamare il metodo remove
            @Override
            public String next() {
            	try {
					return splittedURI[currentIndex];
				}catch (Exception e){
            		throw new NoSuchElementException(e.getMessage());
				}

            }
            
            @Override
            public void remove() {
            	currentIndex++;
            }

			@Override
			public String toString() {
				return "$classname{" +
						"currentIndex=" + currentIndex +
						", splittedURI=" + Arrays.toString(splittedURI) +
						'}';
			}
		};
        return it;
    }
}
