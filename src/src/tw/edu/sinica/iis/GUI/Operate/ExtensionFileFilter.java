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

/**
 * Inherited FileFilter class to facilitate reuse when multiple file filter
 * selections are required. For example purposes, I used a static nested class,
 * which is defined as below as a member of our original FileChooserExample
 * class.
 */
class ExtensionFileFilter extends javax.swing.filechooser.FileFilter {

	private java.util.List<String> extensions;
	private String description;

	public ExtensionFileFilter(String[] exts, String desc) {
		if (exts != null) {
			extensions = new java.util.ArrayList<String>();

			for (String ext : exts) {

				// Clean array of extensions to remove "."
				// and transform to lowercase.
				extensions.add(ext.replace(".", "").trim().toLowerCase());
			}
		} // No else need; null extensions handled below.

		// Using inline if syntax, use input from desc or use
		// a default value.
		// Wrap with an if statement to default as well as
		// avoid NullPointerException when using trim().
		description = (desc != null) ? desc.trim() : "Custom File List";
	}

	// Handles which files are allowed by filter.
	@Override
	public boolean accept(File f) {

		// Allow directories to be seen.
		if (f.isDirectory())
			return true;

		// exit if no extensions exist.
		if (extensions == null)
			return false;

		// Allows files with extensions specified to be seen.
		for (String ext : extensions) {
			if (f.getName().toLowerCase().endsWith("." + ext))
				return true;
		}

		// Otherwise file is not shown.
		return false;
	}

	// 'Files of Type' description
	@Override
	public String getDescription() {
		return description;
	}

}
