package com.seanregan.javaimport;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassVisitor extends VoidVisitorAdapter {
	private final HashMap<String, ArrayList<String>> mMethodHash	 = new HashMap<String, ArrayList<String>>();
	private final HashMap<String, ArrayList<String>> mAttributesHash = new HashMap<String, ArrayList<String>>();
	
	public ClassVisitor(CompilationUnit cu) {
		visit(cu, null);
	}

	public String getMethodsString(String className) {
		return getFieldString(className, mMethodHash);
	}

	public String getAttributesStrings(String className) {
		return getFieldString(className, mAttributesHash);
	}

	private String getFieldString(String className, HashMap<String, ArrayList<String>> fromHash) {
		String fieldValue = "";
		if (fromHash.containsKey(className)) {
			ArrayList<String> tokens = fromHash.get(className);
			for (String m : tokens) {
				fieldValue += m + "\n";
			}
		}
		
		return fieldValue;
	}

	public List<String> getClasses() {
		ArrayList<String> classes = new ArrayList<String>(mMethodHash.size());
		for (String key : mMethodHash.keySet()) {
			classes.add(key);
		}

		return classes;
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		// here you can access the attributes of the method.
		// this method will be called for all methods in this 
		// CompilationUnit, including inner class methods
		//System.out.println(n.getNameExpr());

		ArrayList<String> methods;
		ClassOrInterfaceDeclaration cOID = null;
		if (n.getParentNode() instanceof ClassOrInterfaceDeclaration) {
			cOID = ((ClassOrInterfaceDeclaration)n.getParentNode());
			String className = cOID.getName();
			
			methods = getClassArray(className, mMethodHash);
		} else {
			return;
		}

		String params = "";
		boolean isFirst = true;
		for (Parameter p : n.getParameters()) {
			if (!isFirst) params += ", ";
			params += p.getName() + " : " + p.getType();
			isFirst = false;
		}
		
		//Interfaces always have public methods
		int mod = n.getModifiers();
		if (cOID.isInterface()) mod = Modifier.PUBLIC;
		
		methods.add(getModType(mod) + " " + n.getNameExpr() + "(" + params + ")");
		//mMethodsField.setText();

		super.visit(n, arg);
	}

	@Override
	public void visit(FieldDeclaration n, Object arg) {
		ArrayList<String> attributes;
		if (n.getParentNode() instanceof ClassOrInterfaceDeclaration) {
			String className = ((ClassOrInterfaceDeclaration)n.getParentNode()).getName();

			attributes = getClassArray(className, mAttributesHash);
		} else {
			return;
		}

		String modType = getModType(n.getModifiers());
		
		for (VariableDeclarator var : n.getVariables()) {
			attributes.add(modType + " " + var.getId().getName() + " : " + n.getType());
			//mAttributesField.setText(mAttributesField.getText() + modType + " " + var.getId().getName() + " : " + n.getType() + "\n");
		}

		super.visit(n, arg);
	}

	private ArrayList<String> getClassArray(String getClass, HashMap<String, ArrayList<String>> fromHash) {
		ArrayList<String> aList = null;
		if (!fromHash.containsKey(getClass)) {
			aList = new ArrayList<String>();
			fromHash.put(getClass, aList);
		} else {
			aList = fromHash.get(getClass);
		}

		return aList;
	}

	private String getModType(int mod) {
		String modType = "-";

		if (Modifier.isPublic(mod)) {
			modType = "+";
		} else if (Modifier.isProtected(mod)){
			modType = "#";
		}
		
		if (Modifier.isStatic(mod)) {
			modType = "<<static>> " + modType;
		}

		return modType;
	}
}