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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import org.apache.tika.config.ServiceLoader;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.CloseShieldInputStream;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Format Specification:
 * http://isatab.sourceforge.net/docs/ISA-TAB_release-candidate
 * -1_v1.0_24nov08.pdf
 *
 */
public class ISATabInvestigationParser extends AbstractParser {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -7682305811427263516L;

	private static final ServiceLoader LOADER = new ServiceLoader(
			ISATabInvestigationParser.class.getClassLoader());

	private final Set<MediaType> SUPPORTED_TYPES = Collections
			.singleton(MediaType.application("x-isatab-investigation"));
	
	// TODO This list includes only the investigation section.
	private String[] sections = {"INVESTIGATION", "INVESTIGATION PUBLICATIONS", "INVESTIGATION CONTACTS"};

	@Override
	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	@Override
	public void parse(InputStream stream, ContentHandler handler,
			Metadata metadata, ParseContext context) throws IOException,
			SAXException, TikaException {

		// Automatically detect the character encoding
		AutoDetectReader reader = new AutoDetectReader(new CloseShieldInputStream(stream), metadata, context.get(ServiceLoader.class, LOADER));

		try {
			Charset charset = reader.getCharset();
			MediaType type = new MediaType(MediaType.application("x-isatab-investigation"), charset);
			metadata.set(Metadata.CONTENT_TYPE, type.toString());
			metadata.set(Metadata.CONTENT_ENCODING, charset.name());
			extractMetadata(reader, metadata);
			
			XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
			xhtml.startDocument();
			// TODO
			xhtml.endDocument();
		} finally {
			reader.close();
		}
	}

	private void extractMetadata(Reader reader, Metadata metadata) throws IOException {
		boolean section = false;
		
		BufferedReader bufReader = new BufferedReader(reader);
		
		String line = null;
		while ((line = bufReader.readLine()) != null) {
			String[] values = line.split("\t");
			// checks for "investigation" section headers; all uppercase and a single value.
			if (line.toUpperCase(Locale.ENGLISH).equals(line) && (values.length == 1)) {
				// investigation sections only!
				section = Arrays.asList(sections).contains(line);
			}
			else {
				if (section) {
					for (int i=1; i < values.length; i++) {
						metadata.add(values[0], values[i].replaceAll("(^\")|(\"$)",""));
					}
				}
			}
		}
	}
}