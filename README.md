# ISATabParser
This is a prelimianry work to add to Tika the ability to parse the ISA-Tab data formats.
ISA-Tab files are related to [ISA Tools](http://www.isa-tools.org/).

This work aims at providing the following features:
* Detection based on glob patterns and pattern matching on header values.
* One Tika Parser for each ISA-Tab filetype (three parsers: Investigation, Study, Assay)
* The Investigation parser gets only metadata. Since ISA-Tab files are organized Top-Down, I am working on extending it in order to parse related files (study and assay files) starting from investigation.
* Study and Assay parsers rely on Apache Commons CSV to perform parsing, because both study and assay files are organized in a per-row basis and they essentially are CSV files using the tab character as delimiter.

## What’s the next
The most important improvement is to refine these three parsers and combine them in order to parse a ISArchive as well as possible. Another improvement is better mapping of study and assay data on XHTML structures.

## TIKA-
An issue has been created on the Tika Issue Tracker system: [TIKA-](https://issues.apache.org/jira/browse/TIKA-). Please refer to that issue for comments and feedback.
