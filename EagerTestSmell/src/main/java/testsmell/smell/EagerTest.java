package testsmell.smell;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import testsmell.*;
import testsmelldetectorutils.Output;
import testsmelldetectorutils.OutputList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class EagerTest extends AbstractSmell {

    private static final String TEST_FILE = "Test";
    private static final String PRODUCTION_FILE = "Production";
    private String productionClassName;
    private List<SmellyElement> smellyElementList;
    private List<MethodDeclaration> productionMethods;
    ArrayList<OutputList> outs;
    String f, path;
    FileWriter fw;

    public EagerTest() {
    	
        productionMethods = new ArrayList<>();
        smellyElementList = new ArrayList<>();
        
    }


    public String getSmellName() {
        return "Eager Test Smell";
    }

    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }


    public void runAnalysis(CompilationUnit testFileCompilationUnit, ArrayList<File> productionFiles) throws FileNotFoundException {


    	CompilationUnit productionFileCompilationUnit=null;
    	List<CompilationUnit> productionFileCompilationUnits = new ArrayList<CompilationUnit>();
    	FileInputStream productionFileInputStream;
    	for (File file : productionFiles) {
    		if(!StringUtils.isEmpty(file.getAbsolutePath())){
              productionFileInputStream = new FileInputStream(file.getAbsolutePath());
              productionFileCompilationUnit = JavaParser.parse(productionFileInputStream);
              if (productionFileCompilationUnit == null)
                throw new FileNotFoundException();
              productionFileCompilationUnits.add(productionFileCompilationUnit);
          }
		}
        EagerTest.ClassVisitor classVisitor;

        classVisitor = new EagerTest.ClassVisitor(PRODUCTION_FILE);
        for (CompilationUnit compilationUnit : productionFileCompilationUnits) {
        	
            classVisitor.visit(compilationUnit, null);
		}

        classVisitor = new EagerTest.ClassVisitor(TEST_FILE);
        classVisitor.visit(testFileCompilationUnit, null);

    }

   
    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }


   
    private class ClassVisitor extends VoidVisitorAdapter<Void> {
        private MethodDeclaration currentMethod = null;
        TestMethod testMethod;
        private int eagerCount = 0;
        private List<String> productionVariables = new ArrayList<>();
        private List<String> calledMethods = new ArrayList<>();
        private String fileType;

        public ClassVisitor(String type) {
            fileType = type;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            if (Objects.equals(fileType, PRODUCTION_FILE)) {
                productionClassName = n.getNameAsString();
            }
            super.visit(n, arg);
        }

        @Override
        public void visit(EnumDeclaration n, Void arg) {
            if (Objects.equals(fileType, PRODUCTION_FILE)) {
                productionClassName = n.getNameAsString();
            }
            super.visit(n, arg);
        }

        @Override
        public void visit(MethodDeclaration n, Void arg) {
        	
            if (Objects.equals(fileType, TEST_FILE)) {
                if (Util.isValidTestMethod(n)) {
                    currentMethod = n;
                    
                    testMethod = new TestMethod(currentMethod.getNameAsString());
                    testMethod.setHasSmell(false); 
                    super.visit(n, arg);

                    testMethod.setHasSmell(eagerCount > 1); 
                    smellyElementList.add(testMethod);

                    currentMethod = null;
                    eagerCount = 0;
                    productionVariables = new ArrayList<>();
                    calledMethods = new ArrayList<>();
                }
            } else { 
            	
                for (Modifier modifier : n.getModifiers()) {
                    if (modifier.name().toLowerCase().equals("public") || modifier.name().toLowerCase().equals("protected")) {
                        productionMethods.add(n);
                    }
                }

            }
        }

        //normal method check
        //static diye call check kora lagbe
        //variable a method call check
      
        @Override
        public void visit(MethodCallExpr n, Void arg) {
        	
            NameExpr nameExpr = null;
            if (currentMethod != null) {
                if (productionMethods.stream().anyMatch(i -> i.getNameAsString().equals(n.getNameAsString()) &&
                        i.getParameters().size() == n.getArguments().size())) {
                    eagerCount++;
                    if(eagerCount>1) {
                    	OutputList o = new OutputList();
                    	o.setMn(currentMethod.getNameAsString());
                    	o.setLine(Integer.toString(n.getBegin().get().line));
                    	o.setFn(f);
                    	o.setPath(path);
                    	o.setSmell(getSmellName());
                    	outs.add(o);
                    	try {
							fw.write(getSmellName()+","+f+","+currentMethod.getNameAsString()+","+Integer.toString(n.getBegin().get().line)+","+path+"\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	System.out.println("Found Eager Smell at " + currentMethod.getNameAsString()+ " Line: " + n.getBegin().get().line + " ...path:" + path);
                    }
                    calledMethods.add(n.getNameAsString());
                } else {
                    if (n.getScope().isPresent()) {
                        
                        if ((n.getScope().get() instanceof MethodCallExpr)) {
                            getFinalScope(n);
                            nameExpr = tempNameExpr;
                        }
                        if (n.getScope().get() instanceof NameExpr) {
                            nameExpr = (NameExpr) n.getScope().get();
                        }

                        if (nameExpr != null) {
                            if (nameExpr.getNameAsString().equals(productionClassName) ||
                                    productionVariables.contains(nameExpr.getNameAsString())) {
                                if (!calledMethods.contains(n.getNameAsString())) {
                                    eagerCount++;
                                    if(eagerCount>1) {
                                    	OutputList o = new OutputList();
                                    	o.setMn(currentMethod.getNameAsString());
                                    	o.setLine(Integer.toString(n.getBegin().get().line));
                                    	o.setFn(f);
                                    	o.setPath(path);
                                    	o.setSmell(getSmellName());
                                    	outs.add(o);
                                    	try {
											fw.write(getSmellName()+","+f+","+currentMethod.getNameAsString()+","+Integer.toString(n.getBegin().get().line)+","+path+"\n");
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
                    					
                                    	System.out.println("Found Eager Smell at " + currentMethod.getNameAsString() + " Line: " + n.getBegin().get().line+ " ...path:" + path);
                                    }
                                    calledMethods.add(n.getNameAsString());
                                }

                            }
                        }
                    }
                }
            }
            super.visit(n, arg);
        }

        private NameExpr tempNameExpr;


        private void getFinalScope(MethodCallExpr n) {
            if (n.getScope().isPresent()) {
                if ((n.getScope().get() instanceof MethodCallExpr)) {
                    getFinalScope((MethodCallExpr) n.getScope().get());
                } else if ((n.getScope().get() instanceof NameExpr)) {
                    tempNameExpr = ((NameExpr) n.getScope().get());
                }
            }
        }

        @Override
        public void visit(VariableDeclarator n, Void arg) {
            if (Objects.equals(fileType, TEST_FILE)) {
                if (productionClassName.equals(n.getType().asString())) {
                    productionVariables.add(n.getNameAsString());
                }
            }
            super.visit(n, arg);
        }
    }



	@Override
	public ArrayList<OutputList> getOutputList() {
		
		return this.outs;
	}


	@Override
	public void setOutputList(ArrayList<OutputList> outs, String f, String path, FileWriter fw) {
		
		this.outs = outs;
		this.f = f;
		this.path = path;
		this.fw = fw;
	}
}