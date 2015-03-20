/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tika.parser.isatab;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.tika.config.ServiceLoader;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.CloseShieldInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class ISATabStudyParser extends AbstractParser {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -8050020285778309011L;

	private static final ServiceLoader LOADER = new ServiceLoader(
			ISATabInvestigationParser.class.getClassLoader());

	private final Set<MediaType> SUPPORTED_TYPES = Collections
			.singleton(MediaType.application("x-isatab-study"));
	
	@Override
	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	@Override
	public void parse(InputStream stream, ContentHandler handler, Metadata metadata,
			ParseContext context) throws IOException, SAXException, TikaException {
		
		// Automatically detect the character encoding
		AutoDetectReader reader = new AutoDetectReader(new CloseShieldInputStream(stream), metadata, context.get(ServiceLoader.class, LOADER));
		CSVParser csvParser = null;
		
		try {
			Charset charset = reader.getCharset();
			MediaType type = new MediaType(MediaType.application("x-isatab-study"), charset);
			metadata.set(Metadata.CONTENT_TYPE, type.toString());
			metadata.set(Metadata.CONTENT_ENCODING, charset.name());
			
			XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
			xhtml.newline();
			
			csvParser = new CSVParser(reader, CSVFormat.TDF);
			
			xhtml.startDocument();
			xhtml.newline();
			
			xhtml.startElement("table");
			xhtml.newline();
			
			List<CSVRecord> records = csvParser.getRecords();
			
			for (int i=0; i < records.get(0).size(); i++) {
				xhtml.startElement("th");
				xhtml.characters(records.get(0).get(i));
				xhtml.endElement("th");
				xhtml.newline();
			}
			
			for (int i=1; i < records.get(0).size(); i++) {
				xhtml.startElement("tr");
				xhtml.newline();
				
				for (int j = 0; j < records.get(i).size(); j++) {
					xhtml.startElement("td");
					xhtml.characters(records.get(i).get(j));
					xhtml.endElement("td");
					xhtml.newline();
				}
				
				xhtml.endElement("tr");
				xhtml.newline();
			}
			
			xhtml.endElement("table");
			xhtml.newline();
			
			xhtml.endDocument();
			
		} finally {
			reader.close();
			csvParser.close();
		}
	}
}