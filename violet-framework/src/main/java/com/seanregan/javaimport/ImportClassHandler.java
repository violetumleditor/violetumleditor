package com.seanregan.javaimport;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author Sean
 */
public class ImportClassHandler {
	private final IJavaParseable mNode;
	
	public ImportClassHandler(IJavaParseable node) {
		mNode = node;
		
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				String fName = chooser.getSelectedFile().getName();
				if (fName.toLowerCase().contains(".java")) {
					mNode.setFileReference(chooser.getSelectedFile().getAbsolutePath());
					CompilationUnit cu = JavaParser.parse(chooser.getSelectedFile());

					final ClassVisitor cVisistor  = new ClassVisitor(cu);
					List<String> cs = cVisistor.getClasses();

					if (cs.size() > 0) {
						ClassSelectionDialog dialog = new ClassSelectionDialog(cs);
						ClassSelectionDialog.SelectionListener listener = new ClassSelectionDialog.SelectionListener() {
							@Override
							public void onSelectionMade(String sel) {
								mNode.setClassName(sel);
								mNode.parseAndPopulate();
							}

							@Override
							public void onCancelled() {
							}
						};
						dialog.setSelectionListener(listener);

						if (cs.size() > 1) dialog.show();
						else listener.onSelectionMade(cs.get(0));
					}
				}
			} catch (Exception f) {
				f.printStackTrace();
			}
		}
	}
}
