/*
 * (C) Copyright 2013 The CloudDOE Project and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *      Wei-Chun Chung (wcchung@iis.sinica.edu.tw)
 *      Yu-Chun Wang (zxaustin@iis.sinica.edu.tw)
 * 
 * CloudDOE Project:
 *      http://clouddoe.iis.sinica.edu.tw/
 */

package tw.edu.sinica.iis.GUI.Operate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLConfigParser {

	protected ProgramInfo programInfo;
	protected List<ParameterItem> parameterItems;
	protected List<LogItem> logItems;
	protected List<DownloadItem> downloadItems;

	protected enum paramType {
		VALUE, INPUT, OUTPUT, WORK, MAPPER, REDUCER, FILE
	};

	protected enum logType {
		SHORT, DETAIL
	};

	protected class ProgramInfo {
		public String name;
		public String jarfile;
		public String clsname;
		public String lastupd;
		public String website;
		public String argformat;
		public boolean streaming;

		public ProgramInfo() {
			set("", "", "", "", "", "", "");
		}

		public ProgramInfo(String name, String jarfile, String clsname,
				String lastupd, String website, String argformat,
				String streaming) {
			set(name, jarfile, clsname, lastupd, website, argformat, streaming);
		}

		public void set(String name, String jarfile, String clsname,
				String lastupd, String website, String argformat,
				String streaming) {
			this.name = name;
			this.jarfile = jarfile;
			this.clsname = clsname;
			this.lastupd = lastupd;
			this.website = website;
			this.argformat = argformat;
			this.streaming = Boolean.parseBoolean(streaming);
		}

		public void clear() {
			set("", "", "", "", "", "", "");
		}

		@Override
		public String toString() {
			return "name=" + name + ", jarfile=" + jarfile + ", clsname="
					+ clsname + ", lastupd=" + lastupd + ", website=" + website
					+ ", argformat=" + argformat + ", streaming=" + streaming;
		}
	}

	protected class ParameterItem {
		public String label;
		public String arg;
		public String value;
		public paramType type;
		public boolean editable;

		public ParameterItem(String label, String arg, String value,
				String type, String editable) {
			this.label = label;
			this.arg = arg;
			this.value = value;
			this.type = paramType.valueOf(type.toUpperCase());
			this.editable = Boolean.parseBoolean(editable);
		}

		@Override
		public String toString() {
			return "label=" + label + ", arg=" + arg + ", value=" + value
					+ ", type=" + type + ", editable=" + editable;
		}
	}

	protected class LogItem {
		public String name;
		public logType type;

		public LogItem(String name, String type) {
			this.name = name;
			this.type = logType.valueOf(type.toUpperCase());
		}

		@Override
		public String toString() {
			return "name=" + name + ", type=" + type;
		}
	}

	protected class DownloadItem {
		public String src;
		public String dst;
		public boolean merge;

		public DownloadItem(String src, String dst, String merge) {
			this.src = src;
			this.dst = dst;
			this.merge = Boolean.parseBoolean(merge);
		}

		@Override
		public String toString() {
			return "src=" + src + ", dst=" + dst + ", merge=" + merge;
		}
	}

	public XMLConfigParser() {
		programInfo = new ProgramInfo();
		parameterItems = new ArrayList<XMLConfigParser.ParameterItem>();
		logItems = new ArrayList<XMLConfigParser.LogItem>();
		downloadItems = new ArrayList<XMLConfigParser.DownloadItem>();
	}

	public void removeAll() {
		programInfo.clear();
		parameterItems.clear();
		logItems.clear();
		downloadItems.clear();
	}

	public boolean load(final String xmlPath) {
		Map<String, String> ns = new HashMap<String, String>();
		ns.put("p", "http://clouddoe.iis.sinica.edu.tw/confXML");

		SAXReader reader = new SAXReader();
		reader.getDocumentFactory().setXPathNamespaceURIs(ns);

		try {
			removeAll();

			Document doc = reader.read(new File(xmlPath));

			Element info = (Element) doc.selectSingleNode("//p:program");
			programInfo.set(info.elementTextTrim("name"),
					info.elementTextTrim("jarfile"),
					info.elementTextTrim("clsname"),
					info.elementTextTrim("lastupd"),
					info.elementTextTrim("website"),
					info.elementTextTrim("argformat"),
					info.elementTextTrim("streaming"));

			List<?> list = doc.selectNodes("//p:parameters/p:parameter");
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				Element element = (Element) iter.next();

				parameterItems.add(new ParameterItem(element
						.elementTextTrim("label"), element
						.elementTextTrim("arg"), element
						.elementTextTrim("value"), element
						.elementTextTrim("type"), element
						.elementTextTrim("editable")));
			}
			list.clear();

			list = doc.selectNodes("//p:logs/p:log");
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				logItems.add(new LogItem(element.elementTextTrim("name"),
						element.elementTextTrim("type")));
			}
			list.clear();

			list = doc.selectNodes("//p:downloads/p:download");
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				downloadItems.add(new DownloadItem(element
						.elementTextTrim("src"),
						element.elementTextTrim("dst"), element
								.elementTextTrim("merge")));
			}
			list.clear();

			return true;
		} catch (DocumentException e) {
			removeAll();

			return false;
		}
	}

	public String genParamValList(final Character delimiter) {
		StringBuilder list = new StringBuilder();

		for (ParameterItem p : parameterItems) {
			list.append(p.value).append(delimiter);
		}

		if (list.charAt(list.length() - 1) == delimiter) {
			list.deleteCharAt(list.length() - 1);
		}

		return list.toString();
	}

	public String genJarPath(final String prefix, final String workpath) {
		String jarPath = "";

		if (!programInfo.streaming) {
			jarPath = prefix + "/" + workpath + "/" + programInfo.jarfile;
		} else {
			jarPath = programInfo.jarfile
					+ "/contrib/streaming/hadoop-streaming-*.jar";
		}

		return jarPath;
	}

	public String genClassName() {
		if (programInfo.clsname == null) {
			return "";
		}

		return programInfo.clsname;
	}

	public String genProgramArgs(final String prefix, final String workpath) {
		String args = programInfo.argformat;

		for (ParameterItem p : parameterItems) {
			String strArg = p.arg + " ";

			switch (p.type) {
			case INPUT:
			case OUTPUT:
			case WORK:
				strArg += prefix + "/" + p.type.toString().toLowerCase();
				break;

			case MAPPER:
			case REDUCER:
			case FILE:
				strArg += prefix + "/" + workpath;
				break;

			default:
				break;
			}

			if (!p.value.equals("")) {
				if (p.type != paramType.VALUE) {
					strArg += "/";
				}
				strArg += p.value;
			}

			args = args.replaceFirst("\\$" + p.type.toString().toLowerCase(),
					strArg);
		}

		return args;
	}

	public ParameterItem getSingleParam(final paramType type) {
		for (ParameterItem p : parameterItems) {
			if (p.type == type) {
				return p;
			}
		}

		return null;
	}

	public LogItem getLog(final logType type) {
		for (LogItem l : logItems) {
			if (l.type == type) {
				return l;
			}
		}

		if (logItems.size() > 0) {
			return logItems.get(0);
		}

		return null;
	}

	public void print() {
		System.out.println(parameterItems.size() + " params and "
				+ downloadItems.size() + " dls");

		System.out.println(programInfo);

		for (ParameterItem p : parameterItems) {
			System.out.println(p);
		}

		for (LogItem l : logItems) {
			System.out.println(l);
		}

		for (DownloadItem d : downloadItems) {
			System.out.println(d);
		}

		System.out.println("vals:\"" + genParamValList(';') + "\"");
		System.out.println("path:\"" + genJarPath("UID", "main") + "\"");
		System.out.println("clas:\"" + genClassName() + "\"");
		System.out.println("args:\"" + genProgramArgs("UID", "main") + "\"");
	}

	protected void finalize() throws Throwable {
		removeAll();

		programInfo = null;
		parameterItems = null;
		logItems = null;
		downloadItems = null;
	};

	public static void main(String[] args) {
		XMLConfigParser configParser = new XMLConfigParser();
		configParser.load("workspace/main/CloudRS.xml");
		configParser.print();
		configParser.removeAll();
	}
}
