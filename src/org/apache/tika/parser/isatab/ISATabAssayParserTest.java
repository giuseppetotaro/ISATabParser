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

public class ISATabAssayParserTest {

	@Test
	public void testParseStudy() throws Exception {
		String path = "/test-documents/testISATab_BII-I-1/a_metabolome.txt";
		
		// first row values
		String[] values = {
				"C-0.1-aliquot1",
				"internal",
				"EHDAA",
				"http://purl.obolibrary.org/obo/EHDAA_7477",
				"metabolite extraction",
				"4",
				"microliter",
				"UO",	
				"http://purl.obolibrary.org/obo/UO_0000101",
				"200",
				"microliter",
				"UO",
				"http://purl.obolibrary.org/obo/UO_0000101",
				"S-0.2-aliquot8",
				"JIC36_Sulphate_0.20_Internal_1_3",
				"JIC36_Sulphate_0.20_Internal_1_3.txt",
				"carbon",
				"EFO",
				"http://purl.obolibrary.org/obo/CHEBI_27594",
				"0.1",
				"l/hour"
			};

		Parser parser = new ISATabAssayParser();

		ContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		ParseContext context = new ParseContext();
		InputStream stream = null;
		try {
			stream = ISATabAssayParserTest.class.getResourceAsStream(path);
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
