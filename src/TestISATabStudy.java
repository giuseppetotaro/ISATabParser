import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.isatab.ISATabStudyParser;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


public class TestISATabStudy {
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 1) {
			System.err.println("Usage :" + TestISATabStudy.class.getName() + " /path/to/input");
		}
		String fileName = args[0];
		
		Parser parser = new ISATabStudyParser();
		InputStream stream = TikaInputStream.get(new File(fileName));
		ContentHandler handler = new ToHTMLContentHandler();
		Metadata metadata = new Metadata();
		ParseContext context = new ParseContext();
		
		try {
			System.out.println("Parsing ISA-Tab study file...");
			parser.parse(stream, handler, metadata, context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(handler.toString());
	}
}
