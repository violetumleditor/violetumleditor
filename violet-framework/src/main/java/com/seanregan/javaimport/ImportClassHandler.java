package com.seanregan.javaimport;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Prompts the user to choose a .java file to import
 * and allows them to pick the class from the file
 * to import onto the node
 * @author Sean
 */
public class ImportClassHandler {
	private static String mLastDir = null;
	
	private final IJavaParseable mNode;
	
	/**
	 * Constructs an ImportClassHandler which Prompts the user to choose a .java file to import
	 * and allows them to pick the class from the file to import
	 * @param node the IJavaParseable node to fill with the results of the import
	 */
	public ImportClassHandler(IJavaParseable node) {
		mNode = node;
	}
	
	public void show() {
		//Present file chooser
		JFileChooser chooser = new JFileChooser();
		if (mLastDir != null) chooser.setCurrentDirectory(new File(mLastDir));
		
		//Accept only Java files
		chooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Java Files", "java", "java");
		chooser.setFileFilter(filter);
		
		//File selected
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				mLastDir = chooser.getCurrentDirectory().getAbsolutePath();
				
				//Ensure that it's actually a .java
				String fName = chooser.getSelectedFile().getName();
				if (fName.toLowerCase().contains(".java")) {					
					//Create a CompilationUnit for the JavaParser
					CompilationUnit cu = JavaParser.parse(chooser.getSelectedFile());

					//Find all the Methods and Attributes of this file
					final ClassVisitor cVisistor  = new ClassVisitor(cu);
					List<String> cs = cVisistor.getClasses();

					if (cs.size() > 0) {
						//Set the file for the node
						mNode.setFileReference(chooser.getSelectedFile().getAbsolutePath());
						
						//Ask the user to pick the class that they want to choose
						ClassSelectionDialog dialog = new ClassSelectionDialog(cs);
						ClassSelectionDialog.SelectionListener listener = new ClassSelectionDialog.SelectionListener() {
							@Override
							public void onSelectionMade(String sel) {
								//Set the name of the class and populate
								mNode.setClassName(sel);
								mNode.parseAndPopulate();
							}

							@Override
							public void onCancelled() {
							}
						};
						dialog.setSelectionListener(listener);

						if (cs.size() > 1) {
							dialog.show();
						} else {
							//There's only one choice so pick it for the user
							listener.onSelectionMade(cs.get(0));
						}
					}
				}
			} catch (Exception f) {
				f.printStackTrace();
			}
		}
	}
}
