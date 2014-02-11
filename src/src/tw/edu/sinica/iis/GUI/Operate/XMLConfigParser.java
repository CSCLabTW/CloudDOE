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
		VALUE, INPUT, OUTPUT, WORK
	};

	protected enum logType {
		SHORT, DETAIL
	};
	
	protected enum downloadType {
		GET, GETMERGE
	};

	protected class ProgramInfo {
		public String name;
		public String jarfile;
		public String lastupd;
		public String website;
		public String argformat;

		public ProgramInfo() {
			set("", "", "", "", "");
		}

		public ProgramInfo(String name, String jarfile, String lastupd,
				String website, String argformat) {
			set(name, jarfile, lastupd, website, argformat);
		}

		public void set(String name, String jarfile, String lastupd,
				String website, String argformat) {
			this.name = name;
			this.jarfile = jarfile;
			this.lastupd = lastupd;
			this.website = website;
			this.argformat = argformat;
		}

		public void clear() {
			set("", "", "", "", "");
		}

		@Override
		public String toString() {
			return "name=" + name + ", jarfile=" + jarfile + ", lastupd="
					+ lastupd + ", website=" + website + ", argformat="
					+ argformat;
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
		public downloadType method;
		public String src;
		public String dst;

		public DownloadItem(String method, String src, String dst) {
			this.method = downloadType.valueOf(method.toUpperCase());
			this.src = src;
			this.dst = dst;
		}

		@Override
		public String toString() {
			return "method=" + method + ", src=" + src + ", dst=" + dst;
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
					info.elementTextTrim("lastupd"),
					info.elementTextTrim("website"),
					info.elementTextTrim("argformat"));

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
				downloadItems
						.add(new DownloadItem(
								element.elementTextTrim("method"), element
										.elementTextTrim("src"), element
										.elementTextTrim("dst")));
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

	public String genProgramArgs(final String prefix) {
		String args = programInfo.argformat;

		for (ParameterItem p : parameterItems) {
			String strArg = p.arg + " ";

			if (p.type != paramType.VALUE) {
				strArg += prefix + "/" + p.type.toString().toLowerCase();
			}

			if(!p.value.equals("")) {
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
		
		if(logItems.size() > 0) {
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
		System.out.println("args:\"" + genProgramArgs("UID") + "\"");
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
