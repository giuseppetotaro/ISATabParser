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

import static org.junit.Assert.*;

import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.xml.sax.ContentHandler;

public class ISATabStudyParserTest {

	@Test
	public void testParseStudy() throws Exception {
		String path = "/test-documents/testISATab_BII-I-1/s_BII-S-1.txt";
		
		// first row values
		String[] values = {
				"culture1",
				"Saccharomyces cerevisiae (Baker's yeast)",
				"KanMx4 MATa/MATalpha ura3-52/ura3-52 leu2-1/+trp1-63/+his3-D200/+ hoD KanMx4/hoD", 
				"growth protocol", 
				"C-0.07-aliquot1",
				"carbon", 
				"EFO", 
				"http://purl.obolibrary.org/obo/CHEBI_27594",
				"0.07", 
				"l/hour" 
			};

		Parser parser = new ISATabStudyParser();

		ContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		ParseContext context = new ParseContext();
		InputStream stream = null;
		try {
			stream = ISATabStudyParserTest.class.getResourceAsStream(path);
			parser.parse(stream, handler, metadata, context);

		} finally {
			stream.close();
		}

		String content = handler.toString();
		for (String value : values) {
			assertTrue("Did not contain expected text: " + value, content.contains(value));
		}
	}
}
