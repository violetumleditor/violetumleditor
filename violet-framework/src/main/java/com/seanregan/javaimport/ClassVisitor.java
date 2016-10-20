package com.seanregan.javaimport;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
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

/**
 * A ClassVisitor which is used to get and store the class information
 * of a .java file
 * @author Sean
 */
public class ClassVisitor extends VoidVisitorAdapter {
	private final HashMap<String, ArrayList<String>> mMethodHash	 = new HashMap<String, ArrayList<String>>();
	private final HashMap<String, ArrayList<String>> mAttributesHash = new HashMap<String, ArrayList<String>>();
	
	/**
	 * A ClassVisitor which is used to get and store the class information
	 * of a .java file
	 * @param cu the CompilationUnit to visit fields on
	 */
	public ClassVisitor(CompilationUnit cu) {
		//Begin search
		visit(cu, null);
	}

	/**
	 * Gets a string that contains all of the methods and their parameters
	 * @param className the name of the class that the methods should belong to
	 * @return a String containing all of the methods and their parameters
	 * from the specified class
	 */
	public String getMethodsString(String className) {
		return getFieldString(className, mMethodHash);
	}

	 /**
	 * Gets a string that contains all of the member variables and their types
	 * @param className the name of the class that the members should belong to
	 * @return a String containing all of the member variables and their parameters
	 * from the specified class
	 */
	public String getAttributesStrings(String className) {
		return getFieldString(className, mAttributesHash);
	}

	/**
	 * Gets a String of data from the specified field
	 * @param className the name of the class to get the fields from
	 * @param fromHash the HashMap that contains the individual Strings for the desired
	 * field type
	 * @return a single String containing the information for the specified field type
	 */
	private String getFieldString(String className, HashMap<String, ArrayList<String>> fromHash) {
		String fieldValue = "";
		
		//Check if class exists
		if (fromHash.containsKey(className)) {
			
			//Get the ArrayList
			ArrayList<String> tokens = fromHash.get(className);
			for (String m : tokens) {
				//Concat strings from ArrayList
				fieldValue += m + "\n";
			}
		}
		
		return fieldValue;
	}

	/**
	 * Gets an List<String> with the names of the classes that the
	 * ClassVisitor found in the .java file
	 * @return a List<String> containing the names of the classes from the
	 * file
	 */
	public List<String> getClasses() {
		ArrayList<String> classes = new ArrayList<String>(mMethodHash.size());
		for (String key : mMethodHash.keySet()) {
			classes.add(key);
		}

		return classes;
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		ArrayList<String> methods;
		ClassOrInterfaceDeclaration cOID = null;
		
		//Parent is a class or interface
		if (n.getParentNode() instanceof ClassOrInterfaceDeclaration) {
			cOID = ((ClassOrInterfaceDeclaration)n.getParentNode());
			String className = getFullClassName(cOID);
			
			//Get the ArrayList<String> for methods that belongs to this class
			methods = getClassArray(className, mMethodHash);
		} else {
			return;
		}

		//Get all the parameters of the method
		String params = "";
		boolean isFirst = true;
		for (Parameter p : n.getParameters()) {
			//Construct the string for the method
			if (!isFirst) params += ", ";
			params += p.getName() + " : " + p.getType();
			isFirst = false;
		}
		
		//Interfaces always have public methods
		int mod = n.getModifiers();
		if (cOID.isInterface()) mod = Modifier.PUBLIC;
		
		//Add the method and its paramters to the List of methods for this class
		methods.add(getModType(mod) + " " + n.getNameExpr() + "(" + params + ")");
		//mMethodsField.setText();

		super.visit(n, arg);
	}

	@Override
	public void visit(FieldDeclaration n, Object arg) {
		ArrayList<String> attributes;
		
		//Parent is a class or interface
		if (n.getParentNode() instanceof ClassOrInterfaceDeclaration) {
			String className = getFullClassName(n.getParentNode());

			//Get the ArrayList<String> for attributes that belongs to this class
			attributes = getClassArray(className, mAttributesHash);
		} else {
			return;
		}

		//Get declaration visibility
		String modType = getModType(n.getModifiers());
		
		//Get each variable and add it to the attributes ArrayList
		//Multiple variables can be declared in a single line
		for (VariableDeclarator var : n.getVariables()) {
			attributes.add(modType + " " + var.getId().getName() + " : " + n.getType());
		}

		super.visit(n, arg);
	}

	/**
	 * Gets the full class name for a class using dot notation
	 * @param startNode the node to start the upward recursive search from
	 * @return a String containing the full class name of the class
	 */
	private String getFullClassName(Node startNode) {
		return getFullClassName(startNode, "");
	}
	
	/**
	 * Gets the full class name for a class using dot notation
	 * @param startNode the node to start the upward recursive search from
	 * @param curString the String to begin concatenation with
	 * @return a String containing the full class name of the class
	 */
	private String getFullClassName(Node startNode, String curString) {
		if (startNode instanceof CompilationUnit) {
			//Base case - end of classes
			return curString;
		} else {
			
			//Add class to the current string
			String dot = ".";
			if (curString.compareTo("") == 0) dot = "";
			if (startNode instanceof ClassOrInterfaceDeclaration) {
				curString = ((ClassOrInterfaceDeclaration) startNode).getName() + dot + curString;
			}
			
			//Recurse upwards
			startNode = startNode.getParentNode();
			return getFullClassName(startNode, curString);
		}
	}
	
	/**
	 * Gets the ArrayList<String> that belongs to the specified ClassName
	 * for the specified type
	 * @param getClass the name of the class to get the ArrayList<String> for
	 * @param fromHash the HashMap of the field to get the ArrayList<String> from
	 * @return an ArrayList<String> to store class data such as methods or attributes
	 */
	private ArrayList<String> getClassArray(String getClass, HashMap<String, ArrayList<String>> fromHash) {
		ArrayList<String> aList = null;
		if (!fromHash.containsKey(getClass)) {
			//New class so make an ArrayList<String> for it
			aList = new ArrayList<String>();
			fromHash.put(getClass, aList);
		} else {
			//Have the class already so get the ArrayList<String>
			aList = fromHash.get(getClass);
		}

		return aList;
	}

	/**
	 * Gets the correct String notation for the specified
	 * modifier. For example public = "+", private = "-", etc
	 * @param mod the modifier to reference
	 * @return the a String containing the correct UML notation for
	 * the specified modifier
	 */
	private String getModType(int mod) {
		//Private by default
		String modType = "-";

		//Public or protected
		if (Modifier.isPublic(mod)) {
			modType = "+";
		} else if (Modifier.isProtected(mod)){
			modType = "#";
		}
		
		//Mark as static if needed
		if (Modifier.isStatic(mod)) {
			modType = "<<static>> " + modType;
		}

		return modType;
	}
}